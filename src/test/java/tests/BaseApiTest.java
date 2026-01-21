package tests;

import client.ApiClient;
import config.FrameworkConfig;
import org.testng.annotations.BeforeClass;

public abstract class BaseApiTest {
    protected ApiClient apiClient;

    @BeforeClass
    public void setUpClient() {
        apiClient = new ApiClient(new FrameworkConfig());
    }
}
