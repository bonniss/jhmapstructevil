package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeMiMi;
import xyz.jhmapstruct.repository.EmployeeMiMiRepository;
import xyz.jhmapstruct.service.EmployeeMiMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeMiMi}.
 */
@Service
@Transactional
public class EmployeeMiMiServiceImpl implements EmployeeMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeMiMiServiceImpl.class);

    private final EmployeeMiMiRepository employeeMiMiRepository;

    public EmployeeMiMiServiceImpl(EmployeeMiMiRepository employeeMiMiRepository) {
        this.employeeMiMiRepository = employeeMiMiRepository;
    }

    @Override
    public EmployeeMiMi save(EmployeeMiMi employeeMiMi) {
        LOG.debug("Request to save EmployeeMiMi : {}", employeeMiMi);
        return employeeMiMiRepository.save(employeeMiMi);
    }

    @Override
    public EmployeeMiMi update(EmployeeMiMi employeeMiMi) {
        LOG.debug("Request to update EmployeeMiMi : {}", employeeMiMi);
        return employeeMiMiRepository.save(employeeMiMi);
    }

    @Override
    public Optional<EmployeeMiMi> partialUpdate(EmployeeMiMi employeeMiMi) {
        LOG.debug("Request to partially update EmployeeMiMi : {}", employeeMiMi);

        return employeeMiMiRepository
            .findById(employeeMiMi.getId())
            .map(existingEmployeeMiMi -> {
                if (employeeMiMi.getFirstName() != null) {
                    existingEmployeeMiMi.setFirstName(employeeMiMi.getFirstName());
                }
                if (employeeMiMi.getLastName() != null) {
                    existingEmployeeMiMi.setLastName(employeeMiMi.getLastName());
                }
                if (employeeMiMi.getEmail() != null) {
                    existingEmployeeMiMi.setEmail(employeeMiMi.getEmail());
                }
                if (employeeMiMi.getHireDate() != null) {
                    existingEmployeeMiMi.setHireDate(employeeMiMi.getHireDate());
                }
                if (employeeMiMi.getPosition() != null) {
                    existingEmployeeMiMi.setPosition(employeeMiMi.getPosition());
                }

                return existingEmployeeMiMi;
            })
            .map(employeeMiMiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeMiMi> findAll() {
        LOG.debug("Request to get all EmployeeMiMis");
        return employeeMiMiRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeMiMi> findOne(Long id) {
        LOG.debug("Request to get EmployeeMiMi : {}", id);
        return employeeMiMiRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeMiMi : {}", id);
        employeeMiMiRepository.deleteById(id);
    }
}
