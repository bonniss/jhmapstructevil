package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceSigma;
import xyz.jhmapstruct.repository.InvoiceSigmaRepository;
import xyz.jhmapstruct.service.dto.InvoiceSigmaDTO;
import xyz.jhmapstruct.service.mapper.InvoiceSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceSigma}.
 */
@Service
@Transactional
public class InvoiceSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSigmaService.class);

    private final InvoiceSigmaRepository invoiceSigmaRepository;

    private final InvoiceSigmaMapper invoiceSigmaMapper;

    public InvoiceSigmaService(InvoiceSigmaRepository invoiceSigmaRepository, InvoiceSigmaMapper invoiceSigmaMapper) {
        this.invoiceSigmaRepository = invoiceSigmaRepository;
        this.invoiceSigmaMapper = invoiceSigmaMapper;
    }

    /**
     * Save a invoiceSigma.
     *
     * @param invoiceSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceSigmaDTO save(InvoiceSigmaDTO invoiceSigmaDTO) {
        LOG.debug("Request to save InvoiceSigma : {}", invoiceSigmaDTO);
        InvoiceSigma invoiceSigma = invoiceSigmaMapper.toEntity(invoiceSigmaDTO);
        invoiceSigma = invoiceSigmaRepository.save(invoiceSigma);
        return invoiceSigmaMapper.toDto(invoiceSigma);
    }

    /**
     * Update a invoiceSigma.
     *
     * @param invoiceSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceSigmaDTO update(InvoiceSigmaDTO invoiceSigmaDTO) {
        LOG.debug("Request to update InvoiceSigma : {}", invoiceSigmaDTO);
        InvoiceSigma invoiceSigma = invoiceSigmaMapper.toEntity(invoiceSigmaDTO);
        invoiceSigma = invoiceSigmaRepository.save(invoiceSigma);
        return invoiceSigmaMapper.toDto(invoiceSigma);
    }

    /**
     * Partially update a invoiceSigma.
     *
     * @param invoiceSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceSigmaDTO> partialUpdate(InvoiceSigmaDTO invoiceSigmaDTO) {
        LOG.debug("Request to partially update InvoiceSigma : {}", invoiceSigmaDTO);

        return invoiceSigmaRepository
            .findById(invoiceSigmaDTO.getId())
            .map(existingInvoiceSigma -> {
                invoiceSigmaMapper.partialUpdate(existingInvoiceSigma, invoiceSigmaDTO);

                return existingInvoiceSigma;
            })
            .map(invoiceSigmaRepository::save)
            .map(invoiceSigmaMapper::toDto);
    }

    /**
     * Get one invoiceSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceSigma : {}", id);
        return invoiceSigmaRepository.findById(id).map(invoiceSigmaMapper::toDto);
    }

    /**
     * Delete the invoiceSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceSigma : {}", id);
        invoiceSigmaRepository.deleteById(id);
    }
}
