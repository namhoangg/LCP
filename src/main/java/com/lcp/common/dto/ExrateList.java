package com.lcp.common.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@JacksonXmlRootElement(localName = "ExrateList")
@Data
public class ExrateList {
    @JacksonXmlProperty(localName = "DateTime")
    private String dateTime;

    @JacksonXmlProperty(localName = "Source")
    private String source;

    @JacksonXmlProperty(localName = "Exrate")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Exrate> exrates;
}
