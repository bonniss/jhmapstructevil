package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerVi;
import xyz.jhmapstruct.repository.CustomerViRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerVi}.
 */
@Service
@Transactional
public class CustomerViService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerViService.class);

    private final CustomerViRepository customerViRepository;

    public CustomerViService(CustomerViRepository customerViRepository) {
        this.customerViRepository = customerViRepository;
    }

    /**
     * Save a customerVi.
     *
     * @param customerVi the entity to save.
     * @return the persisted entity.
     */
    public CustomerVi save(CustomerVi customerVi) {
        LOG.debug("Request to save CustomerVi : {}", customerVi);
        return customerViRepository.save(customerVi);
    }

    /**
     * Update a customerVi.
     *
     * @param customerVi the entity to save.
     * @return the persisted entity.
     */
    public CustomerVi update(CustomerVi customerVi) {
        LOG.debug("Request to update CustomerVi : {}", customerVi);
        return customerViRepository.save(customerVi);
    }

    /**
     * Partially update a customerVi.
     *
     * @param customerVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerVi> partialUpdate(CustomerVi customerVi) {
        LOG.debug("Request to partially update CustomerVi : {}", customerVi);

        return customerViRepository
            .findById(customerVi.getId())
            .map(existingCustomerVi -> {
                if (customerVi.getFirstName() != null) {
                    existingCustomerVi.setFirstName(customerVi.getFirstName());
                }
                if (customerVi.getLastName() != null) {
                    existingCustomerVi.setLastName(customerVi.getLastName());
                }
                if (customerVi.getEmail() != null) {
                    existingCustomerVi.setEmail(customerVi.getEmail());
                }
                if (customerVi.getPhoneNumber() != null) {
                    existingCustomerVi.setPhoneNumber(customerVi.getPhoneNumber());
                }

                return existingCustomerVi;
            })
            .map(customerViRepository::save);
    }

    /**
     * Get one customerVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerVi> findOne(Long id) {
        LOG.debug("Request to get CustomerVi : {}", id);
        return customerViRepository.findById(id);
    }

    /**
     * Delete the customerVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerVi : {}", id);
        customerViRepository.deleteById(id);
    }
}
