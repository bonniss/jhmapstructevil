package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.InvoiceVi;
import xyz.jhmapstruct.repository.InvoiceViRepository;
import xyz.jhmapstruct.service.InvoiceViService;
import xyz.jhmapstruct.service.dto.InvoiceViDTO;
import xyz.jhmapstruct.service.mapper.InvoiceViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.InvoiceVi}.
 */
@Service
@Transactional
public class InvoiceViServiceImpl implements InvoiceViService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViServiceImpl.class);

    private final InvoiceViRepository invoiceViRepository;

    private final InvoiceViMapper invoiceViMapper;

    public InvoiceViServiceImpl(InvoiceViRepository invoiceViRepository, InvoiceViMapper invoiceViMapper) {
        this.invoiceViRepository = invoiceViRepository;
        this.invoiceViMapper = invoiceViMapper;
    }

    @Override
    public InvoiceViDTO save(InvoiceViDTO invoiceViDTO) {
        LOG.debug("Request to save InvoiceVi : {}", invoiceViDTO);
        InvoiceVi invoiceVi = invoiceViMapper.toEntity(invoiceViDTO);
        invoiceVi = invoiceViRepository.save(invoiceVi);
        return invoiceViMapper.toDto(invoiceVi);
    }

    @Override
    public InvoiceViDTO update(InvoiceViDTO invoiceViDTO) {
        LOG.debug("Request to update InvoiceVi : {}", invoiceViDTO);
        InvoiceVi invoiceVi = invoiceViMapper.toEntity(invoiceViDTO);
        invoiceVi = invoiceViRepository.save(invoiceVi);
        return invoiceViMapper.toDto(invoiceVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceViDTO> findAll() {
        LOG.debug("Request to get all InvoiceVis");
        return invoiceViRepository.findAll().stream().map(invoiceViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceViDTO> findOne(Long id) {
        LOG.debug("Request to get InvoiceVi : {}", id);
        return invoiceViRepository.findById(id).map(invoiceViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete InvoiceVi : {}", id);
        invoiceViRepository.deleteById(id);
    }
}
