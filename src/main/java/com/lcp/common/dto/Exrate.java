package com.lcp.common.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Exrate {
    @JacksonXmlProperty(isAttribute = true, localName = "CurrencyCode")
    private String currencyCode;

    @JacksonXmlProperty(isAttribute = true, localName = "CurrencyName")
    private String currencyName;

    @JacksonXmlProperty(isAttribute = true, localName = "Buy")
    private String buy;

    @JacksonXmlProperty(isAttribute = true, localName = "Transfer")
    private String transfer;

    @JacksonXmlProperty(isAttribute = true, localName = "Sell")
    private String sell;
}
