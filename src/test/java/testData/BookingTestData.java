package testData;

import builder.BookingRequestBuilder;
import model.Booking;

public final class BookingTestData {
    private BookingTestData() {
    }

    public static Booking testBooking() {
        return BookingRequestBuilder.booking()
                .firstname("Jim")
                .lastname("Brown")
                .totalprice(111)
                .depositpaid(true)
                .checkin("2018-01-01")
                .checkout("2019-01-01")
                .additionalneeds("Breakfast")
                .build();
    }
}
