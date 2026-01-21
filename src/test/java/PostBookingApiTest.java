import model.Booking;
import model.BookingDates;
import model.CreateBookingResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PostBookingApiTest extends BaseApiTest {

        @Test
        public void createBooking_returnsBookingIdAndBookingPayload() {
            BookingDates dates = new BookingDates();
            dates.setCheckin("2018-01-01");
            dates.setCheckout("2019-01-01");

            Booking bookingRequest = new Booking();
            bookingRequest.setFirstname("Jim");
            bookingRequest.setLastname("Brown");
            bookingRequest.setTotalprice(111);
            bookingRequest.setDepositpaid(true);
            bookingRequest.setBookingdates(dates);
            bookingRequest.setAdditionalneeds("Breakfast");

            CreateBookingResponse createdBooking = apiClient.post("/booking", bookingRequest, CreateBookingResponse.class);

            Assert.assertNotNull(createdBooking, "Create booking response must be deserialized");
            Assert.assertTrue(createdBooking.getBookingid() > 0, "bookingid should be > 0");
            Assert.assertNotNull(createdBooking.getBooking(), "booking object should be present");

            Assert.assertEquals(createdBooking.getBooking(), bookingRequest);

            int bookingId = createdBooking.getBookingid();

            Booking fetched = apiClient.get("/booking/" + bookingId, Booking.class);

            Assert.assertNotNull(fetched.getBookingdates(), "Fetched bookingdates must be present");

            Assert.assertEquals(fetched.getFirstname(), bookingRequest.getFirstname());
            Assert.assertEquals(fetched.getLastname(), bookingRequest.getLastname());
            Assert.assertEquals(fetched.getTotalprice(), bookingRequest.getTotalprice());
            Assert.assertEquals(fetched.isDepositpaid(), bookingRequest.isDepositpaid());
            Assert.assertEquals(fetched.getAdditionalneeds(), bookingRequest.getAdditionalneeds());
            Assert.assertEquals(fetched.getBookingdates().getCheckin(), bookingRequest.getBookingdates().getCheckin());
            Assert.assertEquals(fetched.getBookingdates().getCheckout(), bookingRequest.getBookingdates().getCheckout());
        }
}
