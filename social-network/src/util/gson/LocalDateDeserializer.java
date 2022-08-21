package util.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import exceptions.BadRequestException;
import util.DateUtil;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {

    private final DateUtil dateUtil = new DateUtil();

    @Override
    public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        try {
            return dateUtil.parseStringToLocalDate(jsonElement.getAsString());
        } catch (Exception e) {
            throw new BadRequestException("Invalid date format. Excepted: yyyy-MM-dd");
        }
    }
}
