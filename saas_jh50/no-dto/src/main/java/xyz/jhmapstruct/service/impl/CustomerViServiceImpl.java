package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.CustomerVi;
import xyz.jhmapstruct.repository.CustomerViRepository;
import xyz.jhmapstruct.service.CustomerViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.CustomerVi}.
 */
@Service
@Transactional
public class CustomerViServiceImpl implements CustomerViService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerViServiceImpl.class);

    private final CustomerViRepository customerViRepository;

    public CustomerViServiceImpl(CustomerViRepository customerViRepository) {
        this.customerViRepository = customerViRepository;
    }

    @Override
    public CustomerVi save(CustomerVi customerVi) {
        LOG.debug("Request to save CustomerVi : {}", customerVi);
        return customerViRepository.save(customerVi);
    }

    @Override
    public CustomerVi update(CustomerVi customerVi) {
        LOG.debug("Request to update CustomerVi : {}", customerVi);
        return customerViRepository.save(customerVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<CustomerVi> findAll() {
        LOG.debug("Request to get all CustomerVis");
        return customerViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerVi> findOne(Long id) {
        LOG.debug("Request to get CustomerVi : {}", id);
        return customerViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerVi : {}", id);
        customerViRepository.deleteById(id);
    }
}
