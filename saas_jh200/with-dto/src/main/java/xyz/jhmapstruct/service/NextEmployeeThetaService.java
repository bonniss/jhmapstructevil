package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeTheta;
import xyz.jhmapstruct.repository.NextEmployeeThetaRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeThetaDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeTheta}.
 */
@Service
@Transactional
public class NextEmployeeThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeThetaService.class);

    private final NextEmployeeThetaRepository nextEmployeeThetaRepository;

    private final NextEmployeeThetaMapper nextEmployeeThetaMapper;

    public NextEmployeeThetaService(
        NextEmployeeThetaRepository nextEmployeeThetaRepository,
        NextEmployeeThetaMapper nextEmployeeThetaMapper
    ) {
        this.nextEmployeeThetaRepository = nextEmployeeThetaRepository;
        this.nextEmployeeThetaMapper = nextEmployeeThetaMapper;
    }

    /**
     * Save a nextEmployeeTheta.
     *
     * @param nextEmployeeThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeThetaDTO save(NextEmployeeThetaDTO nextEmployeeThetaDTO) {
        LOG.debug("Request to save NextEmployeeTheta : {}", nextEmployeeThetaDTO);
        NextEmployeeTheta nextEmployeeTheta = nextEmployeeThetaMapper.toEntity(nextEmployeeThetaDTO);
        nextEmployeeTheta = nextEmployeeThetaRepository.save(nextEmployeeTheta);
        return nextEmployeeThetaMapper.toDto(nextEmployeeTheta);
    }

    /**
     * Update a nextEmployeeTheta.
     *
     * @param nextEmployeeThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeThetaDTO update(NextEmployeeThetaDTO nextEmployeeThetaDTO) {
        LOG.debug("Request to update NextEmployeeTheta : {}", nextEmployeeThetaDTO);
        NextEmployeeTheta nextEmployeeTheta = nextEmployeeThetaMapper.toEntity(nextEmployeeThetaDTO);
        nextEmployeeTheta = nextEmployeeThetaRepository.save(nextEmployeeTheta);
        return nextEmployeeThetaMapper.toDto(nextEmployeeTheta);
    }

    /**
     * Partially update a nextEmployeeTheta.
     *
     * @param nextEmployeeThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeThetaDTO> partialUpdate(NextEmployeeThetaDTO nextEmployeeThetaDTO) {
        LOG.debug("Request to partially update NextEmployeeTheta : {}", nextEmployeeThetaDTO);

        return nextEmployeeThetaRepository
            .findById(nextEmployeeThetaDTO.getId())
            .map(existingNextEmployeeTheta -> {
                nextEmployeeThetaMapper.partialUpdate(existingNextEmployeeTheta, nextEmployeeThetaDTO);

                return existingNextEmployeeTheta;
            })
            .map(nextEmployeeThetaRepository::save)
            .map(nextEmployeeThetaMapper::toDto);
    }

    /**
     * Get one nextEmployeeTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeThetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeTheta : {}", id);
        return nextEmployeeThetaRepository.findById(id).map(nextEmployeeThetaMapper::toDto);
    }

    /**
     * Delete the nextEmployeeTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeTheta : {}", id);
        nextEmployeeThetaRepository.deleteById(id);
    }
}
