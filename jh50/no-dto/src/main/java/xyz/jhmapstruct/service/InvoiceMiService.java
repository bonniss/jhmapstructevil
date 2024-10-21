package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.domain.InvoiceMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.InvoiceMi}.
 */
public interface InvoiceMiService {
    /**
     * Save a invoiceMi.
     *
     * @param invoiceMi the entity to save.
     * @return the persisted entity.
     */
    InvoiceMi save(InvoiceMi invoiceMi);

    /**
     * Updates a invoiceMi.
     *
     * @param invoiceMi the entity to update.
     * @return the persisted entity.
     */
    InvoiceMi update(InvoiceMi invoiceMi);

    /**
     * Partially updates a invoiceMi.
     *
     * @param invoiceMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceMi> partialUpdate(InvoiceMi invoiceMi);

    /**
     * Get all the invoiceMis.
     *
     * @return the list of entities.
     */
    List<InvoiceMi> findAll();

    /**
     * Get the "id" invoiceMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceMi> findOne(Long id);

    /**
     * Delete the "id" invoiceMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
