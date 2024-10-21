package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Invoice;
import xyz.jhmapstruct.repository.InvoiceRepository;
import xyz.jhmapstruct.service.InvoiceService;
import xyz.jhmapstruct.service.dto.InvoiceDTO;
import xyz.jhmapstruct.service.mapper.InvoiceMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Invoice}.
 */
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
    }

    @Override
    public InvoiceDTO save(InvoiceDTO invoiceDTO) {
        LOG.debug("Request to save Invoice : {}", invoiceDTO);
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(invoice);
    }

    @Override
    public InvoiceDTO update(InvoiceDTO invoiceDTO) {
        LOG.debug("Request to update Invoice : {}", invoiceDTO);
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(invoice);
    }

    @Override
    public Optional<InvoiceDTO> partialUpdate(InvoiceDTO invoiceDTO) {
        LOG.debug("Request to partially update Invoice : {}", invoiceDTO);

        return invoiceRepository
            .findById(invoiceDTO.getId())
            .map(existingInvoice -> {
                invoiceMapper.partialUpdate(existingInvoice, invoiceDTO);

                return existingInvoice;
            })
            .map(invoiceRepository::save)
            .map(invoiceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceDTO> findAll() {
        LOG.debug("Request to get all Invoices");
        return invoiceRepository.findAll().stream().map(invoiceMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceDTO> findOne(Long id) {
        LOG.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id).map(invoiceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }
}
