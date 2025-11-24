package com.lcp.shipment.service.impl;

import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.common.enumeration.QuoteStatus;
import com.lcp.common.enumeration.ShipmentStatusEnum;
import com.lcp.exception.ApiException;
import com.lcp.quote.entity.Quote;
import com.lcp.quote.repository.QuoteRepository;
import com.lcp.shipment.dto.*;
import com.lcp.shipment.entity.Shipment;
import com.lcp.shipment.entity.ShipmentStatus;
import com.lcp.shipment.helper.ShipmentHelper;
import com.lcp.shipment.mapper.ShipmentMapper;
import com.lcp.shipment.mapper.ShipmentStatusMapper;
import com.lcp.shipment.repository.ShipmentRepository;
import com.lcp.shipment.repository.ShipmentStatusRepository;
import com.lcp.shipment.service.ShipmentService;
import com.lcp.util.MapUtil;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final ShipmentHelper shipmentHelper;
    private final ShipmentStatusRepository shipmentStatusRepository;
    private final QuoteRepository quoteRepository;

    @Transactional
    @Override
    public ShipmentResponseDto create(ShipmentCreateDto createDto) {
        ShipmentStatusCreateDto shipmentStatusCreateDto = new ShipmentStatusCreateDto();
        shipmentStatusCreateDto.setShipmentStatus(ShipmentStatusEnum.DRAFT);
        ShipmentStatus shipmentStatus = ShipmentStatusMapper.createEntity(shipmentStatusCreateDto);
        shipmentStatusRepository.save(shipmentStatus);

        Shipment shipment = ShipmentMapper.createEntity(createDto);
        shipment.setShipmentStatusId(shipmentStatus.getId());
        shipment.setCode(shipmentHelper.genShipmentCode());
        shipmentRepository.save(shipment);
        PersistentUtil.flushAndClear();
        return detail(shipment.getId());
    }

    @Transactional
    @Override
    public ShipmentResponseDto update(Long id, ShipmentUpdateDto updateDto) {
        Shipment shipment = get(id);
        MapUtil.copyProperties(updateDto, shipment);
        shipmentRepository.save(shipment);
        PersistentUtil.flushAndClear();
        return detail(shipment.getId());
    }

    @Transactional
    @Override
    public ShipmentResponseDto updateStatus(Long id, ShipmentStatusCreateDto shipmentStatusCreateDto) {
        Shipment shipment = get(id);
        ShipmentStatus shipmentStatus = shipmentStatusRepository.getOne(shipment.getShipmentStatusId());

        Quote quote = quoteRepository.findById(shipment.getQuoteId()).orElseThrow(
                () -> new ApiException(new ApiMessageBase("Quote not found"))
        );

        if (shipmentStatusCreateDto.getShipmentStatus() == ShipmentStatusEnum.PRE_CARGO
                && shipment.getShipmentStatus().getShipmentStatus() == ShipmentStatusEnum.DRAFT) {
            quote.setQuoteStatus(QuoteStatus.BOOKED);
        }

        if (shipment.getShipmentStatus().getShipmentStatus() == ShipmentStatusEnum.DESTINATION
                && shipmentStatusCreateDto.getShipmentStatus() == ShipmentStatusEnum.DELIVERED) {
            shipmentStatusCreateDto.setAta(LocalDate.now());
            shipment.setIsOnTime(!shipmentStatusCreateDto.getAta().isAfter(shipment.getEta()));
        }
        MapUtil.copyProperties(shipmentStatusCreateDto, shipmentStatus);
        shipmentStatusRepository.save(shipmentStatus);
        quoteRepository.save(quote);
        PersistentUtil.flushAndClear();
        return detail(shipment.getId());
    }

    @Override
    public ShipmentResponseDto detail(Long id) {
        Shipment shipment = get(id);
        ShipmentResponseDto responseDto = ShipmentMapper.createResponse(shipment);

        // // Calculate prices if quote exists
        // if (responseDto.getQuote() != null && responseDto.getQuote().getCargoVolume() != null) {
        //     CargoVolume cargoVolume = shipment.getQuote().getCargoVolume();

        //     // Get profit rate
        //     BigDecimal profitRate = shipment.getQuote().getProfitRate() != null ?
        //             BigDecimal.ONE.add(shipment.getQuote().getProfitRate().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)) : BigDecimal.ONE;

        //     if (Boolean.TRUE.equals(cargoVolume.getIsFCL()) && cargoVolume.getFclRate() != null) {
        //         FclRate rate = cargoVolume.getFclRate();
        //         String currencyCode = rate.getCurrency() != null ? rate.getCurrency().getCode() : "VND";

        //         // Calculate container prices
        //         if (cargoVolume.getTotalCont20dc() > 0) {
        //             ContainerPriceDto price20dc = new ContainerPriceDto();
        //             price20dc.setQuantity(cargoVolume.getTotalCont20dc());
        //             price20dc.setPricePerUnit(rate.getRate20dc().setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price20dc.setTotalPrice(rate.getRate20dc().multiply(BigDecimal.valueOf(cargoVolume.getTotalCont20dc())).multiply(profitRate).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price20dc.setCurrencyCode(currencyCode);
        //             responseDto.getQuote().getCargoVolume().setCont20dcPrice(price20dc);
        //             if (Objects.requireNonNull(UserDetailsCustom.getCurrentUser()).getIsClient()) {
        //                 price20dc.setPricePerUnit(null);
        //             }
        //         }

        //         if (cargoVolume.getTotalCont40dc() > 0) {
        //             ContainerPriceDto price40dc = new ContainerPriceDto();
        //             price40dc.setQuantity(cargoVolume.getTotalCont40dc());
        //             price40dc.setPricePerUnit(rate.getRate40dc().setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price40dc.setTotalPrice(rate.getRate40dc().multiply(BigDecimal.valueOf(cargoVolume.getTotalCont40dc())).multiply(profitRate).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price40dc.setCurrencyCode(currencyCode);
        //             responseDto.getQuote().getCargoVolume().setCont40dcPrice(price40dc);
        //             if (Objects.requireNonNull(UserDetailsCustom.getCurrentUser()).getIsClient()) {
        //                 price40dc.setPricePerUnit(null);
        //             }
        //         }

        //         if (cargoVolume.getTotalCont20rf() > 0) {
        //             ContainerPriceDto price20rf = new ContainerPriceDto();
        //             price20rf.setQuantity(cargoVolume.getTotalCont20rf());
        //             price20rf.setPricePerUnit(rate.getRate20rf().setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price20rf.setTotalPrice(rate.getRate20rf().multiply(BigDecimal.valueOf(cargoVolume.getTotalCont20rf())).multiply(profitRate).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price20rf.setCurrencyCode(currencyCode);
        //             responseDto.getQuote().getCargoVolume().setCont20rfPrice(price20rf);
        //             if (Objects.requireNonNull(UserDetailsCustom.getCurrentUser()).getIsClient()) {
        //                 price20rf.setPricePerUnit(null);
        //             }
        //         }

        //         if (cargoVolume.getTotalCont40rf() > 0) {
        //             ContainerPriceDto price40rf = new ContainerPriceDto();
        //             price40rf.setQuantity(cargoVolume.getTotalCont40rf());
        //             price40rf.setPricePerUnit(rate.getRate40rf().setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price40rf.setTotalPrice(rate.getRate40rf().multiply(BigDecimal.valueOf(cargoVolume.getTotalCont40rf())).multiply(profitRate).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price40rf.setCurrencyCode(currencyCode);
        //             responseDto.getQuote().getCargoVolume().setCont40rfPrice(price40rf);
        //             if (Objects.requireNonNull(UserDetailsCustom.getCurrentUser()).getIsClient()) {
        //                 price40rf.setPricePerUnit(null);
        //             }
        //         }

        //         if (cargoVolume.getTotalCont20hc() > 0) {
        //             ContainerPriceDto price20hc = new ContainerPriceDto();
        //             price20hc.setQuantity(cargoVolume.getTotalCont20hc());
        //             price20hc.setPricePerUnit(rate.getRate20hc().setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price20hc.setTotalPrice(rate.getRate20hc().multiply(BigDecimal.valueOf(cargoVolume.getTotalCont20hc())).multiply(profitRate).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price20hc.setCurrencyCode(currencyCode);
        //             responseDto.getQuote().getCargoVolume().setCont20hcPrice(price20hc);
        //             if (Objects.requireNonNull(UserDetailsCustom.getCurrentUser()).getIsClient()) {
        //                 price20hc.setPricePerUnit(null);
        //             }
        //         }

        //         if (cargoVolume.getTotalCont40hc() > 0) {
        //             ContainerPriceDto price40hc = new ContainerPriceDto();
        //             price40hc.setQuantity(cargoVolume.getTotalCont40hc());
        //             price40hc.setPricePerUnit(rate.getRate40hc().setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price40hc.setTotalPrice(rate.getRate40hc().multiply(BigDecimal.valueOf(cargoVolume.getTotalCont40hc())).multiply(profitRate).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price40hc.setCurrencyCode(currencyCode);
        //             responseDto.getQuote().getCargoVolume().setCont40hcPrice(price40hc);
        //             if (Objects.requireNonNull(UserDetailsCustom.getCurrentUser()).getIsClient()) {
        //                 price40hc.setPricePerUnit(null);
        //             }
        //         }

        //         if (cargoVolume.getTotalCont45hc() > 0) {
        //             ContainerPriceDto price45hc = new ContainerPriceDto();
        //             price45hc.setQuantity(cargoVolume.getTotalCont45hc());
        //             price45hc.setPricePerUnit(rate.getRate45hc().setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price45hc.setTotalPrice(rate.getRate45hc().multiply(BigDecimal.valueOf(cargoVolume.getTotalCont45hc())).multiply(profitRate).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //             price45hc.setCurrencyCode(currencyCode);
        //             responseDto.getQuote().getCargoVolume().setCont45hcPrice(price45hc);
        //             if (Objects.requireNonNull(UserDetailsCustom.getCurrentUser()).getIsClient()) {
        //                 price45hc.setPricePerUnit(null);
        //             }
        //         }
        //     } else if (cargoVolume.getLclRate() != null) {
        //         LclRate rate = cargoVolume.getLclRate();
        //         String currencyCode = rate.getCurrency() != null ? rate.getCurrency().getCode() : "USD";

        //         // Calculate LCL price
        //         double volumeMetricWeight = cargoVolume.getTotalVolume() * rate.getVolumetricFactor().doubleValue();
        //         double chargeableWeight = Math.max(cargoVolume.getTotalWeight(), volumeMetricWeight);

        //         double weightPrice = chargeableWeight * rate.getPricePerWeight().doubleValue();
        //         double volumePrice = cargoVolume.getTotalVolume() * rate.getPricePerVolume().doubleValue();

        //         double basePrice = Math.max(weightPrice, volumePrice);
        //         basePrice = Math.max(basePrice, rate.getMinimumCharge().doubleValue());
        //         responseDto.getQuote().getCargoVolume().setTotalLclPrice(BigDecimal.valueOf(basePrice).multiply(profitRate).setScale(2, RoundingMode.HALF_UP).doubleValue());
        //         responseDto.getQuote().getCargoVolume().setLclCurrencyCode(currencyCode);
        //         responseDto.getQuote().getCargoVolume().setBaseLclPrice(basePrice);
        //         if (Objects.requireNonNull(UserDetailsCustom.getCurrentUser()).getIsClient()) {
        //             responseDto.getQuote().getCargoVolume().setBaseLclPrice(null);
        //         }
        //     }

        //     // Calculate total price including additional charges
        //     double totalPrice = 0.0;
        //     CargoVolumeResponseDto cargoVolumeDto = responseDto.getQuote().getCargoVolume();
        //     if (cargoVolumeDto.getCont20dcPrice() != null)
        //         totalPrice += cargoVolumeDto.getCont20dcPrice().getTotalPrice();
        //     if (cargoVolumeDto.getCont40dcPrice() != null)
        //         totalPrice += cargoVolumeDto.getCont40dcPrice().getTotalPrice();
        //     if (cargoVolumeDto.getCont20rfPrice() != null)
        //         totalPrice += cargoVolumeDto.getCont20rfPrice().getTotalPrice();
        //     if (cargoVolumeDto.getCont40rfPrice() != null)
        //         totalPrice += cargoVolumeDto.getCont40rfPrice().getTotalPrice();
        //     if (cargoVolumeDto.getCont20hcPrice() != null)
        //         totalPrice += cargoVolumeDto.getCont20hcPrice().getTotalPrice();
        //     if (cargoVolumeDto.getCont40hcPrice() != null)
        //         totalPrice += cargoVolumeDto.getCont40hcPrice().getTotalPrice();
        //     if (cargoVolumeDto.getCont45hcPrice() != null)
        //         totalPrice += cargoVolumeDto.getCont45hcPrice().getTotalPrice();
        //     if (cargoVolumeDto.getTotalLclPrice() != null) totalPrice += cargoVolumeDto.getTotalLclPrice();

        //     if (responseDto.getQuote().getQuotePriceDetails() != null) {
        //         totalPrice += responseDto.getQuote().getQuotePriceDetails().stream()
        //                 .mapToDouble(detail -> detail.getTotalPrice().doubleValue())
        //                 .sum();
        //     }
        //     responseDto.getQuote().setTotalPrice(totalPrice);
        // }

        // Hide sensitive information for clients
//        if (Objects.requireNonNull(UserDetailsCustom.getCurrentUser()).getIsClient()) {
//            if (responseDto.getQuote() != null) {
//                responseDto.getQuote().setProvider(null);
//                responseDto.getQuote().setProviderId(null);
//                responseDto.getQuote().setProfitRate(null);

//                if (responseDto.getQuote().getCargoVolume() != null) {
//                    responseDto.getQuote().getCargoVolume().setFclRate(null);
//                    responseDto.getQuote().getCargoVolume().setLclRate(null);
//                    //set fclrateid and lclrateid to null
//                    responseDto.getQuote().getCargoVolume().setFclRateId(null);
//                    responseDto.getQuote().getCargoVolume().setLclRateId(null);
//                }

//                if (responseDto.getQuote().getQuotePriceDetails() != null) {
//                    responseDto.getQuote().getQuotePriceDetails().forEach(detail -> {
//                        detail.setProvider(null);
//                        detail.setProviderId(null);
//                        detail.setBasePrice(null);
//                    });
//                }
//            }
//        }

        return responseDto;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Shipment shipment = get(id);
        shipmentRepository.delete(shipment);
    }

    @Override
    public PageResponse<ShipmentResponseDto> list(ShipmentListRequest request) {
        Page<Shipment> shipments = shipmentRepository.list(request);
        return PageResponse.buildPageDtoResponse(
                shipments,
                ShipmentMapper::createResponse
        );
    }

    private Shipment get(Long id) {
        Optional<Shipment> shipmentOptional = shipmentRepository.findById(id);
        if (shipmentOptional.isEmpty()) {
            throw new ApiException(new ApiMessageBase("Shipment not found"));
        }
        return shipmentOptional.get();
    }
}
