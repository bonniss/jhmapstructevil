package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceMi;
import xyz.jhmapstruct.repository.InvoiceMiRepository;
import xyz.jhmapstruct.service.dto.InvoiceMiDTO;
import xyz.jhmapstruct.service.mapper.InvoiceMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceMi}.
 */
@Service
@Transactional
public class InvoiceMiService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiService.class);

    private final InvoiceMiRepository invoiceMiRepository;

    private final InvoiceMiMapper invoiceMiMapper;

    public InvoiceMiService(InvoiceMiRepository invoiceMiRepository, InvoiceMiMapper invoiceMiMapper) {
        this.invoiceMiRepository = invoiceMiRepository;
        this.invoiceMiMapper = invoiceMiMapper;
    }

    /**
     * Save a invoiceMi.
     *
     * @param invoiceMiDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceMiDTO save(InvoiceMiDTO invoiceMiDTO) {
        LOG.debug("Request to save InvoiceMi : {}", invoiceMiDTO);
        InvoiceMi invoiceMi = invoiceMiMapper.toEntity(invoiceMiDTO);
        invoiceMi = invoiceMiRepository.save(invoiceMi);
        return invoiceMiMapper.toDto(invoiceMi);
    }

    /**
     * Update a invoiceMi.
     *
     * @param invoiceMiDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceMiDTO update(InvoiceMiDTO invoiceMiDTO) {
        LOG.debug("Request to update InvoiceMi : {}", invoiceMiDTO);
        InvoiceMi invoiceMi = invoiceMiMapper.toEntity(invoiceMiDTO);
        invoiceMi = invoiceMiRepository.save(invoiceMi);
        return invoiceMiMapper.toDto(invoiceMi);
    }

    /**
     * Partially update a invoiceMi.
     *
     * @param invoiceMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceMiDTO> partialUpdate(InvoiceMiDTO invoiceMiDTO) {
        LOG.debug("Request to partially update InvoiceMi : {}", invoiceMiDTO);

        return invoiceMiRepository
            .findById(invoiceMiDTO.getId())
            .map(existingInvoiceMi -> {
                invoiceMiMapper.partialUpdate(existingInvoiceMi, invoiceMiDTO);

                return existingInvoiceMi;
            })
            .map(invoiceMiRepository::save)
            .map(invoiceMiMapper::toDto);
    }

    /**
     * Get one invoiceMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceMiDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceMi : {}", id);
        return invoiceMiRepository.findById(id).map(invoiceMiMapper::toDto);
    }

    /**
     * Delete the invoiceMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceMi : {}", id);
        invoiceMiRepository.deleteById(id);
    }
}
