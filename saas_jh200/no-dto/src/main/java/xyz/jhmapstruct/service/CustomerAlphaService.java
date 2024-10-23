package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerAlpha;
import xyz.jhmapstruct.repository.CustomerAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerAlpha}.
 */
@Service
@Transactional
public class CustomerAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerAlphaService.class);

    private final CustomerAlphaRepository customerAlphaRepository;

    public CustomerAlphaService(CustomerAlphaRepository customerAlphaRepository) {
        this.customerAlphaRepository = customerAlphaRepository;
    }

    /**
     * Save a customerAlpha.
     *
     * @param customerAlpha the entity to save.
     * @return the persisted entity.
     */
    public CustomerAlpha save(CustomerAlpha customerAlpha) {
        LOG.debug("Request to save CustomerAlpha : {}", customerAlpha);
        return customerAlphaRepository.save(customerAlpha);
    }

    /**
     * Update a customerAlpha.
     *
     * @param customerAlpha the entity to save.
     * @return the persisted entity.
     */
    public CustomerAlpha update(CustomerAlpha customerAlpha) {
        LOG.debug("Request to update CustomerAlpha : {}", customerAlpha);
        return customerAlphaRepository.save(customerAlpha);
    }

    /**
     * Partially update a customerAlpha.
     *
     * @param customerAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerAlpha> partialUpdate(CustomerAlpha customerAlpha) {
        LOG.debug("Request to partially update CustomerAlpha : {}", customerAlpha);

        return customerAlphaRepository
            .findById(customerAlpha.getId())
            .map(existingCustomerAlpha -> {
                if (customerAlpha.getFirstName() != null) {
                    existingCustomerAlpha.setFirstName(customerAlpha.getFirstName());
                }
                if (customerAlpha.getLastName() != null) {
                    existingCustomerAlpha.setLastName(customerAlpha.getLastName());
                }
                if (customerAlpha.getEmail() != null) {
                    existingCustomerAlpha.setEmail(customerAlpha.getEmail());
                }
                if (customerAlpha.getPhoneNumber() != null) {
                    existingCustomerAlpha.setPhoneNumber(customerAlpha.getPhoneNumber());
                }

                return existingCustomerAlpha;
            })
            .map(customerAlphaRepository::save);
    }

    /**
     * Get one customerAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerAlpha> findOne(Long id) {
        LOG.debug("Request to get CustomerAlpha : {}", id);
        return customerAlphaRepository.findById(id);
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
