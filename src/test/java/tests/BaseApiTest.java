package tests;

import org.testng.annotations.BeforeClass;
import config.BaseConfig;
import request_spec.BookingRequestSpec;

public abstract class BaseApiTest extends BaseConfig {
    protected BookingRequestSpec bookingControllerSpec;

    @BeforeClass
    public void setUp() {
        bookingControllerSpec = new BookingRequestSpec(getRequiredProperty("booking.baseUrl"));
    }
}