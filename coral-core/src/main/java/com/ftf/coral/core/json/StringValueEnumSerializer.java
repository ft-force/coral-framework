package com.ftf.coral.core.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ftf.coral.core.enums.StringValueEnum;

public class StringValueEnumSerializer extends JsonSerializer<StringValueEnum> {

    @Override
    public void serialize(StringValueEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getValue());
    }
}