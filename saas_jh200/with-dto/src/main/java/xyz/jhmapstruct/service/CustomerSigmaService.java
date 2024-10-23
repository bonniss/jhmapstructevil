package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerSigma;
import xyz.jhmapstruct.repository.CustomerSigmaRepository;
import xyz.jhmapstruct.service.dto.CustomerSigmaDTO;
import xyz.jhmapstruct.service.mapper.CustomerSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerSigma}.
 */
@Service
@Transactional
public class CustomerSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerSigmaService.class);

    private final CustomerSigmaRepository customerSigmaRepository;

    private final CustomerSigmaMapper customerSigmaMapper;

    public CustomerSigmaService(CustomerSigmaRepository customerSigmaRepository, CustomerSigmaMapper customerSigmaMapper) {
        this.customerSigmaRepository = customerSigmaRepository;
        this.customerSigmaMapper = customerSigmaMapper;
    }

    /**
     * Save a customerSigma.
     *
     * @param customerSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerSigmaDTO save(CustomerSigmaDTO customerSigmaDTO) {
        LOG.debug("Request to save CustomerSigma : {}", customerSigmaDTO);
        CustomerSigma customerSigma = customerSigmaMapper.toEntity(customerSigmaDTO);
        customerSigma = customerSigmaRepository.save(customerSigma);
        return customerSigmaMapper.toDto(customerSigma);
    }

    /**
     * Update a customerSigma.
     *
     * @param customerSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerSigmaDTO update(CustomerSigmaDTO customerSigmaDTO) {
        LOG.debug("Request to update CustomerSigma : {}", customerSigmaDTO);
        CustomerSigma customerSigma = customerSigmaMapper.toEntity(customerSigmaDTO);
        customerSigma = customerSigmaRepository.save(customerSigma);
        return customerSigmaMapper.toDto(customerSigma);
    }

    /**
     * Partially update a customerSigma.
     *
     * @param customerSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerSigmaDTO> partialUpdate(CustomerSigmaDTO customerSigmaDTO) {
        LOG.debug("Request to partially update CustomerSigma : {}", customerSigmaDTO);

        return customerSigmaRepository
            .findById(customerSigmaDTO.getId())
            .map(existingCustomerSigma -> {
                customerSigmaMapper.partialUpdate(existingCustomerSigma, customerSigmaDTO);

                return existingCustomerSigma;
            })
            .map(customerSigmaRepository::save)
            .map(customerSigmaMapper::toDto);
    }

    /**
     * Get one customerSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerSigma : {}", id);
        return customerSigmaRepository.findById(id).map(customerSigmaMapper::toDto);
    }

    /**
     * Delete the customerSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerSigma : {}", id);
        customerSigmaRepository.deleteById(id);
    }
}
