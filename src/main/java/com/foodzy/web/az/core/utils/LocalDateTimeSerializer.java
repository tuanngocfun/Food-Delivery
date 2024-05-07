package com.foodzy.web.az.core.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    private final ZoneOffset zone;

    public LocalDateTimeSerializer() {
        this(ZoneOffset.UTC);  // Default to UTC
    }

    public LocalDateTimeSerializer(ZoneOffset zone) {
        this.zone = zone;
    }

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        if (localDateTime == null) {
            jsonGenerator.writeNull();
        } else {
            long timestamp = localDateTime.toInstant(zone).toEpochMilli();
            jsonGenerator.writeNumber(timestamp);
        }
    }
}
