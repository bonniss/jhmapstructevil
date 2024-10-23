package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentAlpha;
import xyz.jhmapstruct.repository.NextPaymentAlphaRepository;
import xyz.jhmapstruct.service.dto.NextPaymentAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentAlpha}.
 */
@Service
@Transactional
public class NextPaymentAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentAlphaService.class);

    private final NextPaymentAlphaRepository nextPaymentAlphaRepository;

    private final NextPaymentAlphaMapper nextPaymentAlphaMapper;

    public NextPaymentAlphaService(NextPaymentAlphaRepository nextPaymentAlphaRepository, NextPaymentAlphaMapper nextPaymentAlphaMapper) {
        this.nextPaymentAlphaRepository = nextPaymentAlphaRepository;
        this.nextPaymentAlphaMapper = nextPaymentAlphaMapper;
    }

    /**
     * Save a nextPaymentAlpha.
     *
     * @param nextPaymentAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentAlphaDTO save(NextPaymentAlphaDTO nextPaymentAlphaDTO) {
        LOG.debug("Request to save NextPaymentAlpha : {}", nextPaymentAlphaDTO);
        NextPaymentAlpha nextPaymentAlpha = nextPaymentAlphaMapper.toEntity(nextPaymentAlphaDTO);
        nextPaymentAlpha = nextPaymentAlphaRepository.save(nextPaymentAlpha);
        return nextPaymentAlphaMapper.toDto(nextPaymentAlpha);
    }

    /**
     * Update a nextPaymentAlpha.
     *
     * @param nextPaymentAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentAlphaDTO update(NextPaymentAlphaDTO nextPaymentAlphaDTO) {
        LOG.debug("Request to update NextPaymentAlpha : {}", nextPaymentAlphaDTO);
        NextPaymentAlpha nextPaymentAlpha = nextPaymentAlphaMapper.toEntity(nextPaymentAlphaDTO);
        nextPaymentAlpha = nextPaymentAlphaRepository.save(nextPaymentAlpha);
        return nextPaymentAlphaMapper.toDto(nextPaymentAlpha);
    }

    /**
     * Partially update a nextPaymentAlpha.
     *
     * @param nextPaymentAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentAlphaDTO> partialUpdate(NextPaymentAlphaDTO nextPaymentAlphaDTO) {
        LOG.debug("Request to partially update NextPaymentAlpha : {}", nextPaymentAlphaDTO);

        return nextPaymentAlphaRepository
            .findById(nextPaymentAlphaDTO.getId())
            .map(existingNextPaymentAlpha -> {
                nextPaymentAlphaMapper.partialUpdate(existingNextPaymentAlpha, nextPaymentAlphaDTO);

                return existingNextPaymentAlpha;
            })
            .map(nextPaymentAlphaRepository::save)
            .map(nextPaymentAlphaMapper::toDto);
    }

    /**
     * Get one nextPaymentAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get NextPaymentAlpha : {}", id);
        return nextPaymentAlphaRepository.findById(id).map(nextPaymentAlphaMapper::toDto);
    }

    /**
     * Delete the nextPaymentAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentAlpha : {}", id);
        nextPaymentAlphaRepository.deleteById(id);
    }
}
