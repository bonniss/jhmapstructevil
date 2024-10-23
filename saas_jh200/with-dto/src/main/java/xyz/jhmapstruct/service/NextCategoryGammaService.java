package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategoryGamma;
import xyz.jhmapstruct.repository.NextCategoryGammaRepository;
import xyz.jhmapstruct.service.dto.NextCategoryGammaDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategoryGamma}.
 */
@Service
@Transactional
public class NextCategoryGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryGammaService.class);

    private final NextCategoryGammaRepository nextCategoryGammaRepository;

    private final NextCategoryGammaMapper nextCategoryGammaMapper;

    public NextCategoryGammaService(
        NextCategoryGammaRepository nextCategoryGammaRepository,
        NextCategoryGammaMapper nextCategoryGammaMapper
    ) {
        this.nextCategoryGammaRepository = nextCategoryGammaRepository;
        this.nextCategoryGammaMapper = nextCategoryGammaMapper;
    }

    /**
     * Save a nextCategoryGamma.
     *
     * @param nextCategoryGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryGammaDTO save(NextCategoryGammaDTO nextCategoryGammaDTO) {
        LOG.debug("Request to save NextCategoryGamma : {}", nextCategoryGammaDTO);
        NextCategoryGamma nextCategoryGamma = nextCategoryGammaMapper.toEntity(nextCategoryGammaDTO);
        nextCategoryGamma = nextCategoryGammaRepository.save(nextCategoryGamma);
        return nextCategoryGammaMapper.toDto(nextCategoryGamma);
    }

    /**
     * Update a nextCategoryGamma.
     *
     * @param nextCategoryGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategoryGammaDTO update(NextCategoryGammaDTO nextCategoryGammaDTO) {
        LOG.debug("Request to update NextCategoryGamma : {}", nextCategoryGammaDTO);
        NextCategoryGamma nextCategoryGamma = nextCategoryGammaMapper.toEntity(nextCategoryGammaDTO);
        nextCategoryGamma = nextCategoryGammaRepository.save(nextCategoryGamma);
        return nextCategoryGammaMapper.toDto(nextCategoryGamma);
    }

    /**
     * Partially update a nextCategoryGamma.
     *
     * @param nextCategoryGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategoryGammaDTO> partialUpdate(NextCategoryGammaDTO nextCategoryGammaDTO) {
        LOG.debug("Request to partially update NextCategoryGamma : {}", nextCategoryGammaDTO);

        return nextCategoryGammaRepository
            .findById(nextCategoryGammaDTO.getId())
            .map(existingNextCategoryGamma -> {
                nextCategoryGammaMapper.partialUpdate(existingNextCategoryGamma, nextCategoryGammaDTO);

                return existingNextCategoryGamma;
            })
            .map(nextCategoryGammaRepository::save)
            .map(nextCategoryGammaMapper::toDto);
    }

    /**
     * Get one nextCategoryGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategoryGammaDTO> findOne(Long id) {
        LOG.debug("Request to get NextCategoryGamma : {}", id);
        return nextCategoryGammaRepository.findById(id).map(nextCategoryGammaMapper::toDto);
    }

    /**
     * Delete the nextCategoryGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategoryGamma : {}", id);
        nextCategoryGammaRepository.deleteById(id);
    }
}
