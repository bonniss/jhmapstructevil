package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeMi;
import xyz.jhmapstruct.repository.EmployeeMiRepository;
import xyz.jhmapstruct.service.dto.EmployeeMiDTO;
import xyz.jhmapstruct.service.mapper.EmployeeMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeMi}.
 */
@Service
@Transactional
public class EmployeeMiService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeMiService.class);

    private final EmployeeMiRepository employeeMiRepository;

    private final EmployeeMiMapper employeeMiMapper;

    public EmployeeMiService(EmployeeMiRepository employeeMiRepository, EmployeeMiMapper employeeMiMapper) {
        this.employeeMiRepository = employeeMiRepository;
        this.employeeMiMapper = employeeMiMapper;
    }

    /**
     * Save a employeeMi.
     *
     * @param employeeMiDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeMiDTO save(EmployeeMiDTO employeeMiDTO) {
        LOG.debug("Request to save EmployeeMi : {}", employeeMiDTO);
        EmployeeMi employeeMi = employeeMiMapper.toEntity(employeeMiDTO);
        employeeMi = employeeMiRepository.save(employeeMi);
        return employeeMiMapper.toDto(employeeMi);
    }

    /**
     * Update a employeeMi.
     *
     * @param employeeMiDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeMiDTO update(EmployeeMiDTO employeeMiDTO) {
        LOG.debug("Request to update EmployeeMi : {}", employeeMiDTO);
        EmployeeMi employeeMi = employeeMiMapper.toEntity(employeeMiDTO);
        employeeMi = employeeMiRepository.save(employeeMi);
        return employeeMiMapper.toDto(employeeMi);
    }

    /**
     * Partially update a employeeMi.
     *
     * @param employeeMiDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one employeeMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeMiDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeMi : {}", id);
        return employeeMiRepository.findById(id).map(employeeMiMapper::toDto);
    }

    /**
     * Delete the employeeMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeMi : {}", id);
        employeeMiRepository.deleteById(id);
    }
}
