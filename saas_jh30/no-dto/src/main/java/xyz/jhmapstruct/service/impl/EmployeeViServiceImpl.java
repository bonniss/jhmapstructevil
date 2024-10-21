package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeVi;
import xyz.jhmapstruct.repository.EmployeeViRepository;
import xyz.jhmapstruct.service.EmployeeViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeVi}.
 */
@Service
@Transactional
public class EmployeeViServiceImpl implements EmployeeViService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViServiceImpl.class);

    private final EmployeeViRepository employeeViRepository;

    public EmployeeViServiceImpl(EmployeeViRepository employeeViRepository) {
        this.employeeViRepository = employeeViRepository;
    }

    @Override
    public EmployeeVi save(EmployeeVi employeeVi) {
        LOG.debug("Request to save EmployeeVi : {}", employeeVi);
        return employeeViRepository.save(employeeVi);
    }

    @Override
    public EmployeeVi update(EmployeeVi employeeVi) {
        LOG.debug("Request to update EmployeeVi : {}", employeeVi);
        return employeeViRepository.save(employeeVi);
    }

    @Override
    public Optional<EmployeeVi> partialUpdate(EmployeeVi employeeVi) {
        LOG.debug("Request to partially update EmployeeVi : {}", employeeVi);

        return employeeViRepository
            .findById(employeeVi.getId())
            .map(existingEmployeeVi -> {
                if (employeeVi.getFirstName() != null) {
                    existingEmployeeVi.setFirstName(employeeVi.getFirstName());
                }
                if (employeeVi.getLastName() != null) {
                    existingEmployeeVi.setLastName(employeeVi.getLastName());
                }
                if (employeeVi.getEmail() != null) {
                    existingEmployeeVi.setEmail(employeeVi.getEmail());
                }
                if (employeeVi.getHireDate() != null) {
                    existingEmployeeVi.setHireDate(employeeVi.getHireDate());
                }
                if (employeeVi.getPosition() != null) {
                    existingEmployeeVi.setPosition(employeeVi.getPosition());
                }

                return existingEmployeeVi;
            })
            .map(employeeViRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeVi> findAll() {
        LOG.debug("Request to get all EmployeeVis");
        return employeeViRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeVi> findOne(Long id) {
        LOG.debug("Request to get EmployeeVi : {}", id);
        return employeeViRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeVi : {}", id);
        employeeViRepository.deleteById(id);
    }
}
