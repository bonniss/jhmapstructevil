package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerVi;
import xyz.jhmapstruct.repository.CustomerViRepository;
import xyz.jhmapstruct.service.CustomerViService;
import xyz.jhmapstruct.service.dto.CustomerViDTO;
import xyz.jhmapstruct.service.mapper.CustomerViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerVi}.
 */
@Service
@Transactional
public class CustomerViServiceImpl implements CustomerViService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerViServiceImpl.class);

    private final CustomerViRepository customerViRepository;

    private final CustomerViMapper customerViMapper;

    public CustomerViServiceImpl(CustomerViRepository customerViRepository, CustomerViMapper customerViMapper) {
        this.customerViRepository = customerViRepository;
        this.customerViMapper = customerViMapper;
    }

    @Override
    public CustomerViDTO save(CustomerViDTO customerViDTO) {
        LOG.debug("Request to save CustomerVi : {}", customerViDTO);
        CustomerVi customerVi = customerViMapper.toEntity(customerViDTO);
        customerVi = customerViRepository.save(customerVi);
        return customerViMapper.toDto(customerVi);
    }

    @Override
    public CustomerViDTO update(CustomerViDTO customerViDTO) {
        LOG.debug("Request to update CustomerVi : {}", customerViDTO);
        CustomerVi customerVi = customerViMapper.toEntity(customerViDTO);
        customerVi = customerViRepository.save(customerVi);
        return customerViMapper.toDto(customerVi);
    }

    @Override
    public Optional<CustomerViDTO> partialUpdate(CustomerViDTO customerViDTO) {
        LOG.debug("Request to partially update CustomerVi : {}", customerViDTO);

        return customerViRepository
            .findById(customerViDTO.getId())
            .map(existingCustomerVi -> {
                customerViMapper.partialUpdate(existingCustomerVi, customerViDTO);

                return existingCustomerVi;
            })
            .map(customerViRepository::save)
            .map(customerViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerViDTO> findAll() {
        LOG.debug("Request to get all CustomerVis");
        return customerViRepository.findAll().stream().map(customerViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerViDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerVi : {}", id);
        return customerViRepository.findById(id).map(customerViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerVi : {}", id);
        customerViRepository.deleteById(id);
    }
}
