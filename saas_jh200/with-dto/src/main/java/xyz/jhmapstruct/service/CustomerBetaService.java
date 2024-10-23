package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerBeta;
import xyz.jhmapstruct.repository.CustomerBetaRepository;
import xyz.jhmapstruct.service.dto.CustomerBetaDTO;
import xyz.jhmapstruct.service.mapper.CustomerBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerBeta}.
 */
@Service
@Transactional
public class CustomerBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerBetaService.class);

    private final CustomerBetaRepository customerBetaRepository;

    private final CustomerBetaMapper customerBetaMapper;

    public CustomerBetaService(CustomerBetaRepository customerBetaRepository, CustomerBetaMapper customerBetaMapper) {
        this.customerBetaRepository = customerBetaRepository;
        this.customerBetaMapper = customerBetaMapper;
    }

    /**
     * Save a customerBeta.
     *
     * @param customerBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerBetaDTO save(CustomerBetaDTO customerBetaDTO) {
        LOG.debug("Request to save CustomerBeta : {}", customerBetaDTO);
        CustomerBeta customerBeta = customerBetaMapper.toEntity(customerBetaDTO);
        customerBeta = customerBetaRepository.save(customerBeta);
        return customerBetaMapper.toDto(customerBeta);
    }

    /**
     * Update a customerBeta.
     *
     * @param customerBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerBetaDTO update(CustomerBetaDTO customerBetaDTO) {
        LOG.debug("Request to update CustomerBeta : {}", customerBetaDTO);
        CustomerBeta customerBeta = customerBetaMapper.toEntity(customerBetaDTO);
        customerBeta = customerBetaRepository.save(customerBeta);
        return customerBetaMapper.toDto(customerBeta);
    }

    /**
     * Partially update a customerBeta.
     *
     * @param customerBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerBetaDTO> partialUpdate(CustomerBetaDTO customerBetaDTO) {
        LOG.debug("Request to partially update CustomerBeta : {}", customerBetaDTO);

        return customerBetaRepository
            .findById(customerBetaDTO.getId())
            .map(existingCustomerBeta -> {
                customerBetaMapper.partialUpdate(existingCustomerBeta, customerBetaDTO);

                return existingCustomerBeta;
            })
            .map(customerBetaRepository::save)
            .map(customerBetaMapper::toDto);
    }

    /**
     * Get one customerBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerBetaDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerBeta : {}", id);
        return customerBetaRepository.findById(id).map(customerBetaMapper::toDto);
    }

    /**
     * Delete the customerBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerBeta : {}", id);
        customerBetaRepository.deleteById(id);
    }
}
