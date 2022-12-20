package vk.test;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import aquality.selenium.core.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import vk.model.Credentials;
import vk.util.RestApiRequests;

import java.io.IOException;
import java.io.InputStream;

import static vk.util.Serialization.deserialize;
import static vk.util.Serialization.getJsonNode;

public class BaseTest {
    private static String testData;
    protected static Browser browser;
    protected static Credentials credentials;
    public static final Logger logger = AqualityServices.getLogger();

    public static JsonNode getTestData() {
        return getJsonNode(testData);
    }


    @BeforeMethod
    public void setUp() {
        try {
            InputStream testDataInputStream = getClass().getClassLoader().getResourceAsStream("testData.local.json");
            assert testDataInputStream != null;
            testData = new String(testDataInputStream.readAllBytes());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        browser = AqualityServices.getBrowser();
        browser.maximize();
        credentials = deserialize(getTestData().get("credentials").toString(), Credentials.class);
    }

    @AfterMethod
    public void tearDown() {
        RestApiRequests.shutDownUnirest();
        browser.quit();
    }
}
