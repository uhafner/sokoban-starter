package edu.hm.hafner.sokoban;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import edu.hm.hafner.sokoban.model.HighScoreEntry;
import edu.hm.hafner.sokoban.model.Orientation;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A {@link HighScoreService} that stores/retrieves the {@link HighScoreEntry HighScoreEntries} in/from a cloud.
 */
public class CloudHighScoreService implements HighScoreService {

    private static final int WIDTH = 73;
    private static final String ALL = "all";
    private static final String CLOUD_HIGH_SCORE_SERVICE_URL = "http://localhost:8085";
    private final HighScoreEntry.HighScoreEntryBuilder highScoreEntryBuilder = new HighScoreEntry.HighScoreEntryBuilder();
    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
    private final MediaType jsonMediaType = MediaType.parse("application/json; charset=utf-8");
    private static final TypeReference<List<HighScoreEntry>> ENTRY_LIST_TYPE_REFERENCE = new TypeReference<List<HighScoreEntry>>() {
    };

    @Override
    public void registerSolution(final String playerName, final String levelName, final int numberOfMoves,
            final int numberOfAttempts, final Collection<Orientation> solution) {
        HighScoreEntry entry = highScoreEntryBuilder.withPlayerName(playerName)
                .withLevelName(levelName)
                .withNumberOfMoves(numberOfMoves)
                .withNumberOfAttempts(numberOfAttempts)
                .withSolution(new ArrayList<>(solution))
                .build();

        try {
            String body = objectWriter.writeValueAsString(entry);
            String url = getUrlWithLevelParam(levelName);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(jsonMediaType, body))
                    .build();

            Response response = client.newCall(request).execute();
            response.close();
        }
        catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    @Override
    public List<HighScoreEntry> getBoard(final String levelName) {
        String url = getUrlWithLevelParam(levelName);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IllegalStateException("No response body");
            }
            String responseJson = responseBody.string();
            responseBody.close();
            return mapper.readValue(responseJson, ENTRY_LIST_TYPE_REFERENCE);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    @Override
    public void printBoard(final String levelName, final FormattedPrinter printer) {
        List<HighScoreEntry> board = getBoard(levelName);
        printer.print("+" + StringUtils.repeat('-', WIDTH) + "+");
        printer.print("+" + StringUtils.center(levelName, WIDTH) + "+");
        printer.print("+" + StringUtils.repeat('-', WIDTH) + "+");
        System.out.format("|%-28s|%-9s|%-9s|%-24s|%n", "Player", "#Moves", "#Attempts", "Timestamp");
        printer.print("+" + StringUtils.repeat('-', WIDTH) + "+");
        board.forEach(e -> System.out.format("|%-28s|%9d|%9d|%24s|%n", e.getPlayerName(), e.getNumberOfMoves(),
                e.getNumberOfAttempts(), e.getTimestamp()));
        printer.print("+" + StringUtils.repeat('-', WIDTH) + "+");
    }

    @Override
    public void removeScoresFor(final String playerName) {
        String url = getUrlWithPlayerParam(playerName);

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try {
            Response response = client.newCall(request).execute();
            response.close();
        }
        catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    @Override
    public Collection<Orientation> getBestSolutionFor(final String levelName) {
        return getBoard(levelName).get(0).getSolution();
    }

    @Override
    public void clear() {
        removeScoresFor(ALL);
    }

    @Override
    public void setNumberOfEntriesPerLevel(final int numberOfEntries) {
        throw new UnsupportedOperationException();
    }

    private String getUrlWithLevelParam(final String levelName) {
        return getUrlWithParam("levelName", levelName);
    }

    private String getUrlWithPlayerParam(final String playerName) {
        return getUrlWithParam("playerName", playerName);
    }

    private String getUrlWithParam(final String paramName, final String value) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(CLOUD_HIGH_SCORE_SERVICE_URL)).newBuilder();

        if (paramName != null && value != null) {
            urlBuilder.addQueryParameter(paramName, value);
        }

        return urlBuilder.build().toString();
    }

    @Override
    public void printBoards(final FormattedPrinter printer) {
        printBoard(ALL, printer);
    }

    @Override
    public void printScoresFor(final String player, final FormattedPrinter printer) {
        throw new UnsupportedOperationException();
    }
}
