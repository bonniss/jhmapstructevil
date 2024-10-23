package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerTheta;
import xyz.jhmapstruct.repository.CustomerThetaRepository;
import xyz.jhmapstruct.service.dto.CustomerThetaDTO;
import xyz.jhmapstruct.service.mapper.CustomerThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerTheta}.
 */
@Service
@Transactional
public class CustomerThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerThetaService.class);

    private final CustomerThetaRepository customerThetaRepository;

    private final CustomerThetaMapper customerThetaMapper;

    public CustomerThetaService(CustomerThetaRepository customerThetaRepository, CustomerThetaMapper customerThetaMapper) {
        this.customerThetaRepository = customerThetaRepository;
        this.customerThetaMapper = customerThetaMapper;
    }

    /**
     * Save a customerTheta.
     *
     * @param customerThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerThetaDTO save(CustomerThetaDTO customerThetaDTO) {
        LOG.debug("Request to save CustomerTheta : {}", customerThetaDTO);
        CustomerTheta customerTheta = customerThetaMapper.toEntity(customerThetaDTO);
        customerTheta = customerThetaRepository.save(customerTheta);
        return customerThetaMapper.toDto(customerTheta);
    }

    /**
     * Update a customerTheta.
     *
     * @param customerThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerThetaDTO update(CustomerThetaDTO customerThetaDTO) {
        LOG.debug("Request to update CustomerTheta : {}", customerThetaDTO);
        CustomerTheta customerTheta = customerThetaMapper.toEntity(customerThetaDTO);
        customerTheta = customerThetaRepository.save(customerTheta);
        return customerThetaMapper.toDto(customerTheta);
    }

    /**
     * Partially update a customerTheta.
     *
     * @param customerThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerThetaDTO> partialUpdate(CustomerThetaDTO customerThetaDTO) {
        LOG.debug("Request to partially update CustomerTheta : {}", customerThetaDTO);

        return customerThetaRepository
            .findById(customerThetaDTO.getId())
            .map(existingCustomerTheta -> {
                customerThetaMapper.partialUpdate(existingCustomerTheta, customerThetaDTO);

                return existingCustomerTheta;
            })
            .map(customerThetaRepository::save)
            .map(customerThetaMapper::toDto);
    }

    /**
     * Get one customerTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerThetaDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerTheta : {}", id);
        return customerThetaRepository.findById(id).map(customerThetaMapper::toDto);
    }

    /**
     * Delete the customerTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerTheta : {}", id);
        customerThetaRepository.deleteById(id);
    }
}
