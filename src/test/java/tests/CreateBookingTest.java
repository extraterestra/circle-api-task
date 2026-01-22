package tests;

import assertions.BookingAssertions;
import model.Booking;
import model.CreateBookingResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testData.BookingTestData;

import java.util.List;

public class CreateBookingTest extends BaseApiTest {

    @DataProvider(name = "data-provider")
    public Object[][] dpMethod() {
        return BookingTestData.bookingRequests();
    }

    @Test(dataProvider = "data-provider")
    public void should_create_booking_and_find_id_in_booking_ids(Booking booking) {
        CreateBookingResponse createResponse = bookingControllerSpec.createBookingResponse(booking)
                .then()
                .statusCode(200)
                .extract()
                .as(CreateBookingResponse.class);

        BookingAssertions.assertBookingEqualsSoft(createResponse.getBooking(), booking);

        List<Integer> bookingIds = bookingControllerSpec.getBookingIdsResponse()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("bookingid", Integer.class);

        Assert.assertTrue(bookingIds.contains(createResponse.getBookingid()), "Created bookingId not found in /booking list");
    }
}
