package com.lcp.dashboard.service.impl;

import com.lcp.common.enumeration.InvoiceStatus;
import com.lcp.common.enumeration.QuoteStatus;
import com.lcp.common.enumeration.ShipmentStatusEnum;
import com.lcp.dashboard.dto.DashboardAdminResponse;
import com.lcp.dashboard.dto.DashboardClientResponse;
import com.lcp.dashboard.service.DashboardService;
import com.lcp.invoice.repository.InvoiceRepository;
import com.lcp.quote.entity.Quote;
import com.lcp.quote.repository.QuoteRepository;
import com.lcp.shipment.entity.Shipment;
import com.lcp.shipment.entity.ShipmentStatus;
import com.lcp.shipment.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DashboardServiceImpl implements DashboardService {
    private final QuoteRepository quoteRepository;
    private final ShipmentRepository shipmentRepository;
    private final InvoiceRepository invoiceRepository;
    private final EntityManager entityManager;

    @Override
    public DashboardClientResponse getClientDashboard() {
        return null;
    }

    @Override
    public DashboardAdminResponse getAdminDashboard() {

        DashboardAdminResponse response = new DashboardAdminResponse();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<?> root = query.from(com.lcp.shipment.entity.Shipment.class);
        query.select(cb.count(root));
        query.where(cb.and(
                cb.notEqual(root.get("shipmentStatus").get("shipmentStatus"), ShipmentStatusEnum.DELIVERED),
                cb.notEqual(root.get("shipmentStatus").get("shipmentStatus"), ShipmentStatusEnum.DRAFT)
        ));
        response.setActiveShipment(entityManager.createQuery(query).getSingleResult());

        // Count shipments delivered (DELIVERED status)
        query = cb.createQuery(Long.class);
        root = query.from(com.lcp.shipment.entity.Shipment.class);
        query.select(cb.count(root));
        query.where(cb.equal(root.get("shipmentStatus").get("shipmentStatus"), ShipmentStatusEnum.DELIVERED));
        response.setDeliveredShipment(entityManager.createQuery(query).getSingleResult());

        // Count quotes overdue (OVERDUE status)
        query = cb.createQuery(Long.class);
        root = query.from(com.lcp.quote.entity.Quote.class);
        query.select(cb.count(root));
        query.where(cb.equal(root.get("quoteStatus"), QuoteStatus.OVERDUE));
        response.setOverdueQuote(entityManager.createQuery(query).getSingleResult());

        // Count quotes accepted (ACCEPTED status)
        query = cb.createQuery(Long.class);
        root = query.from(com.lcp.quote.entity.Quote.class);
        query.select(cb.count(root));
        query.where(cb.equal(root.get("quoteStatus"), QuoteStatus.ACCEPTED));
        response.setQuoteAccepted(entityManager.createQuery(query).getSingleResult());

        // Count quotes booked (BOOKED status)
        query = cb.createQuery(Long.class);
        root = query.from(com.lcp.quote.entity.Quote.class);
        query.select(cb.count(root));
        query.where(cb.equal(root.get("quoteStatus"), QuoteStatus.BOOKED));
        response.setQuoteBooked(entityManager.createQuery(query).getSingleResult());

        // Count overdue invoices
        query = cb.createQuery(Long.class);
        root = query.from(com.lcp.invoice.entity.Invoice.class);
        query.select(cb.count(root));
        query.where(
                cb.or(
                        cb.and(
                                cb.lessThan(root.get("dueDate"), LocalDate.now()),
                                cb.equal(root.get("paymentStatus"), InvoiceStatus.UNPAID)
                        ),
                        cb.equal(root.get("paymentStatus"), InvoiceStatus.OVERDUE)
                )
        );
        response.setOverdueInvoice(entityManager.createQuery(query).getSingleResult());

        // Count paid invoices
        query = cb.createQuery(Long.class);
        root = query.from(com.lcp.invoice.entity.Invoice.class);
        query.select(cb.count(root));
        query.where(cb.equal(root.get("paymentStatus"), InvoiceStatus.PAID));
        response.setPaidInvoice(entityManager.createQuery(query).getSingleResult());

        CriteriaBuilder cbQuote = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> queryQuote = cbQuote.createQuery(Object[].class); // 👈 change type
        Root<Quote> rootQuote = queryQuote.from(Quote.class);

        queryQuote.multiselect(
                rootQuote.get("providerId"),
                cb.count(rootQuote)
        ).where(
                cb.equal(rootQuote.get("quoteStatus"), QuoteStatus.BOOKED)
        ).groupBy(
                rootQuote.get("providerId")
        );

        List<Object[]> resultList = entityManager.createQuery(queryQuote).getResultList();

        Map<Long, Long> providerQuoteCount = resultList.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],  // providerId
                        row -> (Long) row[1]   // count
                ));

        // Set your response
        response.setProviderQuoteCount(providerQuoteCount);

        CriteriaBuilder cbQuoteClient = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> queryQuoteClient = cbQuoteClient.createQuery(Object[].class);
        Root<Quote> rootQuoteClient = queryQuoteClient.from(Quote.class);

        queryQuoteClient.multiselect(
                rootQuoteClient.get("clientId"),
                cb.count(rootQuoteClient)
        ).where(
                cb.equal(rootQuoteClient.get("quoteStatus"), QuoteStatus.BOOKED)
        ).groupBy(
                rootQuoteClient.get("clientId")
        );

        List<Object[]> resultListQuoteClient = entityManager.createQuery(queryQuoteClient).getResultList();

        Map<Long, Long> clientQuoteCount = resultListQuoteClient.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],  // clientId
                        row -> (Long) row[1]   // count
                ));

