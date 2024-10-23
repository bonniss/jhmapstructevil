package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerAlpha;
import xyz.jhmapstruct.repository.NextCustomerAlphaRepository;
import xyz.jhmapstruct.service.dto.NextCustomerAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerAlpha}.
 */
@Service
@Transactional
public class NextCustomerAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerAlphaService.class);

    private final NextCustomerAlphaRepository nextCustomerAlphaRepository;

    private final NextCustomerAlphaMapper nextCustomerAlphaMapper;

    public NextCustomerAlphaService(
        NextCustomerAlphaRepository nextCustomerAlphaRepository,
        NextCustomerAlphaMapper nextCustomerAlphaMapper
    ) {
        this.nextCustomerAlphaRepository = nextCustomerAlphaRepository;
        this.nextCustomerAlphaMapper = nextCustomerAlphaMapper;
    }

    /**
     * Save a nextCustomerAlpha.
     *
     * @param nextCustomerAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerAlphaDTO save(NextCustomerAlphaDTO nextCustomerAlphaDTO) {
        LOG.debug("Request to save NextCustomerAlpha : {}", nextCustomerAlphaDTO);
        NextCustomerAlpha nextCustomerAlpha = nextCustomerAlphaMapper.toEntity(nextCustomerAlphaDTO);
        nextCustomerAlpha = nextCustomerAlphaRepository.save(nextCustomerAlpha);
        return nextCustomerAlphaMapper.toDto(nextCustomerAlpha);
    }

    /**
     * Update a nextCustomerAlpha.
     *
     * @param nextCustomerAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerAlphaDTO update(NextCustomerAlphaDTO nextCustomerAlphaDTO) {
        LOG.debug("Request to update NextCustomerAlpha : {}", nextCustomerAlphaDTO);
        NextCustomerAlpha nextCustomerAlpha = nextCustomerAlphaMapper.toEntity(nextCustomerAlphaDTO);
        nextCustomerAlpha = nextCustomerAlphaRepository.save(nextCustomerAlpha);
        return nextCustomerAlphaMapper.toDto(nextCustomerAlpha);
    }

    /**
     * Partially update a nextCustomerAlpha.
     *
     * @param nextCustomerAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerAlphaDTO> partialUpdate(NextCustomerAlphaDTO nextCustomerAlphaDTO) {
        LOG.debug("Request to partially update NextCustomerAlpha : {}", nextCustomerAlphaDTO);

        return nextCustomerAlphaRepository
            .findById(nextCustomerAlphaDTO.getId())
            .map(existingNextCustomerAlpha -> {
                nextCustomerAlphaMapper.partialUpdate(existingNextCustomerAlpha, nextCustomerAlphaDTO);

                return existingNextCustomerAlpha;
            })
            .map(nextCustomerAlphaRepository::save)
            .map(nextCustomerAlphaMapper::toDto);
    }

    /**
     * Get one nextCustomerAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get NextCustomerAlpha : {}", id);
        return nextCustomerAlphaRepository.findById(id).map(nextCustomerAlphaMapper::toDto);
    }

    /**
     * Delete the nextCustomerAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerAlpha : {}", id);
        nextCustomerAlphaRepository.deleteById(id);
    }
}
