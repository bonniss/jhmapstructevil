package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.PaymentMiMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.PaymentMiMi}.
 */
public interface PaymentMiMiService {
    /**
     * Save a paymentMiMi.
     *
     * @param paymentMiMi the entity to save.
     * @return the persisted entity.
     */
    PaymentMiMi save(PaymentMiMi paymentMiMi);

    /**
     * Updates a paymentMiMi.
     *
     * @param paymentMiMi the entity to update.
     * @return the persisted entity.
     */
    PaymentMiMi update(PaymentMiMi paymentMiMi);

    /**
     * Partially updates a paymentMiMi.
     *
     * @param paymentMiMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentMiMi> partialUpdate(PaymentMiMi paymentMiMi);

    /**
     * Get all the paymentMiMis.
     *
     * @return the list of entities.
     */
    List<PaymentMiMi> findAll();

    /**
     * Get the "id" paymentMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentMiMi> findOne(Long id);

    /**
     * Delete the "id" paymentMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
