package eu.archivesportaleurope.apeapi.response.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Mahbub
 */
public class JsonDateDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // get the value from the JSON
         long timeInMilliseconds = Long.parseLong(json.getAsJsonPrimitive().getAsString());

         Calendar calendar = Calendar.getInstance();
         calendar.setTimeInMillis(timeInMilliseconds);
         return calendar.getTime();
    }
}
