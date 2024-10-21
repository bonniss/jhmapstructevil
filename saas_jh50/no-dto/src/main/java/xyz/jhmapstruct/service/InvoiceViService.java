package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.InvoiceVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.InvoiceVi}.
 */
public interface InvoiceViService {
    /**
     * Save a invoiceVi.
     *
     * @param invoiceVi the entity to save.
     * @return the persisted entity.
     */
    InvoiceVi save(InvoiceVi invoiceVi);

    /**
     * Updates a invoiceVi.
     *
     * @param invoiceVi the entity to update.
     * @return the persisted entity.
     */
    InvoiceVi update(InvoiceVi invoiceVi);

    /**
     * Partially updates a invoiceVi.
     *
     * @param invoiceVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceVi> partialUpdate(InvoiceVi invoiceVi);

    /**
     * Get all the invoiceVis.
     *
     * @return the list of entities.
     */
    List<InvoiceVi> findAll();

    /**
     * Get the "id" invoiceVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceVi> findOne(Long id);

    /**
     * Delete the "id" invoiceVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
