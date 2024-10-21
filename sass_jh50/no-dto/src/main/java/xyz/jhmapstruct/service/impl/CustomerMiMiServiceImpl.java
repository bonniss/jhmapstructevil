package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerMiMi;
import xyz.jhmapstruct.repository.CustomerMiMiRepository;
import xyz.jhmapstruct.service.CustomerMiMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerMiMi}.
 */
@Service
@Transactional
public class CustomerMiMiServiceImpl implements CustomerMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMiMiServiceImpl.class);

    private final CustomerMiMiRepository customerMiMiRepository;

    public CustomerMiMiServiceImpl(CustomerMiMiRepository customerMiMiRepository) {
        this.customerMiMiRepository = customerMiMiRepository;
    }

    @Override
    public CustomerMiMi save(CustomerMiMi customerMiMi) {
        LOG.debug("Request to save CustomerMiMi : {}", customerMiMi);
        return customerMiMiRepository.save(customerMiMi);
    }

    @Override
    public CustomerMiMi update(CustomerMiMi customerMiMi) {
        LOG.debug("Request to update CustomerMiMi : {}", customerMiMi);
        return customerMiMiRepository.save(customerMiMi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<CustomerMiMi> findAll() {
        LOG.debug("Request to get all CustomerMiMis");
        return customerMiMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerMiMi> findOne(Long id) {
        LOG.debug("Request to get CustomerMiMi : {}", id);
        return customerMiMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerMiMi : {}", id);
        customerMiMiRepository.deleteById(id);
    }
}
