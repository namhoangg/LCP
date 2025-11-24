package com.lcp.common.converter;

import com.lcp.common.Constant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> valueSet) {
        return CollectionUtils.isNotEmpty(valueSet)
                ? valueSet.stream().map(Object::toString).collect(Collectors.joining(Constant.LIST_CONVERTER_SPLIT_CHAR)) : null;
    }

    @Override
    public List<String> convertToEntityAttribute(String stringValue) {
        return StringUtils.isNotEmpty(stringValue)
                ? Stream.of(stringValue.split(Constant.LIST_CONVERTER_SPLIT_CHAR)).collect(Collectors.toList()) : Collections.emptyList();
    }
}

