package assertions;

import org.testng.asserts.SoftAssert;
import model.Booking;
import model.BookingDates;

public final class BookingAssertions {
    private BookingAssertions() {}

    public static void assertBookingEqualsSoft(Booking actual, Booking expected) {
        SoftAssert soft = new SoftAssert();

        soft.assertNotNull(actual, "Actual Booking is null");
        soft.assertNotNull(expected, "Expected Booking is null");

        if (actual != null && expected != null) {
            soft.assertEquals(actual.getFirstname(), expected.getFirstname(), "first name mismatch");
            soft.assertEquals(actual.getLastname(), expected.getLastname(), "last name mismatch");
            soft.assertEquals(actual.getTotalprice(), expected.getTotalprice(), "total price mismatch");
            soft.assertEquals(actual.isDepositpaid(), expected.isDepositpaid(), "deposit paid mismatch");
            soft.assertEquals(actual.getAdditionalneeds(), expected.getAdditionalneeds(), "additional needs mismatch");

            assertBookingDatesEqualsSoft(soft, actual.getBookingdates(), expected.getBookingdates());
        }

        soft.assertAll();
    }

    private static void assertBookingDatesEqualsSoft(SoftAssert soft, BookingDates actual, BookingDates expected) {
        soft.assertNotNull(actual, "bookingdates (actual) is null");
        soft.assertNotNull(expected, "bookingdates (expected) is null");

        if (actual != null && expected != null) {
            soft.assertEquals(actual.getCheckin(), expected.getCheckin(), "booking dates.checkin mismatch");
            soft.assertEquals(actual.getCheckout(), expected.getCheckout(), "booking dates.checkout mismatch");
        }
    }
}


