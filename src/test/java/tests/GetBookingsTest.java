package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import model.BookingId;

public class GetBookingsTest extends BaseApiTest {

    @Test
    public void getBookingIds_should_return_non_empty_list() {
        BookingId[] ids = bookingControllerSpec.getBookingIdsResponse()
                .then()
                .statusCode(200)
                .extract()
                .as(BookingId[].class);

        Assert.assertNotNull(ids, "Booking IDs response should not be null");
        Assert.assertTrue(ids.length > 0, "Booking IDs list should not be empty");
    }


    @Test
    public void getBookingById_notFound_returns404() {
        Response response = bookingControllerSpec.getBookingIdResponse("9999999");
        Assert.assertEquals(response.statusCode(), 404);
        Assert.assertTrue(response.asString().contains("Not Found"), "Expected 404 body to contain 'Not Found'");
    }
}
