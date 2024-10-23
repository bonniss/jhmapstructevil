package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceVi;
import xyz.jhmapstruct.repository.InvoiceViRepository;
import xyz.jhmapstruct.service.dto.InvoiceViDTO;
import xyz.jhmapstruct.service.mapper.InvoiceViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceVi}.
 */
@Service
@Transactional
public class InvoiceViService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViService.class);

    private final InvoiceViRepository invoiceViRepository;

    private final InvoiceViMapper invoiceViMapper;

    public InvoiceViService(InvoiceViRepository invoiceViRepository, InvoiceViMapper invoiceViMapper) {
        this.invoiceViRepository = invoiceViRepository;
        this.invoiceViMapper = invoiceViMapper;
    }

    /**
     * Save a invoiceVi.
     *
     * @param invoiceViDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceViDTO save(InvoiceViDTO invoiceViDTO) {
        LOG.debug("Request to save InvoiceVi : {}", invoiceViDTO);
        InvoiceVi invoiceVi = invoiceViMapper.toEntity(invoiceViDTO);
        invoiceVi = invoiceViRepository.save(invoiceVi);
        return invoiceViMapper.toDto(invoiceVi);
    }

    /**
     * Update a invoiceVi.
     *
     * @param invoiceViDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceViDTO update(InvoiceViDTO invoiceViDTO) {
        LOG.debug("Request to update InvoiceVi : {}", invoiceViDTO);
        InvoiceVi invoiceVi = invoiceViMapper.toEntity(invoiceViDTO);
        invoiceVi = invoiceViRepository.save(invoiceVi);
        return invoiceViMapper.toDto(invoiceVi);
    }

    /**
     * Partially update a invoiceVi.
     *
     * @param invoiceViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceViDTO> partialUpdate(InvoiceViDTO invoiceViDTO) {
        LOG.debug("Request to partially update InvoiceVi : {}", invoiceViDTO);

        return invoiceViRepository
            .findById(invoiceViDTO.getId())
            .map(existingInvoiceVi -> {
                invoiceViMapper.partialUpdate(existingInvoiceVi, invoiceViDTO);

                return existingInvoiceVi;
            })
            .map(invoiceViRepository::save)
            .map(invoiceViMapper::toDto);
    }

    /**
     * Get one invoiceVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceViDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceVi : {}", id);
        return invoiceViRepository.findById(id).map(invoiceViMapper::toDto);
    }

    /**
     * Delete the invoiceVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceVi : {}", id);
        invoiceViRepository.deleteById(id);
    }
}
