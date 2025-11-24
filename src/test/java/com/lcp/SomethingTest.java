package com.lcp;

import com.lcp.exchangerate.dto.ExchangeRateResponseDto;
import com.lcp.exchangerate.service.ExchangeRateService;
import com.lcp.shipment.service.ContainerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootTest
public class SomethingTest {

    @Autowired
    ExchangeRateService exchangeRateService;
    @Autowired
    ContainerService containerService;

    @Test
    public void testSomething() {
        ExchangeRateResponseDto transferRate = exchangeRateService.getExchangeRate(1L, 2L, LocalDate.now());
        System.out.println("Transfer Rate: " + transferRate);
    }

    @Test
    public void testContainerService() {
        // Assuming you have a method in ContainerService to get container details
        // Replace with actual method call and parameters
        var containerDetails = containerService.countByContainerType(10L);
        System.out.println("Container Details: " + containerDetails);
    }
}
