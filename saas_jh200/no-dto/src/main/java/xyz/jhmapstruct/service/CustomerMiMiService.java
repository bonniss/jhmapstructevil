package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerMiMi;
import xyz.jhmapstruct.repository.CustomerMiMiRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerMiMi}.
 */
@Service
@Transactional
public class CustomerMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMiMiService.class);

    private final CustomerMiMiRepository customerMiMiRepository;

    public CustomerMiMiService(CustomerMiMiRepository customerMiMiRepository) {
        this.customerMiMiRepository = customerMiMiRepository;
    }

    /**
     * Save a customerMiMi.
     *
     * @param customerMiMi the entity to save.
     * @return the persisted entity.
     */
    public CustomerMiMi save(CustomerMiMi customerMiMi) {
        LOG.debug("Request to save CustomerMiMi : {}", customerMiMi);
        return customerMiMiRepository.save(customerMiMi);
    }

    /**
     * Update a customerMiMi.
     *
     * @param customerMiMi the entity to save.
     * @return the persisted entity.
     */
    public CustomerMiMi update(CustomerMiMi customerMiMi) {
        LOG.debug("Request to update CustomerMiMi : {}", customerMiMi);
        return customerMiMiRepository.save(customerMiMi);
    }

    /**
     * Partially update a customerMiMi.
     *
     * @param customerMiMi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerMiMi> partialUpdate(CustomerMiMi customerMiMi) {
        LOG.debug("Request to partially update CustomerMiMi : {}", customerMiMi);

        return customerMiMiRepository
            .findById(customerMiMi.getId())
            .map(existingCustomerMiMi -> {
                if (customerMiMi.getFirstName() != null) {
                    existingCustomerMiMi.setFirstName(customerMiMi.getFirstName());
                }
                if (customerMiMi.getLastName() != null) {
                    existingCustomerMiMi.setLastName(customerMiMi.getLastName());
                }
                if (customerMiMi.getEmail() != null) {
                    existingCustomerMiMi.setEmail(customerMiMi.getEmail());
                }
                if (customerMiMi.getPhoneNumber() != null) {
                    existingCustomerMiMi.setPhoneNumber(customerMiMi.getPhoneNumber());
                }

                return existingCustomerMiMi;
            })
            .map(customerMiMiRepository::save);
    }

    /**
     * Get one customerMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerMiMi> findOne(Long id) {
        LOG.debug("Request to get CustomerMiMi : {}", id);
        return customerMiMiRepository.findById(id);
    }

    /**
     * Delete the customerMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerMiMi : {}", id);
        customerMiMiRepository.deleteById(id);
    }
}
