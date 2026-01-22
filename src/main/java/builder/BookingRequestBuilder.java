package builder;

import lombok.Data;
import lombok.experimental.Accessors;
import model.Booking;
import model.BookingDates;

@Data
@Accessors(chain = true)
public class BookingRequestBuilder {
    private String firstname;
    private String lastname;
    private int totalPrice;
    private boolean depositPaid;
    private String checkin;
    private String checkout;
    private String additionalNeeds;

    public Booking build() {
        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);

        Booking booking = new Booking();
        booking.setFirstname(firstname);
        booking.setLastname(lastname);
        booking.setTotalprice(totalPrice);
        booking.setDepositpaid(depositPaid);
        booking.setBookingdates(bookingDates);
        booking.setAdditionalneeds(additionalNeeds);

        return booking;
    }
}
