package testData;

import builder.BookingRequestBuilder;

public final class BookingTestData {
    private BookingTestData() {}

    public static BookingRequestBuilder johnSmith() {
        return new BookingRequestBuilder()
                .setFirstname("John")
                .setLastname("Smith")
                .setTotalPrice(123)
                .setDepositPaid(true)
                .setCheckin("2026-01-22")
                .setCheckout("2026-01-25")
                .setAdditionalNeeds("Breakfast");
    }

    public static BookingRequestBuilder janeDoe() {
        return new BookingRequestBuilder()
                .setFirstname("Jane")
                .setLastname("Doe")
                .setTotalPrice(456)
                .setDepositPaid(false)
                .setCheckin("2026-02-01")
                .setCheckout("2026-02-10")
                .setAdditionalNeeds("Late checkout");
    }

    public static Object[][] bookingRequests() {
        return new Object[][]{
                {johnSmith().build()},
                {janeDoe().build()}
        };
    }

    public static final class Update {
        private Update() {}

        public static BookingRequestBuilder original() {
            return new BookingRequestBuilder()
                    .setFirstname("John")
                    .setLastname("Smith")
                    .setTotalPrice(123)
                    .setDepositPaid(true)
                    .setCheckin("2026-01-22")
                    .setCheckout("2026-01-25")
                    .setAdditionalNeeds("Breakfast");
        }

        public static BookingRequestBuilder updated() {
            return new BookingRequestBuilder()
                    .setFirstname("Josh")
                    .setLastname("Allen")
                    .setTotalPrice(111)
                    .setDepositPaid(true)
                    .setCheckin("2018-01-01")
                    .setCheckout("2019-01-01")
                    .setAdditionalNeeds("Dinner");
        }
    }
}

