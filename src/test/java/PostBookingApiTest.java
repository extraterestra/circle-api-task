import client.ApiResponse;
import model.Booking;
import model.CreateBookingResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import testData.BookingTestData;

public class PostBookingApiTest extends BaseApiTest {

        @Test
        public void createBooking() {
            Booking bookingRequest = BookingTestData.testBooking();

            ApiResponse<CreateBookingResponse> postResponse = apiClient.postResponse("/booking", bookingRequest, CreateBookingResponse.class);
            Assert.assertEquals(postResponse.getStatusCode(), 200, "POST /booking should return 200");

            CreateBookingResponse createdBooking = postResponse.getBody();

            Assert.assertTrue(createdBooking.getBookingid() > 0, "bookingid should be > 0");
            Assert.assertNotNull(createdBooking.getBooking(), "booking object should be present");
            Assert.assertEquals(createdBooking.getBooking(), bookingRequest);

            int bookingId = createdBooking.getBookingid();
            Booking fetched = apiClient.get("/booking/" + bookingId, Booking.class);

            Assert.assertEquals(fetched.getFirstname(), bookingRequest.getFirstname());
            Assert.assertEquals(fetched.getLastname(), bookingRequest.getLastname());
            Assert.assertEquals(fetched.getTotalprice(), bookingRequest.getTotalprice());
            Assert.assertEquals(fetched.isDepositpaid(), bookingRequest.isDepositpaid());
            Assert.assertEquals(fetched.getAdditionalneeds(), bookingRequest.getAdditionalneeds());
            Assert.assertEquals(fetched.getBookingdates().getCheckin(), bookingRequest.getBookingdates().getCheckin());
            Assert.assertEquals(fetched.getBookingdates().getCheckout(), bookingRequest.getBookingdates().getCheckout());
        }
}
