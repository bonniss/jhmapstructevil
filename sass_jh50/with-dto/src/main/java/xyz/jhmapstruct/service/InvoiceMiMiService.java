package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.InvoiceMiMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.InvoiceMiMi}.
 */
public interface InvoiceMiMiService {
    /**
     * Save a invoiceMiMi.
     *
     * @param invoiceMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    InvoiceMiMiDTO save(InvoiceMiMiDTO invoiceMiMiDTO);

    /**
     * Updates a invoiceMiMi.
     *
     * @param invoiceMiMiDTO the entity to update.
     * @return the persisted entity.
     */
    InvoiceMiMiDTO update(InvoiceMiMiDTO invoiceMiMiDTO);

    /**
     * Partially updates a invoiceMiMi.
     *
     * @param invoiceMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceMiMiDTO> partialUpdate(InvoiceMiMiDTO invoiceMiMiDTO);

    /**
     * Get all the invoiceMiMis.
     *
     * @return the list of entities.
     */
    List<InvoiceMiMiDTO> findAll();

    /**
     * Get the "id" invoiceMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceMiMiDTO> findOne(Long id);

    /**
     * Delete the "id" invoiceMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
