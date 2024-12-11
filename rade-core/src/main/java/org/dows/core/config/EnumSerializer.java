package org.dows.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.dows.core.enums.RadeEnum;

import java.io.IOException;

public class EnumSerializer extends JsonSerializer<RadeEnum> {
    @Override
    public void serialize(RadeEnum radeEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(radeEnum.getCode());
    }
}
