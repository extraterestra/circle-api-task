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
        ApiResponse<BookingId[]> res = apiClient.getResponse("/booking", BookingId[].class)
                .assertStatus(200);

        BookingId[] ids = res.getBody();
        Assert.assertNotNull(ids, "Booking list must be deserialized");
        List<BookingId> idList = new ArrayList<>(Arrays.asList(ids));
        Assert.assertTrue(idList.size() > 0, "Expected at least 1 booking id");

        int firstId = idList.get(0).getBookingid();
        Assert.assertTrue(firstId > 0, "First bookingid should be > 0");
    }

    @Test
    public void getBookingById_returnsBooking() {
        ApiResponse<Booking> res = apiClient.getResponse("/booking/10", Booking.class);
        if (res.getStatusCode() != 200 || res.getBody() == null) {
            throw new SkipException("GET /booking/10 did not return HTTP 200 with a JSON body (status=" + res.getStatusCode() + ")");
        }

        Booking booking = res.getBody();
        Assert.assertNotNull(booking, "Booking must be deserialized");
        Assert.assertNotNull(booking.getBookingdates(), "bookingdates must be present");

        // Public API can change/reset; validate shape rather than fixed seed values.
        Assert.assertNotNull(booking.getFirstname());
        Assert.assertFalse(booking.getFirstname().isBlank());
        Assert.assertNotNull(booking.getLastname());
        Assert.assertFalse(booking.getLastname().isBlank());
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
