package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerGamma;
import xyz.jhmapstruct.repository.CustomerGammaRepository;
import xyz.jhmapstruct.service.dto.CustomerGammaDTO;
import xyz.jhmapstruct.service.mapper.CustomerGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerGamma}.
 */
@Service
@Transactional
public class CustomerGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerGammaService.class);

    private final CustomerGammaRepository customerGammaRepository;

    private final CustomerGammaMapper customerGammaMapper;

    public CustomerGammaService(CustomerGammaRepository customerGammaRepository, CustomerGammaMapper customerGammaMapper) {
        this.customerGammaRepository = customerGammaRepository;
        this.customerGammaMapper = customerGammaMapper;
    }

    /**
     * Save a customerGamma.
     *
     * @param customerGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerGammaDTO save(CustomerGammaDTO customerGammaDTO) {
        LOG.debug("Request to save CustomerGamma : {}", customerGammaDTO);
        CustomerGamma customerGamma = customerGammaMapper.toEntity(customerGammaDTO);
        customerGamma = customerGammaRepository.save(customerGamma);
        return customerGammaMapper.toDto(customerGamma);
    }

    /**
     * Update a customerGamma.
     *
     * @param customerGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerGammaDTO update(CustomerGammaDTO customerGammaDTO) {
        LOG.debug("Request to update CustomerGamma : {}", customerGammaDTO);
        CustomerGamma customerGamma = customerGammaMapper.toEntity(customerGammaDTO);
        customerGamma = customerGammaRepository.save(customerGamma);
        return customerGammaMapper.toDto(customerGamma);
    }

    /**
     * Partially update a customerGamma.
     *
     * @param customerGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerGammaDTO> partialUpdate(CustomerGammaDTO customerGammaDTO) {
        LOG.debug("Request to partially update CustomerGamma : {}", customerGammaDTO);

        return customerGammaRepository
            .findById(customerGammaDTO.getId())
            .map(existingCustomerGamma -> {
                customerGammaMapper.partialUpdate(existingCustomerGamma, customerGammaDTO);

                return existingCustomerGamma;
            })
            .map(customerGammaRepository::save)
            .map(customerGammaMapper::toDto);
    }

    /**
     * Get one customerGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerGammaDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerGamma : {}", id);
        return customerGammaRepository.findById(id).map(customerGammaMapper::toDto);
    }

    /**
     * Delete the customerGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerGamma : {}", id);
        customerGammaRepository.deleteById(id);
    }
}
