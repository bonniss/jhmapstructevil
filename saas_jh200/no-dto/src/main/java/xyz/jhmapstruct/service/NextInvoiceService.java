package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoice;
import xyz.jhmapstruct.repository.NextInvoiceRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoice}.
 */
@Service
@Transactional
public class NextInvoiceService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceService.class);

    private final NextInvoiceRepository nextInvoiceRepository;

    public NextInvoiceService(NextInvoiceRepository nextInvoiceRepository) {
        this.nextInvoiceRepository = nextInvoiceRepository;
    }

    /**
     * Save a nextInvoice.
     *
     * @param nextInvoice the entity to save.
     * @return the persisted entity.
     */
    public NextInvoice save(NextInvoice nextInvoice) {
        LOG.debug("Request to save NextInvoice : {}", nextInvoice);
        return nextInvoiceRepository.save(nextInvoice);
    }

    /**
     * Update a nextInvoice.
     *
     * @param nextInvoice the entity to save.
     * @return the persisted entity.
     */
    public NextInvoice update(NextInvoice nextInvoice) {
        LOG.debug("Request to update NextInvoice : {}", nextInvoice);
        return nextInvoiceRepository.save(nextInvoice);
    }

    /**
     * Partially update a nextInvoice.
     *
     * @param nextInvoice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoice> partialUpdate(NextInvoice nextInvoice) {
        LOG.debug("Request to partially update NextInvoice : {}", nextInvoice);

        return nextInvoiceRepository
            .findById(nextInvoice.getId())
            .map(existingNextInvoice -> {
                if (nextInvoice.getInvoiceNumber() != null) {
                    existingNextInvoice.setInvoiceNumber(nextInvoice.getInvoiceNumber());
                }
                if (nextInvoice.getIssueDate() != null) {
                    existingNextInvoice.setIssueDate(nextInvoice.getIssueDate());
                }
                if (nextInvoice.getDueDate() != null) {
                    existingNextInvoice.setDueDate(nextInvoice.getDueDate());
                }
                if (nextInvoice.getAmount() != null) {
                    existingNextInvoice.setAmount(nextInvoice.getAmount());
                }

                return existingNextInvoice;
            })
            .map(nextInvoiceRepository::save);
    }

    /**
     * Get one nextInvoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoice> findOne(Long id) {
        LOG.debug("Request to get NextInvoice : {}", id);
        return nextInvoiceRepository.findById(id);
    }

    /**
     * Delete the nextInvoice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoice : {}", id);
        nextInvoiceRepository.deleteById(id);
    }
}
