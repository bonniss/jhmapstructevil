package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.PaymentViViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.PaymentViVi}.
 */
public interface PaymentViViService {
    /**
     * Save a paymentViVi.
     *
     * @param paymentViViDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentViViDTO save(PaymentViViDTO paymentViViDTO);

    /**
     * Updates a paymentViVi.
     *
     * @param paymentViViDTO the entity to update.
     * @return the persisted entity.
     */
    PaymentViViDTO update(PaymentViViDTO paymentViViDTO);

    /**
     * Partially updates a paymentViVi.
     *
     * @param paymentViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentViViDTO> partialUpdate(PaymentViViDTO paymentViViDTO);

    /**
     * Get all the paymentViVis.
     *
     * @return the list of entities.
     */
    List<PaymentViViDTO> findAll();

    /**
     * Get the "id" paymentViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentViViDTO> findOne(Long id);

    /**
     * Delete the "id" paymentViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
