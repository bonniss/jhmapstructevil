package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceViVi;
import xyz.jhmapstruct.repository.NextInvoiceViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceViVi}.
 */
@Service
@Transactional
public class NextInvoiceViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceViViService.class);

    private final NextInvoiceViViRepository nextInvoiceViViRepository;

    public NextInvoiceViViService(NextInvoiceViViRepository nextInvoiceViViRepository) {
        this.nextInvoiceViViRepository = nextInvoiceViViRepository;
    }

    /**
     * Save a nextInvoiceViVi.
     *
     * @param nextInvoiceViVi the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceViVi save(NextInvoiceViVi nextInvoiceViVi) {
        LOG.debug("Request to save NextInvoiceViVi : {}", nextInvoiceViVi);
        return nextInvoiceViViRepository.save(nextInvoiceViVi);
    }

    /**
     * Update a nextInvoiceViVi.
     *
     * @param nextInvoiceViVi the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceViVi update(NextInvoiceViVi nextInvoiceViVi) {
        LOG.debug("Request to update NextInvoiceViVi : {}", nextInvoiceViVi);
        return nextInvoiceViViRepository.save(nextInvoiceViVi);
    }

    /**
     * Partially update a nextInvoiceViVi.
     *
     * @param nextInvoiceViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceViVi> partialUpdate(NextInvoiceViVi nextInvoiceViVi) {
        LOG.debug("Request to partially update NextInvoiceViVi : {}", nextInvoiceViVi);

        return nextInvoiceViViRepository
            .findById(nextInvoiceViVi.getId())
            .map(existingNextInvoiceViVi -> {
                if (nextInvoiceViVi.getInvoiceNumber() != null) {
                    existingNextInvoiceViVi.setInvoiceNumber(nextInvoiceViVi.getInvoiceNumber());
                }
                if (nextInvoiceViVi.getIssueDate() != null) {
                    existingNextInvoiceViVi.setIssueDate(nextInvoiceViVi.getIssueDate());
                }
                if (nextInvoiceViVi.getDueDate() != null) {
                    existingNextInvoiceViVi.setDueDate(nextInvoiceViVi.getDueDate());
                }
                if (nextInvoiceViVi.getAmount() != null) {
                    existingNextInvoiceViVi.setAmount(nextInvoiceViVi.getAmount());
                }

                return existingNextInvoiceViVi;
            })
            .map(nextInvoiceViViRepository::save);
    }

    /**
     * Get one nextInvoiceViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceViVi> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceViVi : {}", id);
        return nextInvoiceViViRepository.findById(id);
    }

    /**
     * Delete the nextInvoiceViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceViVi : {}", id);
        nextInvoiceViViRepository.deleteById(id);
    }
}
