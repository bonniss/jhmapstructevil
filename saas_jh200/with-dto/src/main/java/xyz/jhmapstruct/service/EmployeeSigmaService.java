package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeSigma;
import xyz.jhmapstruct.repository.EmployeeSigmaRepository;
import xyz.jhmapstruct.service.dto.EmployeeSigmaDTO;
import xyz.jhmapstruct.service.mapper.EmployeeSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeSigma}.
 */
@Service
@Transactional
public class EmployeeSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeSigmaService.class);

    private final EmployeeSigmaRepository employeeSigmaRepository;

    private final EmployeeSigmaMapper employeeSigmaMapper;

    public EmployeeSigmaService(EmployeeSigmaRepository employeeSigmaRepository, EmployeeSigmaMapper employeeSigmaMapper) {
        this.employeeSigmaRepository = employeeSigmaRepository;
        this.employeeSigmaMapper = employeeSigmaMapper;
    }

    /**
     * Save a employeeSigma.
     *
     * @param employeeSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeSigmaDTO save(EmployeeSigmaDTO employeeSigmaDTO) {
        LOG.debug("Request to save EmployeeSigma : {}", employeeSigmaDTO);
        EmployeeSigma employeeSigma = employeeSigmaMapper.toEntity(employeeSigmaDTO);
        employeeSigma = employeeSigmaRepository.save(employeeSigma);
        return employeeSigmaMapper.toDto(employeeSigma);
    }

    /**
     * Update a employeeSigma.
     *
     * @param employeeSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeSigmaDTO update(EmployeeSigmaDTO employeeSigmaDTO) {
        LOG.debug("Request to update EmployeeSigma : {}", employeeSigmaDTO);
        EmployeeSigma employeeSigma = employeeSigmaMapper.toEntity(employeeSigmaDTO);
        employeeSigma = employeeSigmaRepository.save(employeeSigma);
        return employeeSigmaMapper.toDto(employeeSigma);
    }

    /**
     * Partially update a employeeSigma.
     *
     * @param employeeSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeSigmaDTO> partialUpdate(EmployeeSigmaDTO employeeSigmaDTO) {
        LOG.debug("Request to partially update EmployeeSigma : {}", employeeSigmaDTO);

        return employeeSigmaRepository
            .findById(employeeSigmaDTO.getId())
            .map(existingEmployeeSigma -> {
                employeeSigmaMapper.partialUpdate(existingEmployeeSigma, employeeSigmaDTO);

                return existingEmployeeSigma;
            })
            .map(employeeSigmaRepository::save)
            .map(employeeSigmaMapper::toDto);
    }

    /**
     * Get one employeeSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeSigma : {}", id);
        return employeeSigmaRepository.findById(id).map(employeeSigmaMapper::toDto);
    }

    /**
     * Delete the employeeSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeSigma : {}", id);
        employeeSigmaRepository.deleteById(id);
    }
}
