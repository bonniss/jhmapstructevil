package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerViVi;
import xyz.jhmapstruct.repository.CustomerViViRepository;
import xyz.jhmapstruct.service.dto.CustomerViViDTO;
import xyz.jhmapstruct.service.mapper.CustomerViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerViVi}.
 */
@Service
@Transactional
public class CustomerViViService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerViViService.class);

    private final CustomerViViRepository customerViViRepository;

    private final CustomerViViMapper customerViViMapper;

    public CustomerViViService(CustomerViViRepository customerViViRepository, CustomerViViMapper customerViViMapper) {
        this.customerViViRepository = customerViViRepository;
        this.customerViViMapper = customerViViMapper;
    }

    /**
     * Save a customerViVi.
     *
     * @param customerViViDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerViViDTO save(CustomerViViDTO customerViViDTO) {
        LOG.debug("Request to save CustomerViVi : {}", customerViViDTO);
        CustomerViVi customerViVi = customerViViMapper.toEntity(customerViViDTO);
        customerViVi = customerViViRepository.save(customerViVi);
        return customerViViMapper.toDto(customerViVi);
    }

    /**
     * Update a customerViVi.
     *
     * @param customerViViDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerViViDTO update(CustomerViViDTO customerViViDTO) {
        LOG.debug("Request to update CustomerViVi : {}", customerViViDTO);
        CustomerViVi customerViVi = customerViViMapper.toEntity(customerViViDTO);
        customerViVi = customerViViRepository.save(customerViVi);
        return customerViViMapper.toDto(customerViVi);
    }

    /**
     * Partially update a customerViVi.
     *
     * @param customerViViDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one customerViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerViViDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerViVi : {}", id);
        return customerViViRepository.findById(id).map(customerViViMapper::toDto);
    }

    /**
     * Delete the customerViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerViVi : {}", id);
        customerViViRepository.deleteById(id);
    }
}
