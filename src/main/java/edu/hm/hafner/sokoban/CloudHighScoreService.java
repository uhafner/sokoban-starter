package edu.hm.hafner.sokoban;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;

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
    private static final String LINE = "+" + StringUtils.repeat('-', WIDTH) + "+";
    private static final String ALL = "all";

    private static final String CLOUD_HIGH_SCORE_SERVICE_URL = "http://localhost:8085";

    private final OkHttpClient client = new OkHttpClient();

    private static final TypeReference<List<HighScoreEntry>> ENTRY_LIST_TYPE_REFERENCE = new TypeReference<List<HighScoreEntry>>() {
    };

    @Override
    public void registerSolution(final String playerName, final String levelName, final int numberOfMoves,
            final int numberOfAttempts, final Collection<Orientation> solution) {
        HighScoreEntry entry = new HighScoreEntry.HighScoreEntryBuilder().withPlayerName(playerName)
                .withLevelName(levelName)
                .withNumberOfMoves(numberOfMoves)
                .withNumberOfAttempts(numberOfAttempts)
                .withSolution(new ArrayList<>(solution))
                .build();

        try {
            ObjectWriter objectWriter = getObjectMapper().writer().withDefaultPrettyPrinter();
            String body = objectWriter.writeValueAsString(entry);
            String url = getUrlWithLevelParam(levelName);
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                validateBodyOfResponse(response);
            }
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<HighScoreEntry> getBoard(final String levelName) {
        String url = getUrlWithLevelParam(levelName);

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return convertResponse(validateBodyOfResponse(response));
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<HighScoreEntry> convertResponse(final String responseBody) throws IOException {
        return getObjectMapper().readValue(responseBody, ENTRY_LIST_TYPE_REFERENCE);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Instant.class, new InstantDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

    private String validateBodyOfResponse(final Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IllegalStateException("Error: Server returned: " + response.toString());
        }
        try (ResponseBody responseBody = response.body()) {
            if (responseBody == null) {
                throw new IllegalStateException("No response body");
            }
            String responseJson = responseBody.string();
            if ("{error}".equals(responseJson)) {
                throw new IllegalStateException("Error: Server returned ERROR: " + response.toString());
            }
            return responseJson;
        }
    }

    @Override
    public void printBoard(final String levelName, final FormattedPrinter printer) {
        List<HighScoreEntry> board = getBoard(levelName);
        printLine(printer);
        printer.print("+%-73s+", levelName);
        printLine(printer);
        System.out.format("|%-28s|%-9s|%-9s|%-24s|%n", "Player", "#Moves", "#Attempts", "Timestamp");
        printLine(printer);
        board.forEach(e -> System.out.format("|%-28s|%9d|%9d|%24s|%n", e.getPlayerName(), e.getNumberOfMoves(),
                e.getNumberOfAttempts(), e.getTimestamp()));
        printLine(printer);
    }

    @SuppressWarnings("ErrorProne")
    private void printLine(final FormattedPrinter printer) {
        printer.print(LINE);
    }

    @Override
    public void removeScoresFor(final String playerName) {
        String url = getUrlWithPlayerParam(playerName);

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            validateBodyOfResponse(response);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
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

    private static class InstantDeserializer extends JsonDeserializer<Instant> {
        /**
         * Deserializes String representation of an {@link Instant} to an {@link Instant} Object.
         */
        @Override
        public Instant deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) {
            try {
                String dateAsString = jsonParser.getText();
                if (dateAsString != null) {
                    return Instant.parse(dateAsString + "Z"); // Z = UTC timezone
                }
            }
            catch (IOException | DateTimeParseException exception) {
                // ignore and return now
            }
            return Instant.now();
        }
    }
}
