package com.kubeApi.core.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;

@Slf4j
public class LocalDateDeserializer extends JsonDeserializer<LocalDate>
{
    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }

    private final DateTimeFormatter formatter ;
    private final DateTimeFormatter yyyyMMdd ;

    public LocalDateDeserializer() {
        formatter = new DateTimeFormatterBuilder().parseStrict()
                .appendPattern("uuuu/MM/dd")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);
        yyyyMMdd = new DateTimeFormatterBuilder().parseStrict()
                .appendPattern("uuuuMMdd")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);
    }

    public LocalDateDeserializer(String pattern) {
        formatter = new DateTimeFormatterBuilder().parseStrict()
            .appendPattern(pattern)
            .toFormatter()
            .withResolverStyle(ResolverStyle.STRICT);

        yyyyMMdd = new DateTimeFormatterBuilder().parseStrict()
                .appendPattern("uuuuMMdd")
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT);
    }

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if( StringUtils.isBlank(p.getValueAsString()) ) {
            return null;
        }
        else {
            String dt = p.getValueAsString();
            try {
                if(dt.length() == 8)
                    return LocalDate.parse(p.getValueAsString(), yyyyMMdd);
                else
                    return LocalDate.parse(p.getValueAsString(), formatter);
            }
            catch (Exception ex) {
                log.debug("value[{}] - formatter[{}]", p.getValueAsString(), formatter);
                log.error("Error ", ex);
                return null;
            }
        }
    }
}