// Set your response
        response.setClientQuoteCount(clientQuoteCount);

        CriteriaBuilder cbClientShipment = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> queryClientShipment = cbClientShipment.createQuery(Object[].class);

        Root<Shipment> shipment = queryClientShipment.from(Shipment.class);
        Join<Object, Quote> quote = shipment.join("quote");
        Join<Object, ShipmentStatus> status = shipment.join("shipmentStatus");

        queryClientShipment.multiselect(
                quote.get("clientId"),
                cb.count(shipment.get("id"))
        ).where(
                cb.equal(status.get("shipmentStatus"), ShipmentStatusEnum.DELIVERED)
        ).groupBy(
                quote.get("clientId")
        );

        List<Object[]> resultsClientShipment = entityManager.createQuery(queryClientShipment).getResultList();

        Map<Long, Long> clientShipmentCount = resultsClientShipment.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0], // clientId
                        row -> (Long) row[1]  // shipment count
                ));
        response.setClientShipmentCount(clientShipmentCount);

        CriteriaBuilder cbProviderShipment = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> queryProviderShipment = cbProviderShipment.createQuery(Object[].class);
        Root<Shipment> shipmentProvider = queryProviderShipment.from(Shipment.class);

        // Join to related entities
        Join<Object, Quote> quoteJoin = shipmentProvider.join("quote");
        Join<Object, ShipmentStatus> statusJoin = shipmentProvider.join("shipmentStatus");

        // Build the query
        queryProviderShipment.multiselect(
                quoteJoin.get("providerId"),           // Select providerId
                cb.count(shipmentProvider.get("id"))           // Count shipments
        ).where(
                cb.equal(statusJoin.get("shipmentStatus"), ShipmentStatusEnum.DELIVERED)
        ).groupBy(
                quoteJoin.get("providerId")
        );

        // Run the query
        List<Object[]> results = entityManager.createQuery(queryProviderShipment).getResultList();

        // Convert to Map<Long, Long>
        Map<Long, Long> providerShipmentCount = results.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0], // providerId
                        row -> (Long) row[1]  // count
                ));
        response.setProviderShipmentCount(providerShipmentCount);
        return response;
    }
}
