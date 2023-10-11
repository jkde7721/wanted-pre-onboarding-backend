package wanted.preonboarding.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {}

    public static String asJsonString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static <T> T asObject(String jsonString, Class<T> objectType) throws JsonProcessingException {
        return mapper.readValue(jsonString, objectType);
    }
}
