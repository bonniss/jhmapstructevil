package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeMiMi;
import xyz.jhmapstruct.repository.EmployeeMiMiRepository;
import xyz.jhmapstruct.service.EmployeeMiMiService;
import xyz.jhmapstruct.service.dto.EmployeeMiMiDTO;
import xyz.jhmapstruct.service.mapper.EmployeeMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeMiMi}.
 */
@Service
@Transactional
public class EmployeeMiMiServiceImpl implements EmployeeMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeMiMiServiceImpl.class);

    private final EmployeeMiMiRepository employeeMiMiRepository;

    private final EmployeeMiMiMapper employeeMiMiMapper;

    public EmployeeMiMiServiceImpl(EmployeeMiMiRepository employeeMiMiRepository, EmployeeMiMiMapper employeeMiMiMapper) {
        this.employeeMiMiRepository = employeeMiMiRepository;
        this.employeeMiMiMapper = employeeMiMiMapper;
    }

    @Override
    public EmployeeMiMiDTO save(EmployeeMiMiDTO employeeMiMiDTO) {
        LOG.debug("Request to save EmployeeMiMi : {}", employeeMiMiDTO);
        EmployeeMiMi employeeMiMi = employeeMiMiMapper.toEntity(employeeMiMiDTO);
        employeeMiMi = employeeMiMiRepository.save(employeeMiMi);
        return employeeMiMiMapper.toDto(employeeMiMi);
    }

    @Override
    public EmployeeMiMiDTO update(EmployeeMiMiDTO employeeMiMiDTO) {
        LOG.debug("Request to update EmployeeMiMi : {}", employeeMiMiDTO);
        EmployeeMiMi employeeMiMi = employeeMiMiMapper.toEntity(employeeMiMiDTO);
        employeeMiMi = employeeMiMiRepository.save(employeeMiMi);
        return employeeMiMiMapper.toDto(employeeMiMi);
    }

    @Override
    public Optional<EmployeeMiMiDTO> partialUpdate(EmployeeMiMiDTO employeeMiMiDTO) {
        LOG.debug("Request to partially update EmployeeMiMi : {}", employeeMiMiDTO);

        return employeeMiMiRepository
            .findById(employeeMiMiDTO.getId())
            .map(existingEmployeeMiMi -> {
                employeeMiMiMapper.partialUpdate(existingEmployeeMiMi, employeeMiMiDTO);

                return existingEmployeeMiMi;
            })
            .map(employeeMiMiRepository::save)
            .map(employeeMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeMiMiDTO> findAll() {
        LOG.debug("Request to get all EmployeeMiMis");
        return employeeMiMiRepository.findAll().stream().map(employeeMiMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeMiMi : {}", id);
        return employeeMiMiRepository.findById(id).map(employeeMiMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeMiMi : {}", id);
        employeeMiMiRepository.deleteById(id);
    }
}
