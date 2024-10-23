package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryTheta;
import xyz.jhmapstruct.repository.NextCategoryThetaRepository;
import xyz.jhmapstruct.service.dto.NextCategoryThetaDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryTheta}.
 */
@Service
@Transactional
public class NextCategoryThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryThetaService.class);

    private final NextCategoryThetaRepository nextCategoryThetaRepository;

    private final NextCategoryThetaMapper nextCategoryThetaMapper;

    public NextCategoryThetaService(
        NextCategoryThetaRepository nextCategoryThetaRepository,
        NextCategoryThetaMapper nextCategoryThetaMapper
    ) {
        this.nextCategoryThetaRepository = nextCategoryThetaRepository;
        this.nextCategoryThetaMapper = nextCategoryThetaMapper;
    }

    /**
     * Save a nextCategoryTheta.
     *
     * @param nextCategoryThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryThetaDTO save(NextCategoryThetaDTO nextCategoryThetaDTO) {
        LOG.debug("Request to save NextCategoryTheta : {}", nextCategoryThetaDTO);
        NextCategoryTheta nextCategoryTheta = nextCategoryThetaMapper.toEntity(nextCategoryThetaDTO);
        nextCategoryTheta = nextCategoryThetaRepository.save(nextCategoryTheta);
        return nextCategoryThetaMapper.toDto(nextCategoryTheta);
    }

    /**
     * Update a nextCategoryTheta.
     *
     * @param nextCategoryThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryThetaDTO update(NextCategoryThetaDTO nextCategoryThetaDTO) {
        LOG.debug("Request to update NextCategoryTheta : {}", nextCategoryThetaDTO);
        NextCategoryTheta nextCategoryTheta = nextCategoryThetaMapper.toEntity(nextCategoryThetaDTO);
        nextCategoryTheta = nextCategoryThetaRepository.save(nextCategoryTheta);
        return nextCategoryThetaMapper.toDto(nextCategoryTheta);
    }

    /**
     * Partially update a nextCategoryTheta.
     *
     * @param nextCategoryThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryThetaDTO> partialUpdate(NextCategoryThetaDTO nextCategoryThetaDTO) {
        LOG.debug("Request to partially update NextCategoryTheta : {}", nextCategoryThetaDTO);

        return nextCategoryThetaRepository
            .findById(nextCategoryThetaDTO.getId())
            .map(existingNextCategoryTheta -> {
                nextCategoryThetaMapper.partialUpdate(existingNextCategoryTheta, nextCategoryThetaDTO);

                return existingNextCategoryTheta;
            })
            .map(nextCategoryThetaRepository::save)
            .map(nextCategoryThetaMapper::toDto);
    }

    /**
     * Get one nextCategoryTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryThetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextCategoryTheta : {}", id);
        return nextCategoryThetaRepository.findById(id).map(nextCategoryThetaMapper::toDto);
    }

    /**
     * Delete the nextCategoryTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryTheta : {}", id);
        nextCategoryThetaRepository.deleteById(id);
    }
}
