package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.InvoiceViViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.InvoiceViVi}.
 */
public interface InvoiceViViService {
    /**
     * Save a invoiceViVi.
     *
     * @param invoiceViViDTO the entity to save.
     * @return the persisted entity.
     */
    InvoiceViViDTO save(InvoiceViViDTO invoiceViViDTO);

    /**
     * Updates a invoiceViVi.
     *
     * @param invoiceViViDTO the entity to update.
     * @return the persisted entity.
     */
    InvoiceViViDTO update(InvoiceViViDTO invoiceViViDTO);

    /**
     * Partially updates a invoiceViVi.
     *
     * @param invoiceViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceViViDTO> partialUpdate(InvoiceViViDTO invoiceViViDTO);

    /**
     * Get all the invoiceViVis.
     *
     * @return the list of entities.
     */
    List<InvoiceViViDTO> findAll();

    /**
     * Get the "id" invoiceViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceViViDTO> findOne(Long id);

    /**
     * Delete the "id" invoiceViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
