package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.PaymentMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.PaymentMi}.
 */
public interface PaymentMiService {
    /**
     * Save a paymentMi.
     *
     * @param paymentMi the entity to save.
     * @return the persisted entity.
     */
    PaymentMi save(PaymentMi paymentMi);

    /**
     * Updates a paymentMi.
     *
     * @param paymentMi the entity to update.
     * @return the persisted entity.
     */
    PaymentMi update(PaymentMi paymentMi);

    /**
     * Partially updates a paymentMi.
     *
     * @param paymentMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentMi> partialUpdate(PaymentMi paymentMi);

    /**
     * Get all the paymentMis.
     *
     * @return the list of entities.
     */
    List<PaymentMi> findAll();

    /**
     * Get the "id" paymentMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentMi> findOne(Long id);

    /**
     * Delete the "id" paymentMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
