package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerVi;
import xyz.jhmapstruct.repository.CustomerViRepository;
import xyz.jhmapstruct.service.dto.CustomerViDTO;
import xyz.jhmapstruct.service.mapper.CustomerViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerVi}.
 */
@Service
@Transactional
public class CustomerViService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerViService.class);

    private final CustomerViRepository customerViRepository;

    private final CustomerViMapper customerViMapper;

    public CustomerViService(CustomerViRepository customerViRepository, CustomerViMapper customerViMapper) {
        this.customerViRepository = customerViRepository;
        this.customerViMapper = customerViMapper;
    }

    /**
     * Save a customerVi.
     *
     * @param customerViDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerViDTO save(CustomerViDTO customerViDTO) {
        LOG.debug("Request to save CustomerVi : {}", customerViDTO);
        CustomerVi customerVi = customerViMapper.toEntity(customerViDTO);
        customerVi = customerViRepository.save(customerVi);
        return customerViMapper.toDto(customerVi);
    }

    /**
     * Update a customerVi.
     *
     * @param customerViDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerViDTO update(CustomerViDTO customerViDTO) {
        LOG.debug("Request to update CustomerVi : {}", customerViDTO);
        CustomerVi customerVi = customerViMapper.toEntity(customerViDTO);
        customerVi = customerViRepository.save(customerVi);
        return customerViMapper.toDto(customerVi);
    }

    /**
     * Partially update a customerVi.
     *
     * @param customerViDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one customerVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerViDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerVi : {}", id);
        return customerViRepository.findById(id).map(customerViMapper::toDto);
    }

    /**
     * Delete the customerVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerVi : {}", id);
        customerViRepository.deleteById(id);
    }
}
