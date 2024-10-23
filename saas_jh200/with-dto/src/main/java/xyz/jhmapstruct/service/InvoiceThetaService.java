package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceTheta;
import xyz.jhmapstruct.repository.InvoiceThetaRepository;
import xyz.jhmapstruct.service.dto.InvoiceThetaDTO;
import xyz.jhmapstruct.service.mapper.InvoiceThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceTheta}.
 */
@Service
@Transactional
public class InvoiceThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceThetaService.class);

    private final InvoiceThetaRepository invoiceThetaRepository;

    private final InvoiceThetaMapper invoiceThetaMapper;

    public InvoiceThetaService(InvoiceThetaRepository invoiceThetaRepository, InvoiceThetaMapper invoiceThetaMapper) {
        this.invoiceThetaRepository = invoiceThetaRepository;
        this.invoiceThetaMapper = invoiceThetaMapper;
    }

    /**
     * Save a invoiceTheta.
     *
     * @param invoiceThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceThetaDTO save(InvoiceThetaDTO invoiceThetaDTO) {
        LOG.debug("Request to save InvoiceTheta : {}", invoiceThetaDTO);
        InvoiceTheta invoiceTheta = invoiceThetaMapper.toEntity(invoiceThetaDTO);
        invoiceTheta = invoiceThetaRepository.save(invoiceTheta);
        return invoiceThetaMapper.toDto(invoiceTheta);
    }

    /**
     * Update a invoiceTheta.
     *
     * @param invoiceThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceThetaDTO update(InvoiceThetaDTO invoiceThetaDTO) {
        LOG.debug("Request to update InvoiceTheta : {}", invoiceThetaDTO);
        InvoiceTheta invoiceTheta = invoiceThetaMapper.toEntity(invoiceThetaDTO);
        invoiceTheta = invoiceThetaRepository.save(invoiceTheta);
        return invoiceThetaMapper.toDto(invoiceTheta);
    }

    /**
     * Partially update a invoiceTheta.
     *
     * @param invoiceThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceThetaDTO> partialUpdate(InvoiceThetaDTO invoiceThetaDTO) {
        LOG.debug("Request to partially update InvoiceTheta : {}", invoiceThetaDTO);

        return invoiceThetaRepository
            .findById(invoiceThetaDTO.getId())
            .map(existingInvoiceTheta -> {
                invoiceThetaMapper.partialUpdate(existingInvoiceTheta, invoiceThetaDTO);

                return existingInvoiceTheta;
            })
            .map(invoiceThetaRepository::save)
            .map(invoiceThetaMapper::toDto);
    }

    /**
     * Get one invoiceTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceThetaDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceTheta : {}", id);
        return invoiceThetaRepository.findById(id).map(invoiceThetaMapper::toDto);
    }

    /**
     * Delete the invoiceTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceTheta : {}", id);
        invoiceThetaRepository.deleteById(id);
    }
}
