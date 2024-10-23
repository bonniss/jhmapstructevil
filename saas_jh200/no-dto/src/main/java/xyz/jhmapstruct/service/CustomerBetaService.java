package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerBeta;
import xyz.jhmapstruct.repository.CustomerBetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerBeta}.
 */
@Service
@Transactional
public class CustomerBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerBetaService.class);

    private final CustomerBetaRepository customerBetaRepository;

    public CustomerBetaService(CustomerBetaRepository customerBetaRepository) {
        this.customerBetaRepository = customerBetaRepository;
    }

    /**
     * Save a customerBeta.
     *
     * @param customerBeta the entity to save.
     * @return the persisted entity.
     */
    public CustomerBeta save(CustomerBeta customerBeta) {
        LOG.debug("Request to save CustomerBeta : {}", customerBeta);
        return customerBetaRepository.save(customerBeta);
    }

    /**
     * Update a customerBeta.
     *
     * @param customerBeta the entity to save.
     * @return the persisted entity.
     */
    public CustomerBeta update(CustomerBeta customerBeta) {
        LOG.debug("Request to update CustomerBeta : {}", customerBeta);
        return customerBetaRepository.save(customerBeta);
    }

    /**
     * Partially update a customerBeta.
     *
     * @param customerBeta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerBeta> partialUpdate(CustomerBeta customerBeta) {
        LOG.debug("Request to partially update CustomerBeta : {}", customerBeta);

        return customerBetaRepository
            .findById(customerBeta.getId())
            .map(existingCustomerBeta -> {
                if (customerBeta.getFirstName() != null) {
                    existingCustomerBeta.setFirstName(customerBeta.getFirstName());
                }
                if (customerBeta.getLastName() != null) {
                    existingCustomerBeta.setLastName(customerBeta.getLastName());
                }
                if (customerBeta.getEmail() != null) {
                    existingCustomerBeta.setEmail(customerBeta.getEmail());
                }
                if (customerBeta.getPhoneNumber() != null) {
                    existingCustomerBeta.setPhoneNumber(customerBeta.getPhoneNumber());
                }

                return existingCustomerBeta;
            })
            .map(customerBetaRepository::save);
    }

    /**
     * Get one customerBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerBeta> findOne(Long id) {
        LOG.debug("Request to get CustomerBeta : {}", id);
        return customerBetaRepository.findById(id);
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
