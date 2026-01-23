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

        Assert.assertNotNull(token, "Auth token should not be null");
        Assert.assertFalse(token.isBlank(), "Auth token should not be blank");

        // delete normally should return 200 or 204, but in documentation we have 201 Created as expected result
        //docs and this test expected results should be fixed
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

