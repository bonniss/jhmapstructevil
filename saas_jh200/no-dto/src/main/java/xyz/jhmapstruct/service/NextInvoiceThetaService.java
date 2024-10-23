package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceTheta;
import xyz.jhmapstruct.repository.NextInvoiceThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceTheta}.
 */
@Service
@Transactional
public class NextInvoiceThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceThetaService.class);

    private final NextInvoiceThetaRepository nextInvoiceThetaRepository;

    public NextInvoiceThetaService(NextInvoiceThetaRepository nextInvoiceThetaRepository) {
        this.nextInvoiceThetaRepository = nextInvoiceThetaRepository;
    }

    /**
     * Save a nextInvoiceTheta.
     *
     * @param nextInvoiceTheta the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceTheta save(NextInvoiceTheta nextInvoiceTheta) {
        LOG.debug("Request to save NextInvoiceTheta : {}", nextInvoiceTheta);
        return nextInvoiceThetaRepository.save(nextInvoiceTheta);
    }

    /**
     * Update a nextInvoiceTheta.
     *
     * @param nextInvoiceTheta the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceTheta update(NextInvoiceTheta nextInvoiceTheta) {
        LOG.debug("Request to update NextInvoiceTheta : {}", nextInvoiceTheta);
        return nextInvoiceThetaRepository.save(nextInvoiceTheta);
    }

    /**
     * Partially update a nextInvoiceTheta.
     *
     * @param nextInvoiceTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceTheta> partialUpdate(NextInvoiceTheta nextInvoiceTheta) {
        LOG.debug("Request to partially update NextInvoiceTheta : {}", nextInvoiceTheta);

        return nextInvoiceThetaRepository
            .findById(nextInvoiceTheta.getId())
            .map(existingNextInvoiceTheta -> {
                if (nextInvoiceTheta.getInvoiceNumber() != null) {
                    existingNextInvoiceTheta.setInvoiceNumber(nextInvoiceTheta.getInvoiceNumber());
                }
                if (nextInvoiceTheta.getIssueDate() != null) {
                    existingNextInvoiceTheta.setIssueDate(nextInvoiceTheta.getIssueDate());
                }
                if (nextInvoiceTheta.getDueDate() != null) {
                    existingNextInvoiceTheta.setDueDate(nextInvoiceTheta.getDueDate());
                }
                if (nextInvoiceTheta.getAmount() != null) {
                    existingNextInvoiceTheta.setAmount(nextInvoiceTheta.getAmount());
                }

                return existingNextInvoiceTheta;
            })
            .map(nextInvoiceThetaRepository::save);
    }

    /**
     * Get one nextInvoiceTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceTheta> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceTheta : {}", id);
        return nextInvoiceThetaRepository.findById(id);
    }

    /**
     * Delete the nextInvoiceTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceTheta : {}", id);
        nextInvoiceThetaRepository.deleteById(id);
    }
}
