package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerMi;
import xyz.jhmapstruct.repository.CustomerMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerMi}.
 */
@Service
@Transactional
public class CustomerMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMiService.class);

    private final CustomerMiRepository customerMiRepository;

    public CustomerMiService(CustomerMiRepository customerMiRepository) {
        this.customerMiRepository = customerMiRepository;
    }

    /**
     * Save a customerMi.
     *
     * @param customerMi the entity to save.
     * @return the persisted entity.
     */
    public CustomerMi save(CustomerMi customerMi) {
        LOG.debug("Request to save CustomerMi : {}", customerMi);
        return customerMiRepository.save(customerMi);
    }

    /**
     * Update a customerMi.
     *
     * @param customerMi the entity to save.
     * @return the persisted entity.
     */
    public CustomerMi update(CustomerMi customerMi) {
        LOG.debug("Request to update CustomerMi : {}", customerMi);
        return customerMiRepository.save(customerMi);
    }

    /**
     * Partially update a customerMi.
     *
     * @param customerMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerMi> partialUpdate(CustomerMi customerMi) {
        LOG.debug("Request to partially update CustomerMi : {}", customerMi);

        return customerMiRepository
            .findById(customerMi.getId())
            .map(existingCustomerMi -> {
                if (customerMi.getFirstName() != null) {
                    existingCustomerMi.setFirstName(customerMi.getFirstName());
                }
                if (customerMi.getLastName() != null) {
                    existingCustomerMi.setLastName(customerMi.getLastName());
                }
                if (customerMi.getEmail() != null) {
                    existingCustomerMi.setEmail(customerMi.getEmail());
                }
                if (customerMi.getPhoneNumber() != null) {
                    existingCustomerMi.setPhoneNumber(customerMi.getPhoneNumber());
                }

                return existingCustomerMi;
            })
            .map(customerMiRepository::save);
    }

    /**
     * Get one customerMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerMi> findOne(Long id) {
        LOG.debug("Request to get CustomerMi : {}", id);
        return customerMiRepository.findById(id);
    }

    /**
     * Delete the customerMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerMi : {}", id);
        customerMiRepository.deleteById(id);
    }
}
