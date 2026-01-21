package client;

import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
public class ApiResponse<T> {
    private final int statusCode;
    private final Map<String, String> headers;
    private final String rawBody;
    private final T body;

    public ApiResponse(int statusCode, Map<String, String> headers, String rawBody, T body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.rawBody = rawBody;
        this.body = body;
    }

    public Optional<T> bodyOptional() {
        return Optional.ofNullable(body);
    }

    public ApiResponse<T> assertStatus(int expectedStatusCode) {
        if (statusCode != expectedStatusCode) {
            throw new AssertionError(
                    "Unexpected HTTP status. Expected: " + expectedStatusCode
                            + ", actual: " + statusCode
                            + ". Response body: " + (rawBody == null ? "<null>" : rawBody)
            );
        }
        return this;
    }
}
