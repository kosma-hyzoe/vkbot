package vk.util;

import java.time.Duration;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;

import static aquality.selenium.browser.AqualityServices.*;
import static vk.test.BaseTest.getTestData;

public class ConditionalWaits {
    private ConditionalWaits() {}

    private static boolean waitForTrue(BooleanSupplier booleanSupplier, int seconds, int millis, String description) {

        Duration secondsDuration = Duration.ofSeconds(seconds);
        Duration millisDuration = Duration.ofMillis(millis);

        String infoMessage = "conditional wait(" + seconds + "s" + millis + "ms)";
        if (description != null) {
            infoMessage = String.join("", infoMessage, ": ", description);
        }

        getLogger().info("conditional wait(" + seconds + "s" + millis + "ms): " + description);

        try {
            getConditionalWait().waitForTrue(booleanSupplier, secondsDuration, millisDuration, description);
        } catch (TimeoutException e) {
            String timeoutInfoMessage = "failed conditional wait";
            if (description != null) {
                timeoutInfoMessage = String.join("", timeoutInfoMessage, ": ", description);
            }
            getLogger().info(timeoutInfoMessage);

            return false;
        }
        return true;
    }

    public static boolean waitForTrue(BooleanSupplier booleanSupplier, String description) {
        int seconds = getTestData().get("waits").get("default").get("seconds").asInt();
        int millis = getTestData().get("waits").get("default").get("millis").asInt();

        return waitForTrue(booleanSupplier, seconds, millis, description);
    }

    public static boolean shortWaitForTrue(BooleanSupplier booleanSupplier, String description) {
        int seconds = getTestData().get("waits").get("short").get("seconds").asInt();
        int millis = getTestData().get("waits").get("short").get("millis").asInt();

        return waitForTrue(booleanSupplier, seconds, millis, description);
    }

    public static boolean longWaitForTrue(BooleanSupplier booleanSupplier, String description) {
        int seconds = getTestData().get("waits").get("long").get("seconds").asInt();
        int millis = getTestData().get("waits").get("long").get("millis").asInt();

        return waitForTrue(booleanSupplier, seconds, millis, description);
    }
}
