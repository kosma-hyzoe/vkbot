package restapi.tests;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.core.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import restapi.util.RestApiRequests;

import java.io.IOException;
import java.io.InputStream;

import static restapi.util.Serialization.getJsonNode;


public class BaseTest {
    public static final Logger logger = AqualityServices.getLogger();
    private static String testData;

    public static JsonNode getTestData() {
        return getJsonNode(testData);
    }

    @BeforeSuite
    public void setUp() {
        try {
            InputStream testDataInputStream = getClass().getClassLoader().getResourceAsStream("testData.local.json");
            assert testDataInputStream != null;
            testData = new String(testDataInputStream.readAllBytes());
        } catch (IOException e) {
            AqualityServices.getLogger().error(e.getMessage());
        }
    }

    @AfterSuite
    public void tearDown() {
        RestApiRequests.shutDownUnirest();
    }
}
