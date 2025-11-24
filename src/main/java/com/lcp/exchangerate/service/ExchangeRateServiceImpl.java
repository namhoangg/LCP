package com.lcp.exchangerate.service;

import com.lcp.common.dto.Exrate;
import com.lcp.common.dto.ExrateList;
import com.lcp.common.enumeration.CurrencyHardCode;
import com.lcp.exchangerate.dto.ExchangeRateResponseDto;
import com.lcp.exchangerate.entity.ExchangeRate;
import com.lcp.exchangerate.repository.ExchangeRateRepository;
import com.lcp.util.MapUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService{
    private static final String VND_EXCHANGE_RATE_URL = "https://portal.vietcombank.com.vn/Usercontrols/TVPortal.TyGia/pXML.aspx";
    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate;
    private final MappingJackson2XmlHttpMessageConverter xmlConverter;

    @Override
    public ExchangeRateResponseDto getExchangeRate(Long fromCurrencyId, Long toCurrencyId, LocalDate createdDate) {
        ExchangeRate exchangeRate = exchangeRateRepository.findByFromCurrencyIdAndToCurrencyIdAndCreatedDate(fromCurrencyId, toCurrencyId, createdDate);
        if (exchangeRate != null) {
            return createResponse(exchangeRate);
        }
        exchangeRate = importExchangeRate();
        if (exchangeRate == null) {
            throw new IllegalStateException("Exchange rate could not be imported");
        }
        return createResponse(exchangeRate);
    }

    private ExchangeRate importExchangeRate() {
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        String xmlResponse = restTemplate.getForObject(VND_EXCHANGE_RATE_URL, String.class);
        if (StringUtils.isEmpty(xmlResponse)) {
            return null;
        }
        try {
            ExrateList exrateList = xmlConverter.getObjectMapper().readValue(xmlResponse, ExrateList.class);
            for (Exrate exrate : exrateList.getExrates()) {
                if (Objects.equals(exrate.getCurrencyCode(), CurrencyHardCode.USD.getCode()))
                {
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setFromCurrencyId(CurrencyHardCode.USD.getId());
                    exchangeRate.setToCurrencyId(CurrencyHardCode.VND.getId());
                    exchangeRate.setTransferRate(parseBigDecimal(exrate.getTransfer()));
                    exchangeRate.setCreatedDate(LocalDate.now());
                    exchangeRateRepository.save(exchangeRate);
                    return exchangeRate;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private BigDecimal parseBigDecimal(String value) {
        if (StringUtils.isEmpty(value) || Objects.equals(value, "-")) {
            return null;
        }
        try {
            double numberValue = Double.parseDouble(value.replace(",", ""));
            return BigDecimal.valueOf(numberValue);
        } catch (Exception e) {
            return null;
        }
    }

    private ExchangeRateResponseDto createResponse(ExchangeRate entity) {
        ExchangeRateResponseDto response = new ExchangeRateResponseDto();
        MapUtil.copyProperties(entity, response);
        return response;
    }
}
