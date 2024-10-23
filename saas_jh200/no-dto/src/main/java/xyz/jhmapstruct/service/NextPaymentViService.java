package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentVi;
import xyz.jhmapstruct.repository.NextPaymentViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentVi}.
 */
@Service
@Transactional
public class NextPaymentViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentViService.class);

    private final NextPaymentViRepository nextPaymentViRepository;

    public NextPaymentViService(NextPaymentViRepository nextPaymentViRepository) {
        this.nextPaymentViRepository = nextPaymentViRepository;
    }

    /**
     * Save a nextPaymentVi.
     *
     * @param nextPaymentVi the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentVi save(NextPaymentVi nextPaymentVi) {
        LOG.debug("Request to save NextPaymentVi : {}", nextPaymentVi);
        return nextPaymentViRepository.save(nextPaymentVi);
    }

    /**
     * Update a nextPaymentVi.
     *
     * @param nextPaymentVi the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentVi update(NextPaymentVi nextPaymentVi) {
        LOG.debug("Request to update NextPaymentVi : {}", nextPaymentVi);
        return nextPaymentViRepository.save(nextPaymentVi);
    }

    /**
     * Partially update a nextPaymentVi.
     *
     * @param nextPaymentVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentVi> partialUpdate(NextPaymentVi nextPaymentVi) {
        LOG.debug("Request to partially update NextPaymentVi : {}", nextPaymentVi);

        return nextPaymentViRepository
            .findById(nextPaymentVi.getId())
            .map(existingNextPaymentVi -> {
                if (nextPaymentVi.getAmount() != null) {
                    existingNextPaymentVi.setAmount(nextPaymentVi.getAmount());
                }
                if (nextPaymentVi.getPaymentDate() != null) {
                    existingNextPaymentVi.setPaymentDate(nextPaymentVi.getPaymentDate());
                }
                if (nextPaymentVi.getPaymentMethod() != null) {
                    existingNextPaymentVi.setPaymentMethod(nextPaymentVi.getPaymentMethod());
                }

                return existingNextPaymentVi;
            })
            .map(nextPaymentViRepository::save);
    }

    /**
     * Get one nextPaymentVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentVi> findOne(Long id) {
        LOG.debug("Request to get NextPaymentVi : {}", id);
        return nextPaymentViRepository.findById(id);
    }

    /**
     * Delete the nextPaymentVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentVi : {}", id);
        nextPaymentViRepository.deleteById(id);
    }
}
