package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeGamma;
import xyz.jhmapstruct.repository.EmployeeGammaRepository;
import xyz.jhmapstruct.service.dto.EmployeeGammaDTO;
import xyz.jhmapstruct.service.mapper.EmployeeGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeGamma}.
 */
@Service
@Transactional
public class EmployeeGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeGammaService.class);

    private final EmployeeGammaRepository employeeGammaRepository;

    private final EmployeeGammaMapper employeeGammaMapper;

    public EmployeeGammaService(EmployeeGammaRepository employeeGammaRepository, EmployeeGammaMapper employeeGammaMapper) {
        this.employeeGammaRepository = employeeGammaRepository;
        this.employeeGammaMapper = employeeGammaMapper;
    }

    /**
     * Save a employeeGamma.
     *
     * @param employeeGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeGammaDTO save(EmployeeGammaDTO employeeGammaDTO) {
        LOG.debug("Request to save EmployeeGamma : {}", employeeGammaDTO);
        EmployeeGamma employeeGamma = employeeGammaMapper.toEntity(employeeGammaDTO);
        employeeGamma = employeeGammaRepository.save(employeeGamma);
        return employeeGammaMapper.toDto(employeeGamma);
    }

    /**
     * Update a employeeGamma.
     *
     * @param employeeGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeGammaDTO update(EmployeeGammaDTO employeeGammaDTO) {
        LOG.debug("Request to update EmployeeGamma : {}", employeeGammaDTO);
        EmployeeGamma employeeGamma = employeeGammaMapper.toEntity(employeeGammaDTO);
        employeeGamma = employeeGammaRepository.save(employeeGamma);
        return employeeGammaMapper.toDto(employeeGamma);
    }

    /**
     * Partially update a employeeGamma.
     *
     * @param employeeGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeGammaDTO> partialUpdate(EmployeeGammaDTO employeeGammaDTO) {
        LOG.debug("Request to partially update EmployeeGamma : {}", employeeGammaDTO);

        return employeeGammaRepository
            .findById(employeeGammaDTO.getId())
            .map(existingEmployeeGamma -> {
                employeeGammaMapper.partialUpdate(existingEmployeeGamma, employeeGammaDTO);

                return existingEmployeeGamma;
            })
            .map(employeeGammaRepository::save)
            .map(employeeGammaMapper::toDto);
    }

    /**
     * Get one employeeGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeGammaDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeGamma : {}", id);
        return employeeGammaRepository.findById(id).map(employeeGammaMapper::toDto);
    }

    /**
     * Delete the employeeGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeGamma : {}", id);
        employeeGammaRepository.deleteById(id);
    }
}
