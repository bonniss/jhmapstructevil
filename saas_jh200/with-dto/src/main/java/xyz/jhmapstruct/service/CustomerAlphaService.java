package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerAlpha;
import xyz.jhmapstruct.repository.CustomerAlphaRepository;
import xyz.jhmapstruct.service.dto.CustomerAlphaDTO;
import xyz.jhmapstruct.service.mapper.CustomerAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerAlpha}.
 */
@Service
@Transactional
public class CustomerAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerAlphaService.class);

    private final CustomerAlphaRepository customerAlphaRepository;

    private final CustomerAlphaMapper customerAlphaMapper;

    public CustomerAlphaService(CustomerAlphaRepository customerAlphaRepository, CustomerAlphaMapper customerAlphaMapper) {
        this.customerAlphaRepository = customerAlphaRepository;
        this.customerAlphaMapper = customerAlphaMapper;
    }

    /**
     * Save a customerAlpha.
     *
     * @param customerAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerAlphaDTO save(CustomerAlphaDTO customerAlphaDTO) {
        LOG.debug("Request to save CustomerAlpha : {}", customerAlphaDTO);
        CustomerAlpha customerAlpha = customerAlphaMapper.toEntity(customerAlphaDTO);
        customerAlpha = customerAlphaRepository.save(customerAlpha);
        return customerAlphaMapper.toDto(customerAlpha);
    }

    /**
     * Update a customerAlpha.
     *
     * @param customerAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerAlphaDTO update(CustomerAlphaDTO customerAlphaDTO) {
        LOG.debug("Request to update CustomerAlpha : {}", customerAlphaDTO);
        CustomerAlpha customerAlpha = customerAlphaMapper.toEntity(customerAlphaDTO);
        customerAlpha = customerAlphaRepository.save(customerAlpha);
        return customerAlphaMapper.toDto(customerAlpha);
    }

    /**
     * Partially update a customerAlpha.
     *
     * @param customerAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerAlphaDTO> partialUpdate(CustomerAlphaDTO customerAlphaDTO) {
        LOG.debug("Request to partially update CustomerAlpha : {}", customerAlphaDTO);

        return customerAlphaRepository
            .findById(customerAlphaDTO.getId())
            .map(existingCustomerAlpha -> {
                customerAlphaMapper.partialUpdate(existingCustomerAlpha, customerAlphaDTO);

                return existingCustomerAlpha;
            })
            .map(customerAlphaRepository::save)
            .map(customerAlphaMapper::toDto);
    }

    /**
     * Get one customerAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerAlpha : {}", id);
        return customerAlphaRepository.findById(id).map(customerAlphaMapper::toDto);
    }

    /**
     * Delete the customerAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerAlpha : {}", id);
        customerAlphaRepository.deleteById(id);
    }
}
