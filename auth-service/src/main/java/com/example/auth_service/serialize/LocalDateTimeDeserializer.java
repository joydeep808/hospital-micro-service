package com.example.auth_service.serialize;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime>{
private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  @Override
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext arg1) throws IOException, JacksonException {
    String dateString = parser.getValueAsString();
    return LocalDate.parse(dateString , FORMATTER).atStartOfDay();
  }
}
