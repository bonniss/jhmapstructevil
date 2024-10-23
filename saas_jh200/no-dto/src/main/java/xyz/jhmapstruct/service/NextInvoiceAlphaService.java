package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceAlpha;
import xyz.jhmapstruct.repository.NextInvoiceAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceAlpha}.
 */
@Service
@Transactional
public class NextInvoiceAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceAlphaService.class);

    private final NextInvoiceAlphaRepository nextInvoiceAlphaRepository;

    public NextInvoiceAlphaService(NextInvoiceAlphaRepository nextInvoiceAlphaRepository) {
        this.nextInvoiceAlphaRepository = nextInvoiceAlphaRepository;
    }

    /**
     * Save a nextInvoiceAlpha.
     *
     * @param nextInvoiceAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceAlpha save(NextInvoiceAlpha nextInvoiceAlpha) {
        LOG.debug("Request to save NextInvoiceAlpha : {}", nextInvoiceAlpha);
        return nextInvoiceAlphaRepository.save(nextInvoiceAlpha);
    }

    /**
     * Update a nextInvoiceAlpha.
     *
     * @param nextInvoiceAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceAlpha update(NextInvoiceAlpha nextInvoiceAlpha) {
        LOG.debug("Request to update NextInvoiceAlpha : {}", nextInvoiceAlpha);
        return nextInvoiceAlphaRepository.save(nextInvoiceAlpha);
    }

    /**
     * Partially update a nextInvoiceAlpha.
     *
     * @param nextInvoiceAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceAlpha> partialUpdate(NextInvoiceAlpha nextInvoiceAlpha) {
        LOG.debug("Request to partially update NextInvoiceAlpha : {}", nextInvoiceAlpha);

        return nextInvoiceAlphaRepository
            .findById(nextInvoiceAlpha.getId())
            .map(existingNextInvoiceAlpha -> {
                if (nextInvoiceAlpha.getInvoiceNumber() != null) {
                    existingNextInvoiceAlpha.setInvoiceNumber(nextInvoiceAlpha.getInvoiceNumber());
                }
                if (nextInvoiceAlpha.getIssueDate() != null) {
                    existingNextInvoiceAlpha.setIssueDate(nextInvoiceAlpha.getIssueDate());
                }
                if (nextInvoiceAlpha.getDueDate() != null) {
                    existingNextInvoiceAlpha.setDueDate(nextInvoiceAlpha.getDueDate());
                }
                if (nextInvoiceAlpha.getAmount() != null) {
                    existingNextInvoiceAlpha.setAmount(nextInvoiceAlpha.getAmount());
                }

                return existingNextInvoiceAlpha;
            })
            .map(nextInvoiceAlphaRepository::save);
    }

    /**
     * Get one nextInvoiceAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceAlpha> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceAlpha : {}", id);
        return nextInvoiceAlphaRepository.findById(id);
    }

    /**
     * Delete the nextInvoiceAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceAlpha : {}", id);
        nextInvoiceAlphaRepository.deleteById(id);
    }
}
