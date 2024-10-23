package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerViVi;
import xyz.jhmapstruct.repository.CustomerViViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerViVi}.
 */
@Service
@Transactional
public class CustomerViViService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerViViService.class);

    private final CustomerViViRepository customerViViRepository;

    public CustomerViViService(CustomerViViRepository customerViViRepository) {
        this.customerViViRepository = customerViViRepository;
    }

    /**
     * Save a customerViVi.
     *
     * @param customerViVi the entity to save.
     * @return the persisted entity.
     */
    public CustomerViVi save(CustomerViVi customerViVi) {
        LOG.debug("Request to save CustomerViVi : {}", customerViVi);
        return customerViViRepository.save(customerViVi);
    }

    /**
     * Update a customerViVi.
     *
     * @param customerViVi the entity to save.
     * @return the persisted entity.
     */
    public CustomerViVi update(CustomerViVi customerViVi) {
        LOG.debug("Request to update CustomerViVi : {}", customerViVi);
        return customerViViRepository.save(customerViVi);
    }

    /**
     * Partially update a customerViVi.
     *
     * @param customerViVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerViVi> partialUpdate(CustomerViVi customerViVi) {
        LOG.debug("Request to partially update CustomerViVi : {}", customerViVi);

        return customerViViRepository
            .findById(customerViVi.getId())
            .map(existingCustomerViVi -> {
                if (customerViVi.getFirstName() != null) {
                    existingCustomerViVi.setFirstName(customerViVi.getFirstName());
                }
                if (customerViVi.getLastName() != null) {
                    existingCustomerViVi.setLastName(customerViVi.getLastName());
                }
                if (customerViVi.getEmail() != null) {
                    existingCustomerViVi.setEmail(customerViVi.getEmail());
                }
                if (customerViVi.getPhoneNumber() != null) {
                    existingCustomerViVi.setPhoneNumber(customerViVi.getPhoneNumber());
                }

                return existingCustomerViVi;
            })
            .map(customerViViRepository::save);
    }

    /**
     * Get one customerViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerViVi> findOne(Long id) {
        LOG.debug("Request to get CustomerViVi : {}", id);
        return customerViViRepository.findById(id);
    }

    /**
     * Delete the customerViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerViVi : {}", id);
        customerViViRepository.deleteById(id);
    }
}
