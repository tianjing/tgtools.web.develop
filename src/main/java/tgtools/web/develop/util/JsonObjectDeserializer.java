package tgtools.web.develop.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import tgtools.json.JSONObject;

import java.io.IOException;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 9:13
 */
public class JsonObjectDeserializer extends org.springframework.boot.jackson.JsonObjectDeserializer<JSONObject> {
    @Override
    protected JSONObject deserializeObject(JsonParser jsonParser, DeserializationContext deserializationContext, ObjectCodec objectCodec, JsonNode jsonNode) throws IOException {
        try {
            return new JSONObject(jsonNode.toString());
        } catch (Exception e) {
            return new JSONObject();
        }
    }

}
