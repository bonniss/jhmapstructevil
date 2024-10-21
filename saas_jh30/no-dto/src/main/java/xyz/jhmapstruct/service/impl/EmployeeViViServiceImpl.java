package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeViVi;
import xyz.jhmapstruct.repository.EmployeeViViRepository;
import xyz.jhmapstruct.service.EmployeeViViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeViVi}.
 */
@Service
@Transactional
public class EmployeeViViServiceImpl implements EmployeeViViService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViViServiceImpl.class);

    private final EmployeeViViRepository employeeViViRepository;

    public EmployeeViViServiceImpl(EmployeeViViRepository employeeViViRepository) {
        this.employeeViViRepository = employeeViViRepository;
    }

    @Override
    public EmployeeViVi save(EmployeeViVi employeeViVi) {
        LOG.debug("Request to save EmployeeViVi : {}", employeeViVi);
        return employeeViViRepository.save(employeeViVi);
    }

    @Override
    public EmployeeViVi update(EmployeeViVi employeeViVi) {
        LOG.debug("Request to update EmployeeViVi : {}", employeeViVi);
        return employeeViViRepository.save(employeeViVi);
    }

    @Override
    public Optional<EmployeeViVi> partialUpdate(EmployeeViVi employeeViVi) {
        LOG.debug("Request to partially update EmployeeViVi : {}", employeeViVi);

        return employeeViViRepository
            .findById(employeeViVi.getId())
            .map(existingEmployeeViVi -> {
                if (employeeViVi.getFirstName() != null) {
                    existingEmployeeViVi.setFirstName(employeeViVi.getFirstName());
                }
                if (employeeViVi.getLastName() != null) {
                    existingEmployeeViVi.setLastName(employeeViVi.getLastName());
                }
                if (employeeViVi.getEmail() != null) {
                    existingEmployeeViVi.setEmail(employeeViVi.getEmail());
                }
                if (employeeViVi.getHireDate() != null) {
                    existingEmployeeViVi.setHireDate(employeeViVi.getHireDate());
                }
                if (employeeViVi.getPosition() != null) {
                    existingEmployeeViVi.setPosition(employeeViVi.getPosition());
                }

                return existingEmployeeViVi;
            })
            .map(employeeViViRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeViVi> findAll() {
        LOG.debug("Request to get all EmployeeViVis");
        return employeeViViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeViVi> findOne(Long id) {
        LOG.debug("Request to get EmployeeViVi : {}", id);
        return employeeViViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeViVi : {}", id);
        employeeViViRepository.deleteById(id);
    }
}
