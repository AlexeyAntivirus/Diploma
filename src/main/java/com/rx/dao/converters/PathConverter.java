package com.rx.dao.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.nio.file.Path;
import java.nio.file.Paths;

@Converter
public class PathConverter implements AttributeConverter<Path, String> {
    @Override
    public String convertToDatabaseColumn(Path attribute) {
        return attribute.toString();
    }

    @Override
    public Path convertToEntityAttribute(String dbData) {
        return Paths.get(dbData);
    }
}
