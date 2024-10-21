package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeMi;
import xyz.jhmapstruct.repository.EmployeeMiRepository;
import xyz.jhmapstruct.service.EmployeeMiService;
import xyz.jhmapstruct.service.dto.EmployeeMiDTO;
import xyz.jhmapstruct.service.mapper.EmployeeMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeMi}.
 */
@Service
@Transactional
public class EmployeeMiServiceImpl implements EmployeeMiService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeMiServiceImpl.class);

    private final EmployeeMiRepository employeeMiRepository;

    private final EmployeeMiMapper employeeMiMapper;

    public EmployeeMiServiceImpl(EmployeeMiRepository employeeMiRepository, EmployeeMiMapper employeeMiMapper) {
        this.employeeMiRepository = employeeMiRepository;
        this.employeeMiMapper = employeeMiMapper;
    }

    @Override
    public EmployeeMiDTO save(EmployeeMiDTO employeeMiDTO) {
        LOG.debug("Request to save EmployeeMi : {}", employeeMiDTO);
        EmployeeMi employeeMi = employeeMiMapper.toEntity(employeeMiDTO);
        employeeMi = employeeMiRepository.save(employeeMi);
        return employeeMiMapper.toDto(employeeMi);
    }

    @Override
    public EmployeeMiDTO update(EmployeeMiDTO employeeMiDTO) {
        LOG.debug("Request to update EmployeeMi : {}", employeeMiDTO);
        EmployeeMi employeeMi = employeeMiMapper.toEntity(employeeMiDTO);
        employeeMi = employeeMiRepository.save(employeeMi);
        return employeeMiMapper.toDto(employeeMi);
    }

    @Override
    public Optional<EmployeeMiDTO> partialUpdate(EmployeeMiDTO employeeMiDTO) {
        LOG.debug("Request to partially update EmployeeMi : {}", employeeMiDTO);

        return employeeMiRepository
            .findById(employeeMiDTO.getId())
            .map(existingEmployeeMi -> {
                employeeMiMapper.partialUpdate(existingEmployeeMi, employeeMiDTO);

                return existingEmployeeMi;
            })
            .map(employeeMiRepository::save)
            .map(employeeMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeMiDTO> findAll() {
        LOG.debug("Request to get all EmployeeMis");
        return employeeMiRepository.findAll().stream().map(employeeMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeMiDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeMi : {}", id);
        return employeeMiRepository.findById(id).map(employeeMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeMi : {}", id);
        employeeMiRepository.deleteById(id);
    }
}
