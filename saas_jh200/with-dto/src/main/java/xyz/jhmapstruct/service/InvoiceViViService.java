package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceViVi;
import xyz.jhmapstruct.repository.InvoiceViViRepository;
import xyz.jhmapstruct.service.dto.InvoiceViViDTO;
import xyz.jhmapstruct.service.mapper.InvoiceViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceViVi}.
 */
@Service
@Transactional
public class InvoiceViViService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViViService.class);

    private final InvoiceViViRepository invoiceViViRepository;

    private final InvoiceViViMapper invoiceViViMapper;

    public InvoiceViViService(InvoiceViViRepository invoiceViViRepository, InvoiceViViMapper invoiceViViMapper) {
        this.invoiceViViRepository = invoiceViViRepository;
        this.invoiceViViMapper = invoiceViViMapper;
    }

    /**
     * Save a invoiceViVi.
     *
     * @param invoiceViViDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceViViDTO save(InvoiceViViDTO invoiceViViDTO) {
        LOG.debug("Request to save InvoiceViVi : {}", invoiceViViDTO);
        InvoiceViVi invoiceViVi = invoiceViViMapper.toEntity(invoiceViViDTO);
        invoiceViVi = invoiceViViRepository.save(invoiceViVi);
        return invoiceViViMapper.toDto(invoiceViVi);
    }

    /**
     * Update a invoiceViVi.
     *
     * @param invoiceViViDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceViViDTO update(InvoiceViViDTO invoiceViViDTO) {
        LOG.debug("Request to update InvoiceViVi : {}", invoiceViViDTO);
        InvoiceViVi invoiceViVi = invoiceViViMapper.toEntity(invoiceViViDTO);
        invoiceViVi = invoiceViViRepository.save(invoiceViVi);
        return invoiceViViMapper.toDto(invoiceViVi);
    }

    /**
     * Partially update a invoiceViVi.
     *
     * @param invoiceViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceViViDTO> partialUpdate(InvoiceViViDTO invoiceViViDTO) {
        LOG.debug("Request to partially update InvoiceViVi : {}", invoiceViViDTO);

        return invoiceViViRepository
            .findById(invoiceViViDTO.getId())
            .map(existingInvoiceViVi -> {
                invoiceViViMapper.partialUpdate(existingInvoiceViVi, invoiceViViDTO);

                return existingInvoiceViVi;
            })
            .map(invoiceViViRepository::save)
            .map(invoiceViViMapper::toDto);
    }

    /**
     * Get one invoiceViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceViViDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceViVi : {}", id);
        return invoiceViViRepository.findById(id).map(invoiceViViMapper::toDto);
    }

    /**
     * Delete the invoiceViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceViVi : {}", id);
        invoiceViViRepository.deleteById(id);
    }
}
