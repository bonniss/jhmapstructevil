package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceGamma;
import xyz.jhmapstruct.repository.NextInvoiceGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceGamma}.
 */
@Service
@Transactional
public class NextInvoiceGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceGammaService.class);

    private final NextInvoiceGammaRepository nextInvoiceGammaRepository;

    public NextInvoiceGammaService(NextInvoiceGammaRepository nextInvoiceGammaRepository) {
        this.nextInvoiceGammaRepository = nextInvoiceGammaRepository;
    }

    /**
     * Save a nextInvoiceGamma.
     *
     * @param nextInvoiceGamma the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceGamma save(NextInvoiceGamma nextInvoiceGamma) {
        LOG.debug("Request to save NextInvoiceGamma : {}", nextInvoiceGamma);
        return nextInvoiceGammaRepository.save(nextInvoiceGamma);
    }

    /**
     * Update a nextInvoiceGamma.
     *
     * @param nextInvoiceGamma the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceGamma update(NextInvoiceGamma nextInvoiceGamma) {
        LOG.debug("Request to update NextInvoiceGamma : {}", nextInvoiceGamma);
        return nextInvoiceGammaRepository.save(nextInvoiceGamma);
    }

    /**
     * Partially update a nextInvoiceGamma.
     *
     * @param nextInvoiceGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceGamma> partialUpdate(NextInvoiceGamma nextInvoiceGamma) {
        LOG.debug("Request to partially update NextInvoiceGamma : {}", nextInvoiceGamma);

        return nextInvoiceGammaRepository
            .findById(nextInvoiceGamma.getId())
            .map(existingNextInvoiceGamma -> {
                if (nextInvoiceGamma.getInvoiceNumber() != null) {
                    existingNextInvoiceGamma.setInvoiceNumber(nextInvoiceGamma.getInvoiceNumber());
                }
                if (nextInvoiceGamma.getIssueDate() != null) {
                    existingNextInvoiceGamma.setIssueDate(nextInvoiceGamma.getIssueDate());
                }
                if (nextInvoiceGamma.getDueDate() != null) {
                    existingNextInvoiceGamma.setDueDate(nextInvoiceGamma.getDueDate());
                }
                if (nextInvoiceGamma.getAmount() != null) {
                    existingNextInvoiceGamma.setAmount(nextInvoiceGamma.getAmount());
                }

                return existingNextInvoiceGamma;
            })
            .map(nextInvoiceGammaRepository::save);
    }

    /**
     * Get one nextInvoiceGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceGamma> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceGamma : {}", id);
        return nextInvoiceGammaRepository.findById(id);
    }

    /**
     * Delete the nextInvoiceGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceGamma : {}", id);
        nextInvoiceGammaRepository.deleteById(id);
    }
}
