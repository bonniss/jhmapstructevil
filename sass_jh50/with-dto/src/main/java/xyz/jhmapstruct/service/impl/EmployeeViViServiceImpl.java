package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeViVi;
import xyz.jhmapstruct.repository.EmployeeViViRepository;
import xyz.jhmapstruct.service.EmployeeViViService;
import xyz.jhmapstruct.service.dto.EmployeeViViDTO;
import xyz.jhmapstruct.service.mapper.EmployeeViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeViVi}.
 */
@Service
@Transactional
public class EmployeeViViServiceImpl implements EmployeeViViService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViViServiceImpl.class);

    private final EmployeeViViRepository employeeViViRepository;

    private final EmployeeViViMapper employeeViViMapper;

    public EmployeeViViServiceImpl(EmployeeViViRepository employeeViViRepository, EmployeeViViMapper employeeViViMapper) {
        this.employeeViViRepository = employeeViViRepository;
        this.employeeViViMapper = employeeViViMapper;
    }

    @Override
    public EmployeeViViDTO save(EmployeeViViDTO employeeViViDTO) {
        LOG.debug("Request to save EmployeeViVi : {}", employeeViViDTO);
        EmployeeViVi employeeViVi = employeeViViMapper.toEntity(employeeViViDTO);
        employeeViVi = employeeViViRepository.save(employeeViVi);
        return employeeViViMapper.toDto(employeeViVi);
    }

    @Override
    public EmployeeViViDTO update(EmployeeViViDTO employeeViViDTO) {
        LOG.debug("Request to update EmployeeViVi : {}", employeeViViDTO);
        EmployeeViVi employeeViVi = employeeViViMapper.toEntity(employeeViViDTO);
        employeeViVi = employeeViViRepository.save(employeeViVi);
        return employeeViViMapper.toDto(employeeViVi);
    }

    @Override
    public Optional<EmployeeViViDTO> partialUpdate(EmployeeViViDTO employeeViViDTO) {
        LOG.debug("Request to partially update EmployeeViVi : {}", employeeViViDTO);

        return employeeViViRepository
            .findById(employeeViViDTO.getId())
            .map(existingEmployeeViVi -> {
                employeeViViMapper.partialUpdate(existingEmployeeViVi, employeeViViDTO);

                return existingEmployeeViVi;
            })
            .map(employeeViViRepository::save)
            .map(employeeViViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeViViDTO> findAll() {
        LOG.debug("Request to get all EmployeeViVis");
        return employeeViViRepository.findAll().stream().map(employeeViViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeViViDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeViVi : {}", id);
        return employeeViViRepository.findById(id).map(employeeViViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeViVi : {}", id);
        employeeViViRepository.deleteById(id);
    }
}
