package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import xyz.jhmapstruct.service.dto.InvoiceDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.Invoice}.
 */
public interface InvoiceService {
    /**
     * Save a invoice.
     *
     * @param invoiceDTO the entity to save.
     * @return the persisted entity.
     */
    InvoiceDTO save(InvoiceDTO invoiceDTO);

    /**
     * Updates a invoice.
     *
     * @param invoiceDTO the entity to update.
     * @return the persisted entity.
     */
    InvoiceDTO update(InvoiceDTO invoiceDTO);

    /**
     * Partially updates a invoice.
     *
     * @param invoiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InvoiceDTO> partialUpdate(InvoiceDTO invoiceDTO);

    /**
     * Get all the invoices.
     *
     * @return the list of entities.
     */
    List<InvoiceDTO> findAll();

    /**
     * Get the "id" invoice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InvoiceDTO> findOne(Long id);

    /**
     * Delete the "id" invoice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
