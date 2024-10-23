package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceMiMi;
import xyz.jhmapstruct.repository.InvoiceMiMiRepository;
import xyz.jhmapstruct.service.dto.InvoiceMiMiDTO;
import xyz.jhmapstruct.service.mapper.InvoiceMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceMiMi}.
 */
@Service
@Transactional
public class InvoiceMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiMiService.class);

    private final InvoiceMiMiRepository invoiceMiMiRepository;

    private final InvoiceMiMiMapper invoiceMiMiMapper;

    public InvoiceMiMiService(InvoiceMiMiRepository invoiceMiMiRepository, InvoiceMiMiMapper invoiceMiMiMapper) {
        this.invoiceMiMiRepository = invoiceMiMiRepository;
        this.invoiceMiMiMapper = invoiceMiMiMapper;
    }

    /**
     * Save a invoiceMiMi.
     *
     * @param invoiceMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceMiMiDTO save(InvoiceMiMiDTO invoiceMiMiDTO) {
        LOG.debug("Request to save InvoiceMiMi : {}", invoiceMiMiDTO);
        InvoiceMiMi invoiceMiMi = invoiceMiMiMapper.toEntity(invoiceMiMiDTO);
        invoiceMiMi = invoiceMiMiRepository.save(invoiceMiMi);
        return invoiceMiMiMapper.toDto(invoiceMiMi);
    }

    /**
     * Update a invoiceMiMi.
     *
     * @param invoiceMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceMiMiDTO update(InvoiceMiMiDTO invoiceMiMiDTO) {
        LOG.debug("Request to update InvoiceMiMi : {}", invoiceMiMiDTO);
        InvoiceMiMi invoiceMiMi = invoiceMiMiMapper.toEntity(invoiceMiMiDTO);
        invoiceMiMi = invoiceMiMiRepository.save(invoiceMiMi);
        return invoiceMiMiMapper.toDto(invoiceMiMi);
    }

    /**
     * Partially update a invoiceMiMi.
     *
     * @param invoiceMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceMiMiDTO> partialUpdate(InvoiceMiMiDTO invoiceMiMiDTO) {
        LOG.debug("Request to partially update InvoiceMiMi : {}", invoiceMiMiDTO);

        return invoiceMiMiRepository
            .findById(invoiceMiMiDTO.getId())
            .map(existingInvoiceMiMi -> {
                invoiceMiMiMapper.partialUpdate(existingInvoiceMiMi, invoiceMiMiDTO);

                return existingInvoiceMiMi;
            })
            .map(invoiceMiMiRepository::save)
            .map(invoiceMiMiMapper::toDto);
    }

    /**
     * Get one invoiceMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceMiMi : {}", id);
        return invoiceMiMiRepository.findById(id).map(invoiceMiMiMapper::toDto);
    }

    /**
     * Delete the invoiceMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceMiMi : {}", id);
        invoiceMiMiRepository.deleteById(id);
    }
}
