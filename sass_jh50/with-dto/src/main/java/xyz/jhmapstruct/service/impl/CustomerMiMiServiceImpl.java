package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerMiMi;
import xyz.jhmapstruct.repository.CustomerMiMiRepository;
import xyz.jhmapstruct.service.CustomerMiMiService;
import xyz.jhmapstruct.service.dto.CustomerMiMiDTO;
import xyz.jhmapstruct.service.mapper.CustomerMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerMiMi}.
 */
@Service
@Transactional
public class CustomerMiMiServiceImpl implements CustomerMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMiMiServiceImpl.class);

    private final CustomerMiMiRepository customerMiMiRepository;

    private final CustomerMiMiMapper customerMiMiMapper;

    public CustomerMiMiServiceImpl(CustomerMiMiRepository customerMiMiRepository, CustomerMiMiMapper customerMiMiMapper) {
        this.customerMiMiRepository = customerMiMiRepository;
        this.customerMiMiMapper = customerMiMiMapper;
    }

    @Override
    public CustomerMiMiDTO save(CustomerMiMiDTO customerMiMiDTO) {
        LOG.debug("Request to save CustomerMiMi : {}", customerMiMiDTO);
        CustomerMiMi customerMiMi = customerMiMiMapper.toEntity(customerMiMiDTO);
        customerMiMi = customerMiMiRepository.save(customerMiMi);
        return customerMiMiMapper.toDto(customerMiMi);
    }

    @Override
    public CustomerMiMiDTO update(CustomerMiMiDTO customerMiMiDTO) {
        LOG.debug("Request to update CustomerMiMi : {}", customerMiMiDTO);
        CustomerMiMi customerMiMi = customerMiMiMapper.toEntity(customerMiMiDTO);
        customerMiMi = customerMiMiRepository.save(customerMiMi);
        return customerMiMiMapper.toDto(customerMiMi);
    }

    @Override
    public Optional<CustomerMiMiDTO> partialUpdate(CustomerMiMiDTO customerMiMiDTO) {
        LOG.debug("Request to partially update CustomerMiMi : {}", customerMiMiDTO);

        return customerMiMiRepository
            .findById(customerMiMiDTO.getId())
            .map(existingCustomerMiMi -> {
                customerMiMiMapper.partialUpdate(existingCustomerMiMi, customerMiMiDTO);

                return existingCustomerMiMi;
            })
            .map(customerMiMiRepository::save)
            .map(customerMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerMiMiDTO> findAll() {
        LOG.debug("Request to get all CustomerMiMis");
        return customerMiMiRepository.findAll().stream().map(customerMiMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerMiMi : {}", id);
        return customerMiMiRepository.findById(id).map(customerMiMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerMiMi : {}", id);
        customerMiMiRepository.deleteById(id);
    }
}
