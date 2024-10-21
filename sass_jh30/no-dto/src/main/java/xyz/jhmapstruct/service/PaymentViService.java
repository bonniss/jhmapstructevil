package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.PaymentVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.PaymentVi}.
 */
public interface PaymentViService {
    /**
     * Save a paymentVi.
     *
     * @param paymentVi the entity to save.
     * @return the persisted entity.
     */
    PaymentVi save(PaymentVi paymentVi);

    /**
     * Updates a paymentVi.
     *
     * @param paymentVi the entity to update.
     * @return the persisted entity.
     */
    PaymentVi update(PaymentVi paymentVi);

    /**
     * Partially updates a paymentVi.
     *
     * @param paymentVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentVi> partialUpdate(PaymentVi paymentVi);

    /**
     * Get all the paymentVis.
     *
     * @return the list of entities.
     */
    List<PaymentVi> findAll();

    /**
     * Get the "id" paymentVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentVi> findOne(Long id);

    /**
     * Delete the "id" paymentVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
