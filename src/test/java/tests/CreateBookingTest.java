package tests;

import assertions.BookingAssertions;
import model.Booking;
import model.CreateBookingResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import testData.BookingTestData;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.then;

public class CreateBookingTest extends BaseApiTest {

    @DataProvider(name = "data-provider")
    public Object[][] dpMethod() {
        return BookingTestData.bookingRequests();
    }

    @Test(dataProvider = "data-provider")
    public void createBooking_shouldPersistAndAppearInBookingList(Booking booking) {
        CreateBookingResponse createResponse = bookingControllerSpec.createBookingResponse(booking)
                .then()
                //normally post return 201 but according to documentation
                // https://restful-booker.herokuapp.com/apidoc/index.html#api-Booking-CreateBooking
                //200 is expected
                .statusCode(200)
                .extract()
                .as(CreateBookingResponse.class);

        Assert.assertNotNull(createResponse.getBookingid(), "bookingId should not be null");
        Assert.assertTrue(createResponse.getBookingid() > 0, "bookingId should be positive");

        BookingAssertions.assertBookingEqualsSoft(createResponse.getBooking(), booking);

        List<Integer> bookingIds = bookingControllerSpec.getBookingIdsResponse()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("bookingid", Integer.class);

        Assert.assertTrue(bookingIds.contains(createResponse.getBookingid()), "Created bookingId not found in /booking list");

        //cleaning up
        String token = bookingControllerSpec.createTokenResponse()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");

        bookingControllerSpec.deleteBookingResponse(String.valueOf(createResponse.getBookingid()), token).
                then()
                .statusCode(is(201));
    }
}
