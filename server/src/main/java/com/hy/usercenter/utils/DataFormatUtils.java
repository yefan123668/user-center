package com.hy.usercenter.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFormatUtils extends JsonSerializer<Date> {

    private SimpleDateFormat sdf;

    public DataFormatUtils(String pattern) {
        this.sdf = new SimpleDateFormat(pattern);
    }

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, IOException {
        jsonGenerator.writeString(sdf.format(date));
    }
}
