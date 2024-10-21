package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.PaymentMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.PaymentMi}.
 */
public interface PaymentMiService {
    /**
     * Save a paymentMi.
     *
     * @param paymentMiDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentMiDTO save(PaymentMiDTO paymentMiDTO);

    /**
     * Updates a paymentMi.
     *
     * @param paymentMiDTO the entity to update.
     * @return the persisted entity.
     */
    PaymentMiDTO update(PaymentMiDTO paymentMiDTO);

    /**
     * Partially updates a paymentMi.
     *
     * @param paymentMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentMiDTO> partialUpdate(PaymentMiDTO paymentMiDTO);

    /**
     * Get all the paymentMis.
     *
     * @return the list of entities.
     */
    List<PaymentMiDTO> findAll();

    /**
     * Get the "id" paymentMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentMiDTO> findOne(Long id);

    /**
     * Delete the "id" paymentMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
