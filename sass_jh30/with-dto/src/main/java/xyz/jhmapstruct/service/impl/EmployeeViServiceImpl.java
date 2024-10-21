package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeVi;
import xyz.jhmapstruct.repository.EmployeeViRepository;
import xyz.jhmapstruct.service.EmployeeViService;
import xyz.jhmapstruct.service.dto.EmployeeViDTO;
import xyz.jhmapstruct.service.mapper.EmployeeViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeVi}.
 */
@Service
@Transactional
public class EmployeeViServiceImpl implements EmployeeViService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViServiceImpl.class);

    private final EmployeeViRepository employeeViRepository;

    private final EmployeeViMapper employeeViMapper;

    public EmployeeViServiceImpl(EmployeeViRepository employeeViRepository, EmployeeViMapper employeeViMapper) {
        this.employeeViRepository = employeeViRepository;
        this.employeeViMapper = employeeViMapper;
    }

    @Override
    public EmployeeViDTO save(EmployeeViDTO employeeViDTO) {
        LOG.debug("Request to save EmployeeVi : {}", employeeViDTO);
        EmployeeVi employeeVi = employeeViMapper.toEntity(employeeViDTO);
        employeeVi = employeeViRepository.save(employeeVi);
        return employeeViMapper.toDto(employeeVi);
    }

    @Override
    public EmployeeViDTO update(EmployeeViDTO employeeViDTO) {
        LOG.debug("Request to update EmployeeVi : {}", employeeViDTO);
        EmployeeVi employeeVi = employeeViMapper.toEntity(employeeViDTO);
        employeeVi = employeeViRepository.save(employeeVi);
        return employeeViMapper.toDto(employeeVi);
    }

    @Override
    public Optional<EmployeeViDTO> partialUpdate(EmployeeViDTO employeeViDTO) {
        LOG.debug("Request to partially update EmployeeVi : {}", employeeViDTO);

        return employeeViRepository
            .findById(employeeViDTO.getId())
            .map(existingEmployeeVi -> {
                employeeViMapper.partialUpdate(existingEmployeeVi, employeeViDTO);

                return existingEmployeeVi;
            })
            .map(employeeViRepository::save)
            .map(employeeViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeViDTO> findAll() {
        LOG.debug("Request to get all EmployeeVis");
        return employeeViRepository.findAll().stream().map(employeeViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeViDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeVi : {}", id);
        return employeeViRepository.findById(id).map(employeeViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeVi : {}", id);
        employeeViRepository.deleteById(id);
    }
}
