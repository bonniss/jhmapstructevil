package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceMi;
import xyz.jhmapstruct.repository.NextInvoiceMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceMi}.
 */
@Service
@Transactional
public class NextInvoiceMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceMiService.class);

    private final NextInvoiceMiRepository nextInvoiceMiRepository;

    public NextInvoiceMiService(NextInvoiceMiRepository nextInvoiceMiRepository) {
        this.nextInvoiceMiRepository = nextInvoiceMiRepository;
    }

    /**
     * Save a nextInvoiceMi.
     *
     * @param nextInvoiceMi the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceMi save(NextInvoiceMi nextInvoiceMi) {
        LOG.debug("Request to save NextInvoiceMi : {}", nextInvoiceMi);
        return nextInvoiceMiRepository.save(nextInvoiceMi);
    }

    /**
     * Update a nextInvoiceMi.
     *
     * @param nextInvoiceMi the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceMi update(NextInvoiceMi nextInvoiceMi) {
        LOG.debug("Request to update NextInvoiceMi : {}", nextInvoiceMi);
        return nextInvoiceMiRepository.save(nextInvoiceMi);
    }

    /**
     * Partially update a nextInvoiceMi.
     *
     * @param nextInvoiceMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceMi> partialUpdate(NextInvoiceMi nextInvoiceMi) {
        LOG.debug("Request to partially update NextInvoiceMi : {}", nextInvoiceMi);

        return nextInvoiceMiRepository
            .findById(nextInvoiceMi.getId())
            .map(existingNextInvoiceMi -> {
                if (nextInvoiceMi.getInvoiceNumber() != null) {
                    existingNextInvoiceMi.setInvoiceNumber(nextInvoiceMi.getInvoiceNumber());
                }
                if (nextInvoiceMi.getIssueDate() != null) {
                    existingNextInvoiceMi.setIssueDate(nextInvoiceMi.getIssueDate());
                }
                if (nextInvoiceMi.getDueDate() != null) {
                    existingNextInvoiceMi.setDueDate(nextInvoiceMi.getDueDate());
                }
                if (nextInvoiceMi.getAmount() != null) {
                    existingNextInvoiceMi.setAmount(nextInvoiceMi.getAmount());
                }

                return existingNextInvoiceMi;
            })
            .map(nextInvoiceMiRepository::save);
    }

    /**
     * Get one nextInvoiceMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceMi> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceMi : {}", id);
        return nextInvoiceMiRepository.findById(id);
    }

    /**
     * Delete the nextInvoiceMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceMi : {}", id);
        nextInvoiceMiRepository.deleteById(id);
    }
}
