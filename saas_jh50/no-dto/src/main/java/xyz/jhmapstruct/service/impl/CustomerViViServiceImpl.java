package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerViVi;
import xyz.jhmapstruct.repository.CustomerViViRepository;
import xyz.jhmapstruct.service.CustomerViViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerViVi}.
 */
@Service
@Transactional
public class CustomerViViServiceImpl implements CustomerViViService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerViViServiceImpl.class);

    private final CustomerViViRepository customerViViRepository;

    public CustomerViViServiceImpl(CustomerViViRepository customerViViRepository) {
        this.customerViViRepository = customerViViRepository;
    }

    @Override
    public CustomerViVi save(CustomerViVi customerViVi) {
        LOG.debug("Request to save CustomerViVi : {}", customerViVi);
        return customerViViRepository.save(customerViVi);
    }

    @Override
    public CustomerViVi update(CustomerViVi customerViVi) {
        LOG.debug("Request to update CustomerViVi : {}", customerViVi);
        return customerViViRepository.save(customerViVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<CustomerViVi> findAll() {
        LOG.debug("Request to get all CustomerViVis");
        return customerViViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerViVi> findOne(Long id) {
        LOG.debug("Request to get CustomerViVi : {}", id);
        return customerViViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerViVi : {}", id);
        customerViViRepository.deleteById(id);
    }
}
