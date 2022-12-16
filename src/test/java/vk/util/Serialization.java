package vk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static vk.VkTest.logger;

public class Serialization {
    private Serialization(){}

    private static final ObjectMapper jacksonObjectMapper = new ObjectMapper();

    public static <T> T deserialize(String string, Class<T> T){
        try {
            return Serialization.jacksonObjectMapper.readValue(string, T);
        } catch (JsonProcessingException e) {
            logger.error(String.format("Failed to deserialize object \"%s\"  from string \"%s\"\n\t",
                    T, string) + e.getMessage());
            return null;
        }
    }

    public static JsonNode getJsonNode(String string) {
        try {
            return jacksonObjectMapper.readTree(string);
        } catch (JsonProcessingException e){
            logger.error("Failed to parse string '" + string + "':\n\t" + e.getMessage());
            return null;
        }
    }

}
