package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerMi;
import xyz.jhmapstruct.repository.CustomerMiRepository;
import xyz.jhmapstruct.service.dto.CustomerMiDTO;
import xyz.jhmapstruct.service.mapper.CustomerMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerMi}.
 */
@Service
@Transactional
public class CustomerMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMiService.class);

    private final CustomerMiRepository customerMiRepository;

    private final CustomerMiMapper customerMiMapper;

    public CustomerMiService(CustomerMiRepository customerMiRepository, CustomerMiMapper customerMiMapper) {
        this.customerMiRepository = customerMiRepository;
        this.customerMiMapper = customerMiMapper;
    }

    /**
     * Save a customerMi.
     *
     * @param customerMiDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerMiDTO save(CustomerMiDTO customerMiDTO) {
        LOG.debug("Request to save CustomerMi : {}", customerMiDTO);
        CustomerMi customerMi = customerMiMapper.toEntity(customerMiDTO);
        customerMi = customerMiRepository.save(customerMi);
        return customerMiMapper.toDto(customerMi);
    }

    /**
     * Update a customerMi.
     *
     * @param customerMiDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerMiDTO update(CustomerMiDTO customerMiDTO) {
        LOG.debug("Request to update CustomerMi : {}", customerMiDTO);
        CustomerMi customerMi = customerMiMapper.toEntity(customerMiDTO);
        customerMi = customerMiRepository.save(customerMi);
        return customerMiMapper.toDto(customerMi);
    }

    /**
     * Partially update a customerMi.
     *
     * @param customerMiDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one customerMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerMiDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerMi : {}", id);
        return customerMiRepository.findById(id).map(customerMiMapper::toDto);
    }

    /**
     * Delete the customerMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerMi : {}", id);
        customerMiRepository.deleteById(id);
    }
}
