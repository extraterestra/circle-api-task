package tests;

import assertions.BookingAssertions;
import model.Booking;
import org.testng.annotations.Test;
import testData.BookingTestData;

import static org.hamcrest.core.Is.is;

public class UpdateBookingTest extends BaseApiTest{

    @Test
    public void updateBooking_should_update_existing_booking() {
        Booking original = BookingTestData.Update.original().build();

        int bookingId = bookingControllerSpec.createBookingResponse(original)
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

        Booking updatedRequest = BookingTestData.Update.updated().build();

        Booking updatedResponse = bookingControllerSpec.updateBookingResponse(String.valueOf(bookingId), updatedRequest, token)
                .then()
                .statusCode(200)
                .extract()
                .as(Booking.class);

        BookingAssertions.assertBookingEqualsSoft(updatedResponse, updatedRequest);

        //cleaning up
        bookingControllerSpec.deleteBookingResponse(String.valueOf(bookingId), token).
                then()
                .statusCode(is(201));
    }
}

