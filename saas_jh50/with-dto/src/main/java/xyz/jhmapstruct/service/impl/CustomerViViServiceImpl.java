package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerViVi;
import xyz.jhmapstruct.repository.CustomerViViRepository;
import xyz.jhmapstruct.service.CustomerViViService;
import xyz.jhmapstruct.service.dto.CustomerViViDTO;
import xyz.jhmapstruct.service.mapper.CustomerViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerViVi}.
 */
@Service
@Transactional
public class CustomerViViServiceImpl implements CustomerViViService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerViViServiceImpl.class);

    private final CustomerViViRepository customerViViRepository;

    private final CustomerViViMapper customerViViMapper;

    public CustomerViViServiceImpl(CustomerViViRepository customerViViRepository, CustomerViViMapper customerViViMapper) {
        this.customerViViRepository = customerViViRepository;
        this.customerViViMapper = customerViViMapper;
    }

    @Override
    public CustomerViViDTO save(CustomerViViDTO customerViViDTO) {
        LOG.debug("Request to save CustomerViVi : {}", customerViViDTO);
        CustomerViVi customerViVi = customerViViMapper.toEntity(customerViViDTO);
        customerViVi = customerViViRepository.save(customerViVi);
        return customerViViMapper.toDto(customerViVi);
    }

    @Override
    public CustomerViViDTO update(CustomerViViDTO customerViViDTO) {
        LOG.debug("Request to update CustomerViVi : {}", customerViViDTO);
        CustomerViVi customerViVi = customerViViMapper.toEntity(customerViViDTO);
        customerViVi = customerViViRepository.save(customerViVi);
        return customerViViMapper.toDto(customerViVi);
    }

    @Override
    public Optional<CustomerViViDTO> partialUpdate(CustomerViViDTO customerViViDTO) {
        LOG.debug("Request to partially update CustomerViVi : {}", customerViViDTO);

        return customerViViRepository
            .findById(customerViViDTO.getId())
            .map(existingCustomerViVi -> {
                customerViViMapper.partialUpdate(existingCustomerViVi, customerViViDTO);

                return existingCustomerViVi;
            })
            .map(customerViViRepository::save)
            .map(customerViViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerViViDTO> findAll() {
        LOG.debug("Request to get all CustomerViVis");
        return customerViViRepository.findAll().stream().map(customerViViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerViViDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerViVi : {}", id);
        return customerViViRepository.findById(id).map(customerViViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerViVi : {}", id);
        customerViViRepository.deleteById(id);
    }
}
