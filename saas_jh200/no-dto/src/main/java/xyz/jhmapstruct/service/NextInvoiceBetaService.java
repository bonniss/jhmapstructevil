package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceBeta;
import xyz.jhmapstruct.repository.NextInvoiceBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceBeta}.
 */
@Service
@Transactional
public class NextInvoiceBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceBetaService.class);

    private final NextInvoiceBetaRepository nextInvoiceBetaRepository;

    public NextInvoiceBetaService(NextInvoiceBetaRepository nextInvoiceBetaRepository) {
        this.nextInvoiceBetaRepository = nextInvoiceBetaRepository;
    }

    /**
     * Save a nextInvoiceBeta.
     *
     * @param nextInvoiceBeta the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceBeta save(NextInvoiceBeta nextInvoiceBeta) {
        LOG.debug("Request to save NextInvoiceBeta : {}", nextInvoiceBeta);
        return nextInvoiceBetaRepository.save(nextInvoiceBeta);
    }

    /**
     * Update a nextInvoiceBeta.
     *
     * @param nextInvoiceBeta the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceBeta update(NextInvoiceBeta nextInvoiceBeta) {
        LOG.debug("Request to update NextInvoiceBeta : {}", nextInvoiceBeta);
        return nextInvoiceBetaRepository.save(nextInvoiceBeta);
    }

    /**
     * Partially update a nextInvoiceBeta.
     *
     * @param nextInvoiceBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceBeta> partialUpdate(NextInvoiceBeta nextInvoiceBeta) {
        LOG.debug("Request to partially update NextInvoiceBeta : {}", nextInvoiceBeta);

        return nextInvoiceBetaRepository
            .findById(nextInvoiceBeta.getId())
            .map(existingNextInvoiceBeta -> {
                if (nextInvoiceBeta.getInvoiceNumber() != null) {
                    existingNextInvoiceBeta.setInvoiceNumber(nextInvoiceBeta.getInvoiceNumber());
                }
                if (nextInvoiceBeta.getIssueDate() != null) {
                    existingNextInvoiceBeta.setIssueDate(nextInvoiceBeta.getIssueDate());
                }
                if (nextInvoiceBeta.getDueDate() != null) {
                    existingNextInvoiceBeta.setDueDate(nextInvoiceBeta.getDueDate());
                }
                if (nextInvoiceBeta.getAmount() != null) {
                    existingNextInvoiceBeta.setAmount(nextInvoiceBeta.getAmount());
                }

                return existingNextInvoiceBeta;
            })
            .map(nextInvoiceBetaRepository::save);
    }

    /**
     * Get one nextInvoiceBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceBeta> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceBeta : {}", id);
        return nextInvoiceBetaRepository.findById(id);
    }

    /**
     * Delete the nextInvoiceBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceBeta : {}", id);
        nextInvoiceBetaRepository.deleteById(id);
    }
}
