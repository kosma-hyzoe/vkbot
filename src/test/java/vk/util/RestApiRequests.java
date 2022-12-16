package vk.util;

import aquality.selenium.browser.AqualityServices;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class RestApiRequests {
    private RestApiRequests(){}

    public static HttpResponse<String> get(String url) {
        AqualityServices.getLogger().info("making a GET request with URL: \n\t" + url);
        return Unirest.get(url).asString();
    }

    public static HttpResponse<String> post(String url) {
        AqualityServices.getLogger().info("making a POST request with URL: \n\t" + url);
        return Unirest.post(url).asString();
    }

    public static void shutDownUnirest() {
        Unirest.shutDown();
    }
}
