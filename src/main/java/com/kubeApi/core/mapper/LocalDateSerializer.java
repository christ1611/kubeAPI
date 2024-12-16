package com.kubeApi.core.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

public class LocalDateSerializer extends JsonSerializer<LocalDate>
{

    private final DateTimeFormatter formatter;

    public LocalDateSerializer()
    {
        super();
        formatter = new DateTimeFormatterBuilder().parseStrict()
                .appendPattern("uuuu/MM/dd")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);
    }

    public LocalDateSerializer(String pattern)
    {
        super();
        formatter = new DateTimeFormatterBuilder().parseStrict()
                .appendPattern(pattern)
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);
    }

    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value == null) {
            gen.writeString("");
        }
        else {
            gen.writeString(value.format(formatter));
        }
    }
}