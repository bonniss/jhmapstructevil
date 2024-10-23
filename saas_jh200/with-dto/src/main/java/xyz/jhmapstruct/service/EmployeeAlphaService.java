package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeAlpha;
import xyz.jhmapstruct.repository.EmployeeAlphaRepository;
import xyz.jhmapstruct.service.dto.EmployeeAlphaDTO;
import xyz.jhmapstruct.service.mapper.EmployeeAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeAlpha}.
 */
@Service
@Transactional
public class EmployeeAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeAlphaService.class);

    private final EmployeeAlphaRepository employeeAlphaRepository;

    private final EmployeeAlphaMapper employeeAlphaMapper;

    public EmployeeAlphaService(EmployeeAlphaRepository employeeAlphaRepository, EmployeeAlphaMapper employeeAlphaMapper) {
        this.employeeAlphaRepository = employeeAlphaRepository;
        this.employeeAlphaMapper = employeeAlphaMapper;
    }

    /**
     * Save a employeeAlpha.
     *
     * @param employeeAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeAlphaDTO save(EmployeeAlphaDTO employeeAlphaDTO) {
        LOG.debug("Request to save EmployeeAlpha : {}", employeeAlphaDTO);
        EmployeeAlpha employeeAlpha = employeeAlphaMapper.toEntity(employeeAlphaDTO);
        employeeAlpha = employeeAlphaRepository.save(employeeAlpha);
        return employeeAlphaMapper.toDto(employeeAlpha);
    }

    /**
     * Update a employeeAlpha.
     *
     * @param employeeAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeAlphaDTO update(EmployeeAlphaDTO employeeAlphaDTO) {
        LOG.debug("Request to update EmployeeAlpha : {}", employeeAlphaDTO);
        EmployeeAlpha employeeAlpha = employeeAlphaMapper.toEntity(employeeAlphaDTO);
        employeeAlpha = employeeAlphaRepository.save(employeeAlpha);
        return employeeAlphaMapper.toDto(employeeAlpha);
    }

    /**
     * Partially update a employeeAlpha.
     *
     * @param employeeAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeAlphaDTO> partialUpdate(EmployeeAlphaDTO employeeAlphaDTO) {
        LOG.debug("Request to partially update EmployeeAlpha : {}", employeeAlphaDTO);

        return employeeAlphaRepository
            .findById(employeeAlphaDTO.getId())
            .map(existingEmployeeAlpha -> {
                employeeAlphaMapper.partialUpdate(existingEmployeeAlpha, employeeAlphaDTO);

                return existingEmployeeAlpha;
            })
            .map(employeeAlphaRepository::save)
            .map(employeeAlphaMapper::toDto);
    }

    /**
     * Get one employeeAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeAlpha : {}", id);
        return employeeAlphaRepository.findById(id).map(employeeAlphaMapper::toDto);
    }

    /**
     * Delete the employeeAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeAlpha : {}", id);
        employeeAlphaRepository.deleteById(id);
    }
}
