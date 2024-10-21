package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerMi;
import xyz.jhmapstruct.repository.CustomerMiRepository;
import xyz.jhmapstruct.service.CustomerMiService;
import xyz.jhmapstruct.service.dto.CustomerMiDTO;
import xyz.jhmapstruct.service.mapper.CustomerMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerMi}.
 */
@Service
@Transactional
public class CustomerMiServiceImpl implements CustomerMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMiServiceImpl.class);

    private final CustomerMiRepository customerMiRepository;

    private final CustomerMiMapper customerMiMapper;

    public CustomerMiServiceImpl(CustomerMiRepository customerMiRepository, CustomerMiMapper customerMiMapper) {
        this.customerMiRepository = customerMiRepository;
        this.customerMiMapper = customerMiMapper;
    }

    @Override
    public CustomerMiDTO save(CustomerMiDTO customerMiDTO) {
        LOG.debug("Request to save CustomerMi : {}", customerMiDTO);
        CustomerMi customerMi = customerMiMapper.toEntity(customerMiDTO);
        customerMi = customerMiRepository.save(customerMi);
        return customerMiMapper.toDto(customerMi);
    }

    @Override
    public CustomerMiDTO update(CustomerMiDTO customerMiDTO) {
        LOG.debug("Request to update CustomerMi : {}", customerMiDTO);
        CustomerMi customerMi = customerMiMapper.toEntity(customerMiDTO);
        customerMi = customerMiRepository.save(customerMi);
        return customerMiMapper.toDto(customerMi);
    }

    @Override
    public Optional<CustomerMiDTO> partialUpdate(CustomerMiDTO customerMiDTO) {
        LOG.debug("Request to partially update CustomerMi : {}", customerMiDTO);

        return customerMiRepository
            .findById(customerMiDTO.getId())
            .map(existingCustomerMi -> {
                customerMiMapper.partialUpdate(existingCustomerMi, customerMiDTO);

                return existingCustomerMi;
            })
            .map(customerMiRepository::save)
            .map(customerMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerMiDTO> findAll() {
        LOG.debug("Request to get all CustomerMis");
        return customerMiRepository.findAll().stream().map(customerMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerMiDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerMi : {}", id);
        return customerMiRepository.findById(id).map(customerMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerMi : {}", id);
        customerMiRepository.deleteById(id);
    }
}
