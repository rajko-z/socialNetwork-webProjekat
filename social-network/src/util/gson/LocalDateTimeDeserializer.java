package util.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import exceptions.BadRequestException;
import util.DateUtil;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
    private final DateUtil dateUtil = new DateUtil();


    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        try {
            return dateUtil.parseStringToLocalDateTime(jsonElement.getAsString());
        } catch (Exception e) {
            throw new BadRequestException("Invalid date format. Excepted: yyyy-MM-dd HH:mm:ss");
        }
    }
}
