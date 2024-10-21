package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceViVi;
import xyz.jhmapstruct.repository.InvoiceViViRepository;
import xyz.jhmapstruct.service.InvoiceViViService;
import xyz.jhmapstruct.service.dto.InvoiceViViDTO;
import xyz.jhmapstruct.service.mapper.InvoiceViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceViVi}.
 */
@Service
@Transactional
public class InvoiceViViServiceImpl implements InvoiceViViService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViViServiceImpl.class);

    private final InvoiceViViRepository invoiceViViRepository;

    private final InvoiceViViMapper invoiceViViMapper;

    public InvoiceViViServiceImpl(InvoiceViViRepository invoiceViViRepository, InvoiceViViMapper invoiceViViMapper) {
        this.invoiceViViRepository = invoiceViViRepository;
        this.invoiceViViMapper = invoiceViViMapper;
    }

    @Override
    public InvoiceViViDTO save(InvoiceViViDTO invoiceViViDTO) {
        LOG.debug("Request to save InvoiceViVi : {}", invoiceViViDTO);
        InvoiceViVi invoiceViVi = invoiceViViMapper.toEntity(invoiceViViDTO);
        invoiceViVi = invoiceViViRepository.save(invoiceViVi);
        return invoiceViViMapper.toDto(invoiceViVi);
    }

    @Override
    public InvoiceViViDTO update(InvoiceViViDTO invoiceViViDTO) {
        LOG.debug("Request to update InvoiceViVi : {}", invoiceViViDTO);
        InvoiceViVi invoiceViVi = invoiceViViMapper.toEntity(invoiceViViDTO);
        invoiceViVi = invoiceViViRepository.save(invoiceViVi);
        return invoiceViViMapper.toDto(invoiceViVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceViViDTO> findAll() {
        LOG.debug("Request to get all InvoiceViVis");
        return invoiceViViRepository.findAll().stream().map(invoiceViViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceViViDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceViVi : {}", id);
        return invoiceViViRepository.findById(id).map(invoiceViViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceViVi : {}", id);
        invoiceViViRepository.deleteById(id);
    }
}
