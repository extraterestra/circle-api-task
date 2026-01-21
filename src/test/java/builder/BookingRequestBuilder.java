package builder;

import lombok.Setter;
import lombok.experimental.Accessors;
import model.Booking;
import model.BookingDates;

@Setter
@Accessors(fluent = true, chain = true)
public class BookingRequestBuilder {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private String checkin;
    private String checkout;
    private String additionalneeds;

    public static BookingRequestBuilder booking() {
        return new BookingRequestBuilder();
    }

    public Booking build() {
        BookingDates dates = new BookingDates();
        dates.setCheckin(checkin);
        dates.setCheckout(checkout);

        Booking booking = new Booking();
        booking.setFirstname(firstname);
        booking.setLastname(lastname);
        booking.setTotalprice(totalprice);
        booking.setDepositpaid(depositpaid);
        booking.setBookingdates(dates);
        booking.setAdditionalneeds(additionalneeds);
        return booking;
    }
}
