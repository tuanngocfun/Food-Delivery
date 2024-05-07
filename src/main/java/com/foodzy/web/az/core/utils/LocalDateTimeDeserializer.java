package com.foodzy.web.az.core.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private final ZoneId zone;

    public LocalDateTimeDeserializer() {
        this(ZoneId.of("UTC"));  // Default to UTC
    }

    public LocalDateTimeDeserializer(ZoneId zone) {
        this.zone = zone;
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_NUMBER_INT) {
            long timestamp = jsonParser.getLongValue();
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zone);
        } else if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
            return LocalDateTime.parse(jsonParser.getText());
        } else {
            throw new IOException("Unexpected token " + jsonParser.getCurrentToken() + ", expected a numeric timestamp or a valid datetime string");
        }
    }
}
