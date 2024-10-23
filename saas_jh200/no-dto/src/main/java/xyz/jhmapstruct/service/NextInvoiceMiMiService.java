package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceMiMi;
import xyz.jhmapstruct.repository.NextInvoiceMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceMiMi}.
 */
@Service
@Transactional
public class NextInvoiceMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceMiMiService.class);

    private final NextInvoiceMiMiRepository nextInvoiceMiMiRepository;

    public NextInvoiceMiMiService(NextInvoiceMiMiRepository nextInvoiceMiMiRepository) {
        this.nextInvoiceMiMiRepository = nextInvoiceMiMiRepository;
    }

    /**
     * Save a nextInvoiceMiMi.
     *
     * @param nextInvoiceMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceMiMi save(NextInvoiceMiMi nextInvoiceMiMi) {
        LOG.debug("Request to save NextInvoiceMiMi : {}", nextInvoiceMiMi);
        return nextInvoiceMiMiRepository.save(nextInvoiceMiMi);
    }

    /**
     * Update a nextInvoiceMiMi.
     *
     * @param nextInvoiceMiMi the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceMiMi update(NextInvoiceMiMi nextInvoiceMiMi) {
        LOG.debug("Request to update NextInvoiceMiMi : {}", nextInvoiceMiMi);
        return nextInvoiceMiMiRepository.save(nextInvoiceMiMi);
    }

    /**
     * Partially update a nextInvoiceMiMi.
     *
     * @param nextInvoiceMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceMiMi> partialUpdate(NextInvoiceMiMi nextInvoiceMiMi) {
        LOG.debug("Request to partially update NextInvoiceMiMi : {}", nextInvoiceMiMi);

        return nextInvoiceMiMiRepository
            .findById(nextInvoiceMiMi.getId())
            .map(existingNextInvoiceMiMi -> {
                if (nextInvoiceMiMi.getInvoiceNumber() != null) {
                    existingNextInvoiceMiMi.setInvoiceNumber(nextInvoiceMiMi.getInvoiceNumber());
                }
                if (nextInvoiceMiMi.getIssueDate() != null) {
                    existingNextInvoiceMiMi.setIssueDate(nextInvoiceMiMi.getIssueDate());
                }
                if (nextInvoiceMiMi.getDueDate() != null) {
                    existingNextInvoiceMiMi.setDueDate(nextInvoiceMiMi.getDueDate());
                }
                if (nextInvoiceMiMi.getAmount() != null) {
                    existingNextInvoiceMiMi.setAmount(nextInvoiceMiMi.getAmount());
                }

                return existingNextInvoiceMiMi;
            })
            .map(nextInvoiceMiMiRepository::save);
    }

    /**
     * Get one nextInvoiceMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceMiMi> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceMiMi : {}", id);
        return nextInvoiceMiMiRepository.findById(id);
    }

    /**
     * Delete the nextInvoiceMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceMiMi : {}", id);
        nextInvoiceMiMiRepository.deleteById(id);
    }
}
