package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCategorySigma;
import xyz.jhmapstruct.repository.NextCategorySigmaRepository;
import xyz.jhmapstruct.service.dto.NextCategorySigmaDTO;
import xyz.jhmapstruct.service.mapper.NextCategorySigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCategorySigma}.
 */
@Service
@Transactional
public class NextCategorySigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategorySigmaService.class);

    private final NextCategorySigmaRepository nextCategorySigmaRepository;

    private final NextCategorySigmaMapper nextCategorySigmaMapper;

    public NextCategorySigmaService(
        NextCategorySigmaRepository nextCategorySigmaRepository,
        NextCategorySigmaMapper nextCategorySigmaMapper
    ) {
        this.nextCategorySigmaRepository = nextCategorySigmaRepository;
        this.nextCategorySigmaMapper = nextCategorySigmaMapper;
    }

    /**
     * Save a nextCategorySigma.
     *
     * @param nextCategorySigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategorySigmaDTO save(NextCategorySigmaDTO nextCategorySigmaDTO) {
        LOG.debug("Request to save NextCategorySigma : {}", nextCategorySigmaDTO);
        NextCategorySigma nextCategorySigma = nextCategorySigmaMapper.toEntity(nextCategorySigmaDTO);
        nextCategorySigma = nextCategorySigmaRepository.save(nextCategorySigma);
        return nextCategorySigmaMapper.toDto(nextCategorySigma);
    }

    /**
     * Update a nextCategorySigma.
     *
     * @param nextCategorySigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCategorySigmaDTO update(NextCategorySigmaDTO nextCategorySigmaDTO) {
        LOG.debug("Request to update NextCategorySigma : {}", nextCategorySigmaDTO);
        NextCategorySigma nextCategorySigma = nextCategorySigmaMapper.toEntity(nextCategorySigmaDTO);
        nextCategorySigma = nextCategorySigmaRepository.save(nextCategorySigma);
        return nextCategorySigmaMapper.toDto(nextCategorySigma);
    }

    /**
     * Partially update a nextCategorySigma.
     *
     * @param nextCategorySigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCategorySigmaDTO> partialUpdate(NextCategorySigmaDTO nextCategorySigmaDTO) {
        LOG.debug("Request to partially update NextCategorySigma : {}", nextCategorySigmaDTO);

        return nextCategorySigmaRepository
            .findById(nextCategorySigmaDTO.getId())
            .map(existingNextCategorySigma -> {
                nextCategorySigmaMapper.partialUpdate(existingNextCategorySigma, nextCategorySigmaDTO);

                return existingNextCategorySigma;
            })
            .map(nextCategorySigmaRepository::save)
            .map(nextCategorySigmaMapper::toDto);
    }

    /**
     * Get one nextCategorySigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCategorySigmaDTO> findOne(Long id) {
        LOG.debug("Request to get NextCategorySigma : {}", id);
        return nextCategorySigmaRepository.findById(id).map(nextCategorySigmaMapper::toDto);
    }

    /**
     * Delete the nextCategorySigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCategorySigma : {}", id);
        nextCategorySigmaRepository.deleteById(id);
    }
}
