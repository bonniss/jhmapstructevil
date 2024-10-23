package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerSigma;
import xyz.jhmapstruct.repository.CustomerSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerSigma}.
 */
@Service
@Transactional
public class CustomerSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerSigmaService.class);

    private final CustomerSigmaRepository customerSigmaRepository;

    public CustomerSigmaService(CustomerSigmaRepository customerSigmaRepository) {
        this.customerSigmaRepository = customerSigmaRepository;
    }

    /**
     * Save a customerSigma.
     *
     * @param customerSigma the entity to save.
     * @return the persisted entity.
     */
    public CustomerSigma save(CustomerSigma customerSigma) {
        LOG.debug("Request to save CustomerSigma : {}", customerSigma);
        return customerSigmaRepository.save(customerSigma);
    }

    /**
     * Update a customerSigma.
     *
     * @param customerSigma the entity to save.
     * @return the persisted entity.
     */
    public CustomerSigma update(CustomerSigma customerSigma) {
        LOG.debug("Request to update CustomerSigma : {}", customerSigma);
        return customerSigmaRepository.save(customerSigma);
    }

    /**
     * Partially update a customerSigma.
     *
     * @param customerSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerSigma> partialUpdate(CustomerSigma customerSigma) {
        LOG.debug("Request to partially update CustomerSigma : {}", customerSigma);

        return customerSigmaRepository
            .findById(customerSigma.getId())
            .map(existingCustomerSigma -> {
                if (customerSigma.getFirstName() != null) {
                    existingCustomerSigma.setFirstName(customerSigma.getFirstName());
                }
                if (customerSigma.getLastName() != null) {
                    existingCustomerSigma.setLastName(customerSigma.getLastName());
                }
                if (customerSigma.getEmail() != null) {
                    existingCustomerSigma.setEmail(customerSigma.getEmail());
                }
                if (customerSigma.getPhoneNumber() != null) {
                    existingCustomerSigma.setPhoneNumber(customerSigma.getPhoneNumber());
                }

                return existingCustomerSigma;
            })
            .map(customerSigmaRepository::save);
    }

    /**
     * Get one customerSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerSigma> findOne(Long id) {
        LOG.debug("Request to get CustomerSigma : {}", id);
        return customerSigmaRepository.findById(id);
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
