package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import testData.BookingTestData;

public class DeleteBookingTest extends BaseApiTest {

    @Test
    public void deleteBooking_should_remove_existing_booking() {
        int bookingId = bookingControllerSpec.createBookingResponse(BookingTestData.johnSmith().build())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getInt("bookingid");

        String token = bookingControllerSpec.createTokenResponse()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");

        Assert.assertEquals(
                bookingControllerSpec.deleteBookingResponse(String.valueOf(bookingId), token).statusCode(),
                201
        );

        Assert.assertEquals(
                bookingControllerSpec.getBookingIdResponse(String.valueOf(bookingId)).statusCode(),
                404
        );
    }
}

