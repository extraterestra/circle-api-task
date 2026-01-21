package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.FrameworkConfig;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class ApiClient {
    private final FrameworkConfig config;
    private final ObjectMapper objectMapper;
    private final Slf4jRestAssuredFilter loggingFilter = new Slf4jRestAssuredFilter();

    public ApiClient(FrameworkConfig config) {
        this(config, new ObjectMapper());
    }

    public ApiClient(FrameworkConfig config, ObjectMapper objectMapper) {
        this.config = config;
        this.objectMapper = objectMapper;
    }

    /**
     * Convenience API: returns ONLY the mapped body (assumes HTTP 200).
     * Use *Response(...) variants if you need status/headers/raw body.
     */
    public <T> T get(String path, Class<T> responseType) {
        return getResponse(path, responseType).assertStatus(200).getBody();
    }

    /**
     * Convenience API: returns ONLY the mapped body (assumes HTTP 200).
     * Use *Response(...) variants if you need status/headers/raw body.
     */
    public <T> T post(String path, Object requestBody, Class<T> responseType) {
        return postResponse(path, requestBody, responseType).assertStatus(200).getBody();
    }

    public <T> T put(String path, Object requestBody, Class<T> responseType) {
        return putResponse(path, requestBody, responseType).assertStatus(200).getBody();
    }

    public void delete(String path) {
        deleteResponse(path).assertStatus(201); // default for restful-booker delete is 201 Created
    }

    /** Full response APIs (status/headers/raw body available). */
    public <T> ApiResponse<T> getResponse(String path, Class<T> responseType) {
        return execute("GET", path, null, responseType);
    }

    public <T> ApiResponse<T> postResponse(String path, Object requestBody, Class<T> responseType) {
        return execute("POST", path, requestBody, responseType);
    }

    public <T> ApiResponse<T> putResponse(String path, Object requestBody, Class<T> responseType) {
        return execute("PUT", path, requestBody, responseType);
    }

    public ApiResponse<Void> deleteResponse(String path) {
        return execute("DELETE", path, null, Void.class);
    }

    private <T> ApiResponse<T> execute(String method, String path, Object requestBody, Class<T> responseType) {
        Response res = baseRequest(requestBody).request(method, path);
        return toApiResponse(res, responseType);
    }

    private RequestSpecification baseRequest(Object requestBody) {
        RequestSpecification req = RestAssured.given()
                .baseUri(config.getBaseUrl())
                .config(restAssuredConfig())
                .filter(loggingFilter)
                // Be explicit: some servers behave differently based on Accept variants.
                .accept("application/json");

        if (requestBody != null) {
            req.contentType(ContentType.JSON);
            // Let RestAssured/Jackson serialize the object to JSON.
            req.body(requestBody);
        }
        return req;
    }

    private RestAssuredConfig restAssuredConfig() {
        // RestAssured delegates to Apache HTTP client underneath.
        return RestAssuredConfig.config().httpClient(
                HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", config.getConnectTimeoutMs())
                        .setParam("http.socket.timeout", config.getReadTimeoutMs())
                        .setParam("http.connection-manager.timeout", (long) config.getConnectTimeoutMs())
        );
    }

    private <T> ApiResponse<T> toApiResponse(Response res, Class<T> responseType) {
        int statusCode = res.getStatusCode();
        String rawBody = res.getBody() == null ? null : res.getBody().asString();

        Map<String, String> headers = new LinkedHashMap<>();
        res.getHeaders().forEach(h -> headers.put(h.getName(), h.getValue()));

        T body = fromJson(statusCode, rawBody, responseType);
        return new ApiResponse<>(statusCode, headers, rawBody, body);
    }

    private <T> T fromJson(int statusCode, String rawBody, Class<T> responseType) {
        if (responseType == null || responseType == Void.class || rawBody == null || rawBody.isBlank()) {
            return null;
        }
        if (responseType == String.class) {
            return responseType.cast(rawBody);
        }
        try {
            return objectMapper.readValue(rawBody, responseType);
        } catch (Exception e) {
            // Restful-booker (and many real APIs) may return non-JSON bodies (or unexpected shapes).
            return null;
        }
    }
}
