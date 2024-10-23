package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerGamma;
import xyz.jhmapstruct.repository.CustomerGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerGamma}.
 */
@Service
@Transactional
public class CustomerGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerGammaService.class);

    private final CustomerGammaRepository customerGammaRepository;

    public CustomerGammaService(CustomerGammaRepository customerGammaRepository) {
        this.customerGammaRepository = customerGammaRepository;
    }

    /**
     * Save a customerGamma.
     *
     * @param customerGamma the entity to save.
     * @return the persisted entity.
     */
    public CustomerGamma save(CustomerGamma customerGamma) {
        LOG.debug("Request to save CustomerGamma : {}", customerGamma);
        return customerGammaRepository.save(customerGamma);
    }

    /**
     * Update a customerGamma.
     *
     * @param customerGamma the entity to save.
     * @return the persisted entity.
     */
    public CustomerGamma update(CustomerGamma customerGamma) {
        LOG.debug("Request to update CustomerGamma : {}", customerGamma);
        return customerGammaRepository.save(customerGamma);
    }

    /**
     * Partially update a customerGamma.
     *
     * @param customerGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerGamma> partialUpdate(CustomerGamma customerGamma) {
        LOG.debug("Request to partially update CustomerGamma : {}", customerGamma);

        return customerGammaRepository
            .findById(customerGamma.getId())
            .map(existingCustomerGamma -> {
                if (customerGamma.getFirstName() != null) {
                    existingCustomerGamma.setFirstName(customerGamma.getFirstName());
                }
                if (customerGamma.getLastName() != null) {
                    existingCustomerGamma.setLastName(customerGamma.getLastName());
                }
                if (customerGamma.getEmail() != null) {
                    existingCustomerGamma.setEmail(customerGamma.getEmail());
                }
                if (customerGamma.getPhoneNumber() != null) {
                    existingCustomerGamma.setPhoneNumber(customerGamma.getPhoneNumber());
                }

                return existingCustomerGamma;
            })
            .map(customerGammaRepository::save);
    }

    /**
     * Get one customerGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerGamma> findOne(Long id) {
        LOG.debug("Request to get CustomerGamma : {}", id);
        return customerGammaRepository.findById(id);
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
