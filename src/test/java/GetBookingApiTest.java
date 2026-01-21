import client.ApiResponse;
import model.Booking;
import model.BookingId;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetBookingApiTest extends BaseApiTest {

    @Test
    public void getAllBookings_returnsNonEmptyList() {
        ApiResponse<BookingId[]> getResponse = apiClient.getResponse("/booking", BookingId[].class)
                .assertStatus(200);

        BookingId[] ids = getResponse.getBody();
        Assert.assertNotNull(ids, "Booking list must be deserialized");
        List<BookingId> idList = new ArrayList<>(Arrays.asList(ids));
        Assert.assertTrue(idList.size() > 0, "Expected at least 1 booking id");

        int firstId = idList.get(0).getBookingid();
        Assert.assertTrue(firstId > 0, "First bookingid should be > 0");
    }

    @Test
    public void getBookingById_returnsBooking() {
        ApiResponse<Booking> getResponse = apiClient.getResponse("/booking/10", Booking.class);
        Assert.assertEquals(getResponse.getStatusCode(), 200, "GET /bookingId should return 200");

        Booking booking = getResponse.getBody();
        Assert.assertNotNull(booking.getBookingdates(), "booking dates must be present");

        Assert.assertNotNull(booking.getFirstname());
        Assert.assertFalse(booking.getFirstname().isBlank());
        Assert.assertNotNull(booking.getLastname());
        Assert.assertTrue(booking.getTotalprice() >= 0, "totalprice should be >= 0");
        Assert.assertNotNull(booking.getBookingdates().getCheckin());
        Assert.assertFalse(booking.getBookingdates().getCheckin().isBlank());
        Assert.assertNotNull(booking.getBookingdates().getCheckout());
        Assert.assertFalse(booking.getBookingdates().getCheckout().isBlank());
    }

    @Test
    public void getBookingById_notFound_returns404() {
        apiClient.getResponse("/booking/9999999", String.class)
                .assertStatus(404);
    }
}
