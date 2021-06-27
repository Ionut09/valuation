package co.arbelos.gtm.valuation.service;

import co.arbelos.gtm.valuation.config.audit.AuditEventConverter;
import co.arbelos.gtm.valuation.repository.PersistenceAuditEventRepository;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 */
@Service
@Transactional
public class AuditEventService {

    private final PersistenceAuditEventRepository persistenceAuditEventRepository;

    private final AuditEventConverter auditEventConverter;

    public AuditEventService(
        PersistenceAuditEventRepository persistenceAuditEventRepository,
        AuditEventConverter auditEventConverter) {

        this.persistenceAuditEventRepository = persistenceAuditEventRepository;
        this.auditEventConverter = auditEventConverter;
    }

    public Page<AuditEvent> findAll(Pageable pageable) {
        return persistenceAuditEventRepository.findAll(pageable)
            .map(auditEventConverter::convertToAuditEvent);
    }

    public Page<AuditEvent> findByDates(Instant fromDate, Instant toDate, Pageable pageable) {
        return persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate, pageable)
            .map(auditEventConverter::convertToAuditEvent);
    }

    public Page<AuditEvent> findByDatesAndUser(String user, Instant fromDate, Instant toDate, Pageable pageable) {
        return persistenceAuditEventRepository.findAllByPrincipalAndAuditEventDateBetween(user, fromDate, toDate, pageable)
            .map(auditEventConverter::convertToAuditEvent);
    }

    public List<AuditEvent> findAllByDates(String user, Instant fromDate, Instant toDate) {
        return persistenceAuditEventRepository.findAllByPrincipalAndAuditEventDateBetween(user, fromDate, toDate)
            .stream()
            .map(auditEventConverter::convertToAuditEvent)
            .collect(Collectors.toList());
    }

    public List<AuditEvent> findAllByDates(Instant fromDate, Instant toDate) {
        return persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate)
            .stream()
            .map(auditEventConverter::convertToAuditEvent)
            .collect(Collectors.toList());
    }

    public Optional<AuditEvent> find(Long id) {
        return Optional.ofNullable(persistenceAuditEventRepository.findById(id))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(auditEventConverter::convertToAuditEvent);
    }
}
