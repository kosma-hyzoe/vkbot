package restapi.util;

import aquality.selenium.browser.AqualityServices;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.util.ArrayList;
import java.util.Set;

import static restapi.tests.BaseTest.getTestData;


public class Serialization {
    private Serialization(){}

    private static final ObjectMapper jacksonObjectMapper = new ObjectMapper();

    public static JsonSchema getJsonSchema(JsonNode jsonNode) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        return factory.getSchema(jsonNode);
    }

    public static String getQuery(String testDataKey){
        return getTestData().get("database").get("queries").get(testDataKey).asText();
    }
    
    public static <T> T getDefault(Class<T> clazz){
        String key = clazz.getSimpleName();
        return deserialize(getTestData().get("database").get("defaults").get(key).toString(), clazz);
    }

    public static <T> String serialize(T object){
        try {
            return jacksonObjectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            AqualityServices.getLogger().error("failed to serialize object \"" + object.toString() + "\"");
            return null;
        }
    }

    public static <T> T deserialize(String string, Class<T> T){
        try {
            return Serialization.jacksonObjectMapper.readValue(string, T);
        } catch (JsonProcessingException e) {
            AqualityServices.getLogger().error(String.format("Failed to deserialize object \"%s\"  from string \"%s\"\n\t",
                    T, string) + e.getMessage());
            return null;
        }
    }
    

    public static <T> ArrayList<T> deserializeList(String string, Class<T> T) {
        ArrayList<T> objects = new ArrayList<>();
        JsonNode listOfObjectsNode = getJsonNode(string);
        String objectString;
        for (int i = 0; i < listOfObjectsNode.size(); i++) {
            objectString = listOfObjectsNode.get(i).toString();
            objects.add(deserialize(objectString, T));
        }
        return objects;
    }

    public static boolean isValidJson(String string) {
        try {
            jacksonObjectMapper.readTree(string);
        } catch (JacksonException e) {
            return false;
        }
        return true;
    }

    public static JsonNode getJsonNode(String string) {
        try {
            return jacksonObjectMapper.readTree(string);
        } catch (JsonProcessingException e){
            AqualityServices.getLogger().error("Failed to parse string '" + string + "':\n\t" + e.getMessage());
            return null;
        }
    }

    public static boolean obeysProvidedSchema(JsonSchema schema, Object object) {
        JsonNode node = jacksonObjectMapper.valueToTree(object);
        Set<ValidationMessage> errors = schema.validate(node);
        for (ValidationMessage message : errors) {
            AqualityServices.getLogger().warn("Invalid json schema: " + message);
        }
        return errors.isEmpty();
    }
}
