package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceSigma;
import xyz.jhmapstruct.repository.NextInvoiceSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceSigma}.
 */
@Service
@Transactional
public class NextInvoiceSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceSigmaService.class);

    private final NextInvoiceSigmaRepository nextInvoiceSigmaRepository;

    public NextInvoiceSigmaService(NextInvoiceSigmaRepository nextInvoiceSigmaRepository) {
        this.nextInvoiceSigmaRepository = nextInvoiceSigmaRepository;
    }

    /**
     * Save a nextInvoiceSigma.
     *
     * @param nextInvoiceSigma the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceSigma save(NextInvoiceSigma nextInvoiceSigma) {
        LOG.debug("Request to save NextInvoiceSigma : {}", nextInvoiceSigma);
        return nextInvoiceSigmaRepository.save(nextInvoiceSigma);
    }

    /**
     * Update a nextInvoiceSigma.
     *
     * @param nextInvoiceSigma the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceSigma update(NextInvoiceSigma nextInvoiceSigma) {
        LOG.debug("Request to update NextInvoiceSigma : {}", nextInvoiceSigma);
        return nextInvoiceSigmaRepository.save(nextInvoiceSigma);
    }

    /**
     * Partially update a nextInvoiceSigma.
     *
     * @param nextInvoiceSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceSigma> partialUpdate(NextInvoiceSigma nextInvoiceSigma) {
        LOG.debug("Request to partially update NextInvoiceSigma : {}", nextInvoiceSigma);

        return nextInvoiceSigmaRepository
            .findById(nextInvoiceSigma.getId())
            .map(existingNextInvoiceSigma -> {
                if (nextInvoiceSigma.getInvoiceNumber() != null) {
                    existingNextInvoiceSigma.setInvoiceNumber(nextInvoiceSigma.getInvoiceNumber());
                }
                if (nextInvoiceSigma.getIssueDate() != null) {
                    existingNextInvoiceSigma.setIssueDate(nextInvoiceSigma.getIssueDate());
                }
                if (nextInvoiceSigma.getDueDate() != null) {
                    existingNextInvoiceSigma.setDueDate(nextInvoiceSigma.getDueDate());
                }
                if (nextInvoiceSigma.getAmount() != null) {
                    existingNextInvoiceSigma.setAmount(nextInvoiceSigma.getAmount());
                }

                return existingNextInvoiceSigma;
            })
            .map(nextInvoiceSigmaRepository::save);
    }

    /**
     * Get one nextInvoiceSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceSigma> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceSigma : {}", id);
        return nextInvoiceSigmaRepository.findById(id);
    }

    /**
     * Delete the nextInvoiceSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceSigma : {}", id);
        nextInvoiceSigmaRepository.deleteById(id);
    }
}
