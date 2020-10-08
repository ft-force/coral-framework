package com.ftf.coral.core.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ftf.coral.core.enums.IntegerValueEnum;

public class IntegerValueEnumSerializer extends JsonSerializer<IntegerValueEnum> {

    @Override
    public void serialize(IntegerValueEnum value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        gen.writeNumber(value.getValue());
    }
}