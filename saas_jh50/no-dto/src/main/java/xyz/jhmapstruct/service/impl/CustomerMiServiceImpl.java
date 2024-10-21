package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerMi;
import xyz.jhmapstruct.repository.CustomerMiRepository;
import xyz.jhmapstruct.service.CustomerMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerMi}.
 */
@Service
@Transactional
public class CustomerMiServiceImpl implements CustomerMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMiServiceImpl.class);

    private final CustomerMiRepository customerMiRepository;

    public CustomerMiServiceImpl(CustomerMiRepository customerMiRepository) {
        this.customerMiRepository = customerMiRepository;
    }

    @Override
    public CustomerMi save(CustomerMi customerMi) {
        LOG.debug("Request to save CustomerMi : {}", customerMi);
        return customerMiRepository.save(customerMi);
    }

    @Override
    public CustomerMi update(CustomerMi customerMi) {
        LOG.debug("Request to update CustomerMi : {}", customerMi);
        return customerMiRepository.save(customerMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<CustomerMi> findAll() {
        LOG.debug("Request to get all CustomerMis");
        return customerMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerMi> findOne(Long id) {
        LOG.debug("Request to get CustomerMi : {}", id);
        return customerMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerMi : {}", id);
        customerMiRepository.deleteById(id);
    }
}
