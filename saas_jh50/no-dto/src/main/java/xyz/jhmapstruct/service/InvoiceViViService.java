package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.InvoiceViVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.InvoiceViVi}.
 */
public interface InvoiceViViService {
    /**
     * Save a invoiceViVi.
     *
     * @param invoiceViVi the entity to save.
     * @return the persisted entity.
     */
    InvoiceViVi save(InvoiceViVi invoiceViVi);

    /**
     * Updates a invoiceViVi.
     *
     * @param invoiceViVi the entity to update.
     * @return the persisted entity.
     */
    InvoiceViVi update(InvoiceViVi invoiceViVi);

    /**
     * Partially updates a invoiceViVi.
     *
     * @param invoiceViVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceViVi> partialUpdate(InvoiceViVi invoiceViVi);

    /**
     * Get all the invoiceViVis.
     *
     * @return the list of entities.
     */
    List<InvoiceViVi> findAll();

    /**
     * Get the "id" invoiceViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceViVi> findOne(Long id);

    /**
     * Delete the "id" invoiceViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
