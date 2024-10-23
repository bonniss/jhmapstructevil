package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerMiMi;
import xyz.jhmapstruct.repository.CustomerMiMiRepository;
import xyz.jhmapstruct.service.dto.CustomerMiMiDTO;
import xyz.jhmapstruct.service.mapper.CustomerMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerMiMi}.
 */
@Service
@Transactional
public class CustomerMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMiMiService.class);

    private final CustomerMiMiRepository customerMiMiRepository;

    private final CustomerMiMiMapper customerMiMiMapper;

    public CustomerMiMiService(CustomerMiMiRepository customerMiMiRepository, CustomerMiMiMapper customerMiMiMapper) {
        this.customerMiMiRepository = customerMiMiRepository;
        this.customerMiMiMapper = customerMiMiMapper;
    }

    /**
     * Save a customerMiMi.
     *
     * @param customerMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerMiMiDTO save(CustomerMiMiDTO customerMiMiDTO) {
        LOG.debug("Request to save CustomerMiMi : {}", customerMiMiDTO);
        CustomerMiMi customerMiMi = customerMiMiMapper.toEntity(customerMiMiDTO);
        customerMiMi = customerMiMiRepository.save(customerMiMi);
        return customerMiMiMapper.toDto(customerMiMi);
    }

    /**
     * Update a customerMiMi.
     *
     * @param customerMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerMiMiDTO update(CustomerMiMiDTO customerMiMiDTO) {
        LOG.debug("Request to update CustomerMiMi : {}", customerMiMiDTO);
        CustomerMiMi customerMiMi = customerMiMiMapper.toEntity(customerMiMiDTO);
        customerMiMi = customerMiMiRepository.save(customerMiMi);
        return customerMiMiMapper.toDto(customerMiMi);
    }

    /**
     * Partially update a customerMiMi.
     *
     * @param customerMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one customerMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerMiMi : {}", id);
        return customerMiMiRepository.findById(id).map(customerMiMiMapper::toDto);
    }

    /**
     * Delete the customerMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerMiMi : {}", id);
        customerMiMiRepository.deleteById(id);
    }
}
