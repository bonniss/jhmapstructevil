package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceGamma;
import xyz.jhmapstruct.repository.InvoiceGammaRepository;
import xyz.jhmapstruct.service.dto.InvoiceGammaDTO;
import xyz.jhmapstruct.service.mapper.InvoiceGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceGamma}.
 */
@Service
@Transactional
public class InvoiceGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceGammaService.class);

    private final InvoiceGammaRepository invoiceGammaRepository;

    private final InvoiceGammaMapper invoiceGammaMapper;

    public InvoiceGammaService(InvoiceGammaRepository invoiceGammaRepository, InvoiceGammaMapper invoiceGammaMapper) {
        this.invoiceGammaRepository = invoiceGammaRepository;
        this.invoiceGammaMapper = invoiceGammaMapper;
    }

    /**
     * Save a invoiceGamma.
     *
     * @param invoiceGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceGammaDTO save(InvoiceGammaDTO invoiceGammaDTO) {
        LOG.debug("Request to save InvoiceGamma : {}", invoiceGammaDTO);
        InvoiceGamma invoiceGamma = invoiceGammaMapper.toEntity(invoiceGammaDTO);
        invoiceGamma = invoiceGammaRepository.save(invoiceGamma);
        return invoiceGammaMapper.toDto(invoiceGamma);
    }

    /**
     * Update a invoiceGamma.
     *
     * @param invoiceGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceGammaDTO update(InvoiceGammaDTO invoiceGammaDTO) {
        LOG.debug("Request to update InvoiceGamma : {}", invoiceGammaDTO);
        InvoiceGamma invoiceGamma = invoiceGammaMapper.toEntity(invoiceGammaDTO);
        invoiceGamma = invoiceGammaRepository.save(invoiceGamma);
        return invoiceGammaMapper.toDto(invoiceGamma);
    }

    /**
     * Partially update a invoiceGamma.
     *
     * @param invoiceGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceGammaDTO> partialUpdate(InvoiceGammaDTO invoiceGammaDTO) {
        LOG.debug("Request to partially update InvoiceGamma : {}", invoiceGammaDTO);

        return invoiceGammaRepository
            .findById(invoiceGammaDTO.getId())
            .map(existingInvoiceGamma -> {
                invoiceGammaMapper.partialUpdate(existingInvoiceGamma, invoiceGammaDTO);

                return existingInvoiceGamma;
            })
            .map(invoiceGammaRepository::save)
            .map(invoiceGammaMapper::toDto);
    }

    /**
     * Get one invoiceGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceGammaDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceGamma : {}", id);
        return invoiceGammaRepository.findById(id).map(invoiceGammaMapper::toDto);
    }

    /**
     * Delete the invoiceGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceGamma : {}", id);
        invoiceGammaRepository.deleteById(id);
    }
}
