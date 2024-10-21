package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.InvoiceMiMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.InvoiceMiMi}.
 */
public interface InvoiceMiMiService {
    /**
     * Save a invoiceMiMi.
     *
     * @param invoiceMiMi the entity to save.
     * @return the persisted entity.
     */
    InvoiceMiMi save(InvoiceMiMi invoiceMiMi);

    /**
     * Updates a invoiceMiMi.
     *
     * @param invoiceMiMi the entity to update.
     * @return the persisted entity.
     */
    InvoiceMiMi update(InvoiceMiMi invoiceMiMi);

    /**
     * Partially updates a invoiceMiMi.
     *
     * @param invoiceMiMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceMiMi> partialUpdate(InvoiceMiMi invoiceMiMi);

    /**
     * Get all the invoiceMiMis.
     *
     * @return the list of entities.
     */
    List<InvoiceMiMi> findAll();

    /**
     * Get the "id" invoiceMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceMiMi> findOne(Long id);

    /**
     * Delete the "id" invoiceMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
