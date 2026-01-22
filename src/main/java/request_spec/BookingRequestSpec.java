package request_spec;

import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Booking;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookingRequestSpec extends config.BaseConfig {

    private final RequestSpecification spec;

    public BookingRequestSpec(String baseUrl) {
        int connectTimeoutMs = getIntProperty("http.timeout.connectMs", 5000);
        int socketTimeoutMs = getIntProperty("http.timeout.socketMs", 10000);
        int connectionRequestTimeoutMs = getIntProperty("http.timeout.connectionRequestMs", 5000);

        RestAssuredConfig configWithTimeouts = curlConfig.httpClient(
                HttpClientConfig.httpClientConfig()
                        // Apache HttpClient params used by RestAssured
                        .setParam("http.connection.timeout", connectTimeoutMs)
                        .setParam("http.socket.timeout", socketTimeoutMs)
                        .setParam("http.connection-manager.timeout", connectionRequestTimeoutMs)
        );

        this.spec = given()
                .config(configWithTimeouts)
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .urlEncodingEnabled(false);
    }

    public Response getBookingIdsResponse() {
        return given(spec)
                .when()
                .get("/booking")
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response createBookingResponse(Booking booking) {
        return given(spec)
                .body(booking)
                .when()
                .post("/booking")
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response getBookingIdResponse(String id) {
        return given(spec)
                .pathParam("id", id)
                .when()
                .get("/booking/{id}")
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response createTokenResponse(String username, String password) {
        return given(spec)
                // RestAssured ContentType.JSON expands to multiple values in Accept.
                // Use a single value to match API docs / expected curl.
                .header("Accept", "application/json")
                .body(Map.of("username", username, "password", password))
                .when()
                .post("/auth")
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response createTokenResponse() {
        return createTokenResponse(
                getRequiredProperty("booking.auth.username"),
                getRequiredProperty("booking.auth.password")
        );
    }

    public Response updateBookingResponse(String id, Booking booking, String token) {
        return given(spec)
                .pathParam("id", id)
                // RestAssured ContentType.JSON expands to multiple values in Accept.
                // Use a single value to match API docs / expected curl.
                .header("Accept", "application/json")
                .cookie("token", token)
                .body(booking)
                .when()
                .put("/booking/{id}")
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response deleteBookingResponse(String id, String token) {
        return given(spec)
                .pathParam("id", id)
                .header("Accept", "application/json")
                .cookie("token", token)
                .when()
                .delete("/booking/{id}")
                .then()
                .log().all()
                .extract()
                .response();
    }

}

