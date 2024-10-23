package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeMiMi;
import xyz.jhmapstruct.repository.EmployeeMiMiRepository;
import xyz.jhmapstruct.service.dto.EmployeeMiMiDTO;
import xyz.jhmapstruct.service.mapper.EmployeeMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeMiMi}.
 */
@Service
@Transactional
public class EmployeeMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeMiMiService.class);

    private final EmployeeMiMiRepository employeeMiMiRepository;

    private final EmployeeMiMiMapper employeeMiMiMapper;

    public EmployeeMiMiService(EmployeeMiMiRepository employeeMiMiRepository, EmployeeMiMiMapper employeeMiMiMapper) {
        this.employeeMiMiRepository = employeeMiMiRepository;
        this.employeeMiMiMapper = employeeMiMiMapper;
    }

    /**
     * Save a employeeMiMi.
     *
     * @param employeeMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeMiMiDTO save(EmployeeMiMiDTO employeeMiMiDTO) {
        LOG.debug("Request to save EmployeeMiMi : {}", employeeMiMiDTO);
        EmployeeMiMi employeeMiMi = employeeMiMiMapper.toEntity(employeeMiMiDTO);
        employeeMiMi = employeeMiMiRepository.save(employeeMiMi);
        return employeeMiMiMapper.toDto(employeeMiMi);
    }

    /**
     * Update a employeeMiMi.
     *
     * @param employeeMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeMiMiDTO update(EmployeeMiMiDTO employeeMiMiDTO) {
        LOG.debug("Request to update EmployeeMiMi : {}", employeeMiMiDTO);
        EmployeeMiMi employeeMiMi = employeeMiMiMapper.toEntity(employeeMiMiDTO);
        employeeMiMi = employeeMiMiRepository.save(employeeMiMi);
        return employeeMiMiMapper.toDto(employeeMiMi);
    }

    /**
     * Partially update a employeeMiMi.
     *
     * @param employeeMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
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

    /**
     * Get one employeeMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeMiMi : {}", id);
        return employeeMiMiRepository.findById(id).map(employeeMiMiMapper::toDto);
    }

    /**
     * Delete the employeeMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeMiMi : {}", id);
        employeeMiMiRepository.deleteById(id);
    }
}
