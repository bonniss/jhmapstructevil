package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceBeta;
import xyz.jhmapstruct.repository.InvoiceBetaRepository;
import xyz.jhmapstruct.service.dto.InvoiceBetaDTO;
import xyz.jhmapstruct.service.mapper.InvoiceBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceBeta}.
 */
@Service
@Transactional
public class InvoiceBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceBetaService.class);

    private final InvoiceBetaRepository invoiceBetaRepository;

    private final InvoiceBetaMapper invoiceBetaMapper;

    public InvoiceBetaService(InvoiceBetaRepository invoiceBetaRepository, InvoiceBetaMapper invoiceBetaMapper) {
        this.invoiceBetaRepository = invoiceBetaRepository;
        this.invoiceBetaMapper = invoiceBetaMapper;
    }

    /**
     * Save a invoiceBeta.
     *
     * @param invoiceBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceBetaDTO save(InvoiceBetaDTO invoiceBetaDTO) {
        LOG.debug("Request to save InvoiceBeta : {}", invoiceBetaDTO);
        InvoiceBeta invoiceBeta = invoiceBetaMapper.toEntity(invoiceBetaDTO);
        invoiceBeta = invoiceBetaRepository.save(invoiceBeta);
        return invoiceBetaMapper.toDto(invoiceBeta);
    }

    /**
     * Update a invoiceBeta.
     *
     * @param invoiceBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceBetaDTO update(InvoiceBetaDTO invoiceBetaDTO) {
        LOG.debug("Request to update InvoiceBeta : {}", invoiceBetaDTO);
        InvoiceBeta invoiceBeta = invoiceBetaMapper.toEntity(invoiceBetaDTO);
        invoiceBeta = invoiceBetaRepository.save(invoiceBeta);
        return invoiceBetaMapper.toDto(invoiceBeta);
    }

    /**
     * Partially update a invoiceBeta.
     *
     * @param invoiceBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceBetaDTO> partialUpdate(InvoiceBetaDTO invoiceBetaDTO) {
        LOG.debug("Request to partially update InvoiceBeta : {}", invoiceBetaDTO);

        return invoiceBetaRepository
            .findById(invoiceBetaDTO.getId())
            .map(existingInvoiceBeta -> {
                invoiceBetaMapper.partialUpdate(existingInvoiceBeta, invoiceBetaDTO);

                return existingInvoiceBeta;
            })
            .map(invoiceBetaRepository::save)
            .map(invoiceBetaMapper::toDto);
    }

    /**
     * Get one invoiceBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceBetaDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceBeta : {}", id);
        return invoiceBetaRepository.findById(id).map(invoiceBetaMapper::toDto);
    }

    /**
     * Delete the invoiceBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceBeta : {}", id);
        invoiceBetaRepository.deleteById(id);
    }
}
