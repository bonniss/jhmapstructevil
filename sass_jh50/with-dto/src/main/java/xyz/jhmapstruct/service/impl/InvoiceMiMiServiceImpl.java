package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceMiMi;
import xyz.jhmapstruct.repository.InvoiceMiMiRepository;
import xyz.jhmapstruct.service.InvoiceMiMiService;
import xyz.jhmapstruct.service.dto.InvoiceMiMiDTO;
import xyz.jhmapstruct.service.mapper.InvoiceMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceMiMi}.
 */
@Service
@Transactional
public class InvoiceMiMiServiceImpl implements InvoiceMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiMiServiceImpl.class);

    private final InvoiceMiMiRepository invoiceMiMiRepository;

    private final InvoiceMiMiMapper invoiceMiMiMapper;

    public InvoiceMiMiServiceImpl(InvoiceMiMiRepository invoiceMiMiRepository, InvoiceMiMiMapper invoiceMiMiMapper) {
        this.invoiceMiMiRepository = invoiceMiMiRepository;
        this.invoiceMiMiMapper = invoiceMiMiMapper;
    }

    @Override
    public InvoiceMiMiDTO save(InvoiceMiMiDTO invoiceMiMiDTO) {
        LOG.debug("Request to save InvoiceMiMi : {}", invoiceMiMiDTO);
        InvoiceMiMi invoiceMiMi = invoiceMiMiMapper.toEntity(invoiceMiMiDTO);
        invoiceMiMi = invoiceMiMiRepository.save(invoiceMiMi);
        return invoiceMiMiMapper.toDto(invoiceMiMi);
    }

    @Override
    public InvoiceMiMiDTO update(InvoiceMiMiDTO invoiceMiMiDTO) {
        LOG.debug("Request to update InvoiceMiMi : {}", invoiceMiMiDTO);
        InvoiceMiMi invoiceMiMi = invoiceMiMiMapper.toEntity(invoiceMiMiDTO);
        invoiceMiMi = invoiceMiMiRepository.save(invoiceMiMi);
        return invoiceMiMiMapper.toDto(invoiceMiMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceMiMiDTO> findAll() {
        LOG.debug("Request to get all InvoiceMiMis");
        return invoiceMiMiRepository.findAll().stream().map(invoiceMiMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceMiMi : {}", id);
        return invoiceMiMiRepository.findById(id).map(invoiceMiMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceMiMi : {}", id);
        invoiceMiMiRepository.deleteById(id);
    }
}
