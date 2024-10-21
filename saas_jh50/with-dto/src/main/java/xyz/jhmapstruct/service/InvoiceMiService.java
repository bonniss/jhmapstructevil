package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.InvoiceMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.InvoiceMi}.
 */
public interface InvoiceMiService {
    /**
     * Save a invoiceMi.
     *
     * @param invoiceMiDTO the entity to save.
     * @return the persisted entity.
     */
    InvoiceMiDTO save(InvoiceMiDTO invoiceMiDTO);

    /**
     * Updates a invoiceMi.
     *
     * @param invoiceMiDTO the entity to update.
     * @return the persisted entity.
     */
    InvoiceMiDTO update(InvoiceMiDTO invoiceMiDTO);

    /**
     * Partially updates a invoiceMi.
     *
     * @param invoiceMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceMiDTO> partialUpdate(InvoiceMiDTO invoiceMiDTO);

    /**
     * Get all the invoiceMis.
     *
     * @return the list of entities.
     */
    List<InvoiceMiDTO> findAll();

    /**
     * Get the "id" invoiceMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceMiDTO> findOne(Long id);

    /**
     * Delete the "id" invoiceMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
