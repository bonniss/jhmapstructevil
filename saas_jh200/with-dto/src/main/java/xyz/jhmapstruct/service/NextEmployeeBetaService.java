package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeBeta;
import xyz.jhmapstruct.repository.NextEmployeeBetaRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeBetaDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeBeta}.
 */
@Service
@Transactional
public class NextEmployeeBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeBetaService.class);

    private final NextEmployeeBetaRepository nextEmployeeBetaRepository;

    private final NextEmployeeBetaMapper nextEmployeeBetaMapper;

    public NextEmployeeBetaService(NextEmployeeBetaRepository nextEmployeeBetaRepository, NextEmployeeBetaMapper nextEmployeeBetaMapper) {
        this.nextEmployeeBetaRepository = nextEmployeeBetaRepository;
        this.nextEmployeeBetaMapper = nextEmployeeBetaMapper;
    }

    /**
     * Save a nextEmployeeBeta.
     *
     * @param nextEmployeeBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeBetaDTO save(NextEmployeeBetaDTO nextEmployeeBetaDTO) {
        LOG.debug("Request to save NextEmployeeBeta : {}", nextEmployeeBetaDTO);
        NextEmployeeBeta nextEmployeeBeta = nextEmployeeBetaMapper.toEntity(nextEmployeeBetaDTO);
        nextEmployeeBeta = nextEmployeeBetaRepository.save(nextEmployeeBeta);
        return nextEmployeeBetaMapper.toDto(nextEmployeeBeta);
    }

    /**
     * Update a nextEmployeeBeta.
     *
     * @param nextEmployeeBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeBetaDTO update(NextEmployeeBetaDTO nextEmployeeBetaDTO) {
        LOG.debug("Request to update NextEmployeeBeta : {}", nextEmployeeBetaDTO);
        NextEmployeeBeta nextEmployeeBeta = nextEmployeeBetaMapper.toEntity(nextEmployeeBetaDTO);
        nextEmployeeBeta = nextEmployeeBetaRepository.save(nextEmployeeBeta);
        return nextEmployeeBetaMapper.toDto(nextEmployeeBeta);
    }

    /**
     * Partially update a nextEmployeeBeta.
     *
     * @param nextEmployeeBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeBetaDTO> partialUpdate(NextEmployeeBetaDTO nextEmployeeBetaDTO) {
        LOG.debug("Request to partially update NextEmployeeBeta : {}", nextEmployeeBetaDTO);

        return nextEmployeeBetaRepository
            .findById(nextEmployeeBetaDTO.getId())
            .map(existingNextEmployeeBeta -> {
                nextEmployeeBetaMapper.partialUpdate(existingNextEmployeeBeta, nextEmployeeBetaDTO);

                return existingNextEmployeeBeta;
            })
            .map(nextEmployeeBetaRepository::save)
            .map(nextEmployeeBetaMapper::toDto);
    }

    /**
     * Get one nextEmployeeBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeBetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeBeta : {}", id);
        return nextEmployeeBetaRepository.findById(id).map(nextEmployeeBetaMapper::toDto);
    }

    /**
     * Delete the nextEmployeeBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeBeta : {}", id);
        nextEmployeeBetaRepository.deleteById(id);
    }
}
