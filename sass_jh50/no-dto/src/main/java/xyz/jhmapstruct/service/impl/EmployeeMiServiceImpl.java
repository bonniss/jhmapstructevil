package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeMi;
import xyz.jhmapstruct.repository.EmployeeMiRepository;
import xyz.jhmapstruct.service.EmployeeMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeMi}.
 */
@Service
@Transactional
public class EmployeeMiServiceImpl implements EmployeeMiService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeMiServiceImpl.class);

    private final EmployeeMiRepository employeeMiRepository;

    public EmployeeMiServiceImpl(EmployeeMiRepository employeeMiRepository) {
        this.employeeMiRepository = employeeMiRepository;
    }

    @Override
    public EmployeeMi save(EmployeeMi employeeMi) {
        LOG.debug("Request to save EmployeeMi : {}", employeeMi);
        return employeeMiRepository.save(employeeMi);
    }

    @Override
    public EmployeeMi update(EmployeeMi employeeMi) {
        LOG.debug("Request to update EmployeeMi : {}", employeeMi);
        return employeeMiRepository.save(employeeMi);
    }

    @Override
    public Optional<EmployeeMi> partialUpdate(EmployeeMi employeeMi) {
        LOG.debug("Request to partially update EmployeeMi : {}", employeeMi);

        return employeeMiRepository
            .findById(employeeMi.getId())
            .map(existingEmployeeMi -> {
                if (employeeMi.getFirstName() != null) {
                    existingEmployeeMi.setFirstName(employeeMi.getFirstName());
                }
                if (employeeMi.getLastName() != null) {
                    existingEmployeeMi.setLastName(employeeMi.getLastName());
                }
                if (employeeMi.getEmail() != null) {
                    existingEmployeeMi.setEmail(employeeMi.getEmail());
                }
                if (employeeMi.getHireDate() != null) {
                    existingEmployeeMi.setHireDate(employeeMi.getHireDate());
                }
                if (employeeMi.getPosition() != null) {
                    existingEmployeeMi.setPosition(employeeMi.getPosition());
                }

                return existingEmployeeMi;
            })
            .map(employeeMiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeMi> findAll() {
        LOG.debug("Request to get all EmployeeMis");
        return employeeMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeMi> findOne(Long id) {
        LOG.debug("Request to get EmployeeMi : {}", id);
        return employeeMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeMi : {}", id);
        employeeMiRepository.deleteById(id);
    }
}
