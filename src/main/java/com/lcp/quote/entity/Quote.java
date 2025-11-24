package com.lcp.quote.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lcp.client.entity.Client;
import com.lcp.common.EntityBase;
import com.lcp.common.converter.StringListConverter;
import com.lcp.common.enumeration.CurrencyHardCode;
import com.lcp.common.enumeration.QuoteStatus;
import com.lcp.provider.entity.Provider;
import com.lcp.quote.dto.CargoChargePrice;
import com.lcp.quote.dto.ServiceChargeMarkup;
import com.lcp.rate.entity.GoodKind;
import com.lcp.setting.entity.Unloco;
import com.lcp.util.MathUtil;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.lcp.quote.entity.QuotePriceDetail_.totalPrice;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "quote")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Quote extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quote_gen")
    @SequenceGenerator(name = "quote_gen", sequenceName = "quote_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('quote_seq')")
    private Long id;
    private String code;
    private Long clientId;
    private Boolean isRequest;
    private Long originId;
    private Long destinationId;
    private LocalDate etd;
    private LocalDate eta;
    private Long goodKindId;
    private String goodDescription;
    private String note = "";

    // Will be set when quote is accepted
    private Long providerId;

    // After quote sent, it must have
    private LocalDate validUntil;
    @Enumerated(EnumType.STRING)
    private QuoteStatus quoteStatus;

    // For staff draft
    private Boolean isStaffDraft = false;

    private String staffNote = "";

    // To store which container type client requested
    @Convert(converter = StringListConverter.class)
    private List<String> containerTypeIds;

    @Convert(converter = StringListConverter.class)
    private List<String> serviceChargeIds;

    private Long estimatedTransitTime;

    @Type(type = "jsonb")
    @Column(name = "cargo_charge_markup", columnDefinition = "jsonb")
    private JsonNode cargoChargeMarkup = JsonNodeFactory.instance.arrayNode();

    @Type(type = "jsonb")
    @Column(name = "service_charge_markup", columnDefinition = "jsonb")
    private JsonNode serviceChargeMarkup = JsonNodeFactory.instance.arrayNode();

    @Type(type = "jsonb")
    @Column(name = "cargo_charge_price", columnDefinition = "jsonb")
    private JsonNode cargoChargePrice = JsonNodeFactory.instance.arrayNode();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_quote_client"))
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_quote_origin"))
    private Unloco origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinationId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_quote_destination"))
    private Unloco destination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goodKindId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_quote_goodKind"))
    private GoodKind goodKind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "providerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_quote_provider"))
    private Provider provider;

    public List<ServiceChargeMarkup> getServiceChargeMarkupList() {
        if (serviceChargeMarkup == null || serviceChargeMarkup.isEmpty()) {
            return List.of();
        }
        ArrayNode inputArray = (ArrayNode) serviceChargeMarkup;
        List<ServiceChargeMarkup> serviceChargeMarkupList = new ArrayList<>();

        for (JsonNode item : inputArray) {
            Long serviceChargeId = item.get("serviceChargeId").asLong();
            BigDecimal basePrice = item.get("basePrice").decimalValue();
            BigDecimal markup = item.get("markup").decimalValue();
            Long currencyId = item.get("currencyId").asLong();

            // Calculate totalPrice = basePrice * (1 + markup)
            BigDecimal totalPrice = MathUtil.formatNumber(basePrice.multiply(BigDecimal.ONE.add(markup)), CurrencyHardCode.getCodeFromId(currencyId));

            ServiceChargeMarkup serviceChargeMarkup = ServiceChargeMarkup.builder()
                    .serviceChargeId(serviceChargeId)
                    .basePrice(basePrice)
                    .markup(markup)
                    .currencyId(currencyId)
                    .totalPrice(totalPrice)
                    .build();

            serviceChargeMarkupList.add(serviceChargeMarkup);
        }
        return serviceChargeMarkupList;
    }

    public List<CargoChargePrice> getCargoChargePriceList() {
        if (cargoChargePrice == null || cargoChargePrice.isEmpty()) {
            return List.of();
        }
        ArrayNode inputArray = (ArrayNode) cargoChargePrice;
        List<CargoChargePrice> cargoChargePriceList = new ArrayList<>();

        for (JsonNode item : inputArray) {
            Long containerTypeId = item.get("containerTypeId").asLong();
            BigDecimal basePrice = item.get("basePrice").decimalValue();
            BigDecimal markup = item.get("markup").decimalValue();
            Long currencyId = item.get("currencyId").asLong();

            // Calculate totalPrice = basePrice * (1 + markup)
            BigDecimal totalPrice = MathUtil.formatNumber(basePrice.multiply(BigDecimal.ONE.add(markup)), CurrencyHardCode.getCodeFromId(currencyId));


            CargoChargePrice cargoChargePrice = CargoChargePrice.builder()
                    .containerTypeId(containerTypeId)
                    .basePrice(basePrice)
                    .markup(markup)
                    .totalPrice(totalPrice)
                    .currencyId(currencyId)
                    .build();

            cargoChargePriceList.add(cargoChargePrice);
        }
        return cargoChargePriceList;
    }
}
