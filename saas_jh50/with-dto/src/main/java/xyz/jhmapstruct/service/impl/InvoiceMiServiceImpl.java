package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceMi;
import xyz.jhmapstruct.repository.InvoiceMiRepository;
import xyz.jhmapstruct.service.InvoiceMiService;
import xyz.jhmapstruct.service.dto.InvoiceMiDTO;
import xyz.jhmapstruct.service.mapper.InvoiceMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceMi}.
 */
@Service
@Transactional
public class InvoiceMiServiceImpl implements InvoiceMiService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiServiceImpl.class);

    private final InvoiceMiRepository invoiceMiRepository;

    private final InvoiceMiMapper invoiceMiMapper;

    public InvoiceMiServiceImpl(InvoiceMiRepository invoiceMiRepository, InvoiceMiMapper invoiceMiMapper) {
        this.invoiceMiRepository = invoiceMiRepository;
        this.invoiceMiMapper = invoiceMiMapper;
    }

    @Override
    public InvoiceMiDTO save(InvoiceMiDTO invoiceMiDTO) {
        LOG.debug("Request to save InvoiceMi : {}", invoiceMiDTO);
        InvoiceMi invoiceMi = invoiceMiMapper.toEntity(invoiceMiDTO);
        invoiceMi = invoiceMiRepository.save(invoiceMi);
        return invoiceMiMapper.toDto(invoiceMi);
    }

    @Override
    public InvoiceMiDTO update(InvoiceMiDTO invoiceMiDTO) {
        LOG.debug("Request to update InvoiceMi : {}", invoiceMiDTO);
        InvoiceMi invoiceMi = invoiceMiMapper.toEntity(invoiceMiDTO);
        invoiceMi = invoiceMiRepository.save(invoiceMi);
        return invoiceMiMapper.toDto(invoiceMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceMiDTO> findAll() {
        LOG.debug("Request to get all InvoiceMis");
        return invoiceMiRepository.findAll().stream().map(invoiceMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceMiDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceMi : {}", id);
        return invoiceMiRepository.findById(id).map(invoiceMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceMi : {}", id);
        invoiceMiRepository.deleteById(id);
    }
}
