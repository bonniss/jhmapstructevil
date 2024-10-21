package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.InvoiceViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.InvoiceVi}.
 */
public interface InvoiceViService {
    /**
     * Save a invoiceVi.
     *
     * @param invoiceViDTO the entity to save.
     * @return the persisted entity.
     */
    InvoiceViDTO save(InvoiceViDTO invoiceViDTO);

    /**
     * Updates a invoiceVi.
     *
     * @param invoiceViDTO the entity to update.
     * @return the persisted entity.
     */
    InvoiceViDTO update(InvoiceViDTO invoiceViDTO);

    /**
     * Partially updates a invoiceVi.
     *
     * @param invoiceViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceViDTO> partialUpdate(InvoiceViDTO invoiceViDTO);

    /**
     * Get all the invoiceVis.
     *
     * @return the list of entities.
     */
    List<InvoiceViDTO> findAll();

    /**
     * Get the "id" invoiceVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceViDTO> findOne(Long id);

    /**
     * Delete the "id" invoiceVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
