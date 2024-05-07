package com.foodzy.web.az.core.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializers)
            throws IOException, JsonProcessingException {
        if (Objects.isNull(localDateTime))
            jsonGenerator.writeNull();
        else
            jsonGenerator.writeNumber(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli());
    }
}
