package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.EmployeeBeta;
import xyz.jhmapstruct.repository.EmployeeBetaRepository;
import xyz.jhmapstruct.service.dto.EmployeeBetaDTO;
import xyz.jhmapstruct.service.mapper.EmployeeBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.EmployeeBeta}.
 */
@Service
@Transactional
public class EmployeeBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeBetaService.class);

    private final EmployeeBetaRepository employeeBetaRepository;

    private final EmployeeBetaMapper employeeBetaMapper;

    public EmployeeBetaService(EmployeeBetaRepository employeeBetaRepository, EmployeeBetaMapper employeeBetaMapper) {
        this.employeeBetaRepository = employeeBetaRepository;
        this.employeeBetaMapper = employeeBetaMapper;
    }

    /**
     * Save a employeeBeta.
     *
     * @param employeeBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeBetaDTO save(EmployeeBetaDTO employeeBetaDTO) {
        LOG.debug("Request to save EmployeeBeta : {}", employeeBetaDTO);
        EmployeeBeta employeeBeta = employeeBetaMapper.toEntity(employeeBetaDTO);
        employeeBeta = employeeBetaRepository.save(employeeBeta);
        return employeeBetaMapper.toDto(employeeBeta);
    }

    /**
     * Update a employeeBeta.
     *
     * @param employeeBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public EmployeeBetaDTO update(EmployeeBetaDTO employeeBetaDTO) {
        LOG.debug("Request to update EmployeeBeta : {}", employeeBetaDTO);
        EmployeeBeta employeeBeta = employeeBetaMapper.toEntity(employeeBetaDTO);
        employeeBeta = employeeBetaRepository.save(employeeBeta);
        return employeeBetaMapper.toDto(employeeBeta);
    }

    /**
     * Partially update a employeeBeta.
     *
     * @param employeeBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EmployeeBetaDTO> partialUpdate(EmployeeBetaDTO employeeBetaDTO) {
        LOG.debug("Request to partially update EmployeeBeta : {}", employeeBetaDTO);

        return employeeBetaRepository
            .findById(employeeBetaDTO.getId())
            .map(existingEmployeeBeta -> {
                employeeBetaMapper.partialUpdate(existingEmployeeBeta, employeeBetaDTO);

                return existingEmployeeBeta;
            })
            .map(employeeBetaRepository::save)
            .map(employeeBetaMapper::toDto);
    }

    /**
     * Get one employeeBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EmployeeBetaDTO> findOne(Long id) {
        LOG.debug("Request to get EmployeeBeta : {}", id);
        return employeeBetaRepository.findById(id).map(employeeBetaMapper::toDto);
    }

    /**
     * Delete the employeeBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EmployeeBeta : {}", id);
        employeeBetaRepository.deleteById(id);
    }
}
