package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceVi;
import xyz.jhmapstruct.repository.NextInvoiceViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceVi}.
 */
@Service
@Transactional
public class NextInvoiceViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceViService.class);

    private final NextInvoiceViRepository nextInvoiceViRepository;

    public NextInvoiceViService(NextInvoiceViRepository nextInvoiceViRepository) {
        this.nextInvoiceViRepository = nextInvoiceViRepository;
    }

    /**
     * Save a nextInvoiceVi.
     *
     * @param nextInvoiceVi the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceVi save(NextInvoiceVi nextInvoiceVi) {
        LOG.debug("Request to save NextInvoiceVi : {}", nextInvoiceVi);
        return nextInvoiceViRepository.save(nextInvoiceVi);
    }

    /**
     * Update a nextInvoiceVi.
     *
     * @param nextInvoiceVi the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceVi update(NextInvoiceVi nextInvoiceVi) {
        LOG.debug("Request to update NextInvoiceVi : {}", nextInvoiceVi);
        return nextInvoiceViRepository.save(nextInvoiceVi);
    }

    /**
     * Partially update a nextInvoiceVi.
     *
     * @param nextInvoiceVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceVi> partialUpdate(NextInvoiceVi nextInvoiceVi) {
        LOG.debug("Request to partially update NextInvoiceVi : {}", nextInvoiceVi);

        return nextInvoiceViRepository
            .findById(nextInvoiceVi.getId())
            .map(existingNextInvoiceVi -> {
                if (nextInvoiceVi.getInvoiceNumber() != null) {
                    existingNextInvoiceVi.setInvoiceNumber(nextInvoiceVi.getInvoiceNumber());
                }
                if (nextInvoiceVi.getIssueDate() != null) {
                    existingNextInvoiceVi.setIssueDate(nextInvoiceVi.getIssueDate());
                }
                if (nextInvoiceVi.getDueDate() != null) {
                    existingNextInvoiceVi.setDueDate(nextInvoiceVi.getDueDate());
                }
                if (nextInvoiceVi.getAmount() != null) {
                    existingNextInvoiceVi.setAmount(nextInvoiceVi.getAmount());
                }

                return existingNextInvoiceVi;
            })
            .map(nextInvoiceViRepository::save);
    }

    /**
     * Get one nextInvoiceVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceVi> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceVi : {}", id);
        return nextInvoiceViRepository.findById(id);
    }

    /**
     * Delete the nextInvoiceVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceVi : {}", id);
        nextInvoiceViRepository.deleteById(id);
    }
}
