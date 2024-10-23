package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeAlpha;
import xyz.jhmapstruct.repository.NextEmployeeAlphaRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeAlpha}.
 */
@Service
@Transactional
public class NextEmployeeAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeAlphaService.class);

    private final NextEmployeeAlphaRepository nextEmployeeAlphaRepository;

    private final NextEmployeeAlphaMapper nextEmployeeAlphaMapper;

    public NextEmployeeAlphaService(
        NextEmployeeAlphaRepository nextEmployeeAlphaRepository,
        NextEmployeeAlphaMapper nextEmployeeAlphaMapper
    ) {
        this.nextEmployeeAlphaRepository = nextEmployeeAlphaRepository;
        this.nextEmployeeAlphaMapper = nextEmployeeAlphaMapper;
    }

    /**
     * Save a nextEmployeeAlpha.
     *
     * @param nextEmployeeAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeAlphaDTO save(NextEmployeeAlphaDTO nextEmployeeAlphaDTO) {
        LOG.debug("Request to save NextEmployeeAlpha : {}", nextEmployeeAlphaDTO);
        NextEmployeeAlpha nextEmployeeAlpha = nextEmployeeAlphaMapper.toEntity(nextEmployeeAlphaDTO);
        nextEmployeeAlpha = nextEmployeeAlphaRepository.save(nextEmployeeAlpha);
        return nextEmployeeAlphaMapper.toDto(nextEmployeeAlpha);
    }

    /**
     * Update a nextEmployeeAlpha.
     *
     * @param nextEmployeeAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeAlphaDTO update(NextEmployeeAlphaDTO nextEmployeeAlphaDTO) {
        LOG.debug("Request to update NextEmployeeAlpha : {}", nextEmployeeAlphaDTO);
        NextEmployeeAlpha nextEmployeeAlpha = nextEmployeeAlphaMapper.toEntity(nextEmployeeAlphaDTO);
        nextEmployeeAlpha = nextEmployeeAlphaRepository.save(nextEmployeeAlpha);
        return nextEmployeeAlphaMapper.toDto(nextEmployeeAlpha);
    }

    /**
     * Partially update a nextEmployeeAlpha.
     *
     * @param nextEmployeeAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeAlphaDTO> partialUpdate(NextEmployeeAlphaDTO nextEmployeeAlphaDTO) {
        LOG.debug("Request to partially update NextEmployeeAlpha : {}", nextEmployeeAlphaDTO);

        return nextEmployeeAlphaRepository
            .findById(nextEmployeeAlphaDTO.getId())
            .map(existingNextEmployeeAlpha -> {
                nextEmployeeAlphaMapper.partialUpdate(existingNextEmployeeAlpha, nextEmployeeAlphaDTO);

                return existingNextEmployeeAlpha;
            })
            .map(nextEmployeeAlphaRepository::save)
            .map(nextEmployeeAlphaMapper::toDto);
    }

    /**
     * Get one nextEmployeeAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeAlpha : {}", id);
        return nextEmployeeAlphaRepository.findById(id).map(nextEmployeeAlphaMapper::toDto);
    }

    /**
     * Delete the nextEmployeeAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeAlpha : {}", id);
        nextEmployeeAlphaRepository.deleteById(id);
    }
}
