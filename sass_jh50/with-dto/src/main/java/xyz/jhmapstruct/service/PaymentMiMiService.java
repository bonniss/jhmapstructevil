package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.PaymentMiMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.PaymentMiMi}.
 */
public interface PaymentMiMiService {
    /**
     * Save a paymentMiMi.
     *
     * @param paymentMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    PaymentMiMiDTO save(PaymentMiMiDTO paymentMiMiDTO);

    /**
     * Updates a paymentMiMi.
     *
     * @param paymentMiMiDTO the entity to update.
     * @return the persisted entity.
     */
    PaymentMiMiDTO update(PaymentMiMiDTO paymentMiMiDTO);

    /**
     * Partially updates a paymentMiMi.
     *
     * @param paymentMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PaymentMiMiDTO> partialUpdate(PaymentMiMiDTO paymentMiMiDTO);

    /**
     * Get all the paymentMiMis.
     *
     * @return the list of entities.
     */
    List<PaymentMiMiDTO> findAll();

    /**
     * Get the "id" paymentMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PaymentMiMiDTO> findOne(Long id);

    /**
     * Delete the "id" paymentMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
