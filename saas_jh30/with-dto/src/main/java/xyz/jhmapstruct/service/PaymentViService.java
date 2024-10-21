package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.PaymentViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.PaymentVi}.
 */
public interface PaymentViService {
    /**
     * Save a paymentVi.
     *
     * @param paymentViDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentViDTO save(PaymentViDTO paymentViDTO);

    /**
     * Updates a paymentVi.
     *
     * @param paymentViDTO the entity to update.
     * @return the persisted entity.
     */
    PaymentViDTO update(PaymentViDTO paymentViDTO);

    /**
     * Partially updates a paymentVi.
     *
     * @param paymentViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentViDTO> partialUpdate(PaymentViDTO paymentViDTO);

    /**
     * Get all the paymentVis.
     *
     * @return the list of entities.
     */
    List<PaymentViDTO> findAll();

    /**
     * Get the "id" paymentVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentViDTO> findOne(Long id);

    /**
     * Delete the "id" paymentVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
