package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerTheta;
import xyz.jhmapstruct.repository.CustomerThetaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerTheta}.
 */
@Service
@Transactional
public class CustomerThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerThetaService.class);

    private final CustomerThetaRepository customerThetaRepository;

    public CustomerThetaService(CustomerThetaRepository customerThetaRepository) {
        this.customerThetaRepository = customerThetaRepository;
    }

    /**
     * Save a customerTheta.
     *
     * @param customerTheta the entity to save.
     * @return the persisted entity.
     */
    public CustomerTheta save(CustomerTheta customerTheta) {
        LOG.debug("Request to save CustomerTheta : {}", customerTheta);
        return customerThetaRepository.save(customerTheta);
    }

    /**
     * Update a customerTheta.
     *
     * @param customerTheta the entity to save.
     * @return the persisted entity.
     */
    public CustomerTheta update(CustomerTheta customerTheta) {
        LOG.debug("Request to update CustomerTheta : {}", customerTheta);
        return customerThetaRepository.save(customerTheta);
    }

    /**
     * Partially update a customerTheta.
     *
     * @param customerTheta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerTheta> partialUpdate(CustomerTheta customerTheta) {
        LOG.debug("Request to partially update CustomerTheta : {}", customerTheta);

        return customerThetaRepository
            .findById(customerTheta.getId())
            .map(existingCustomerTheta -> {
                if (customerTheta.getFirstName() != null) {
                    existingCustomerTheta.setFirstName(customerTheta.getFirstName());
                }
                if (customerTheta.getLastName() != null) {
                    existingCustomerTheta.setLastName(customerTheta.getLastName());
                }
                if (customerTheta.getEmail() != null) {
                    existingCustomerTheta.setEmail(customerTheta.getEmail());
                }
                if (customerTheta.getPhoneNumber() != null) {
                    existingCustomerTheta.setPhoneNumber(customerTheta.getPhoneNumber());
                }

                return existingCustomerTheta;
            })
            .map(customerThetaRepository::save);
    }

    /**
     * Get one customerTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerTheta> findOne(Long id) {
        LOG.debug("Request to get CustomerTheta : {}", id);
        return customerThetaRepository.findById(id);
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
