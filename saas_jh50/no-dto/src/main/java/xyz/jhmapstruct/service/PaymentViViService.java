package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.PaymentViVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.PaymentViVi}.
 */
public interface PaymentViViService {
    /**
     * Save a paymentViVi.
     *
     * @param paymentViVi the entity to save.
     * @return the persisted entity.
     */
    PaymentViVi save(PaymentViVi paymentViVi);

    /**
     * Updates a paymentViVi.
     *
     * @param paymentViVi the entity to update.
     * @return the persisted entity.
     */
    PaymentViVi update(PaymentViVi paymentViVi);

    /**
     * Partially updates a paymentViVi.
     *
     * @param paymentViVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentViVi> partialUpdate(PaymentViVi paymentViVi);

    /**
     * Get all the paymentViVis.
     *
     * @return the list of entities.
     */
    List<PaymentViVi> findAll();

    /**
     * Get the "id" paymentViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentViVi> findOne(Long id);

    /**
     * Delete the "id" paymentViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
