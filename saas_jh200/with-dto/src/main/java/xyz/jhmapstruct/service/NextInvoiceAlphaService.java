package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceAlpha;
import xyz.jhmapstruct.repository.NextInvoiceAlphaRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceAlpha}.
 */
@Service
@Transactional
public class NextInvoiceAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceAlphaService.class);

    private final NextInvoiceAlphaRepository nextInvoiceAlphaRepository;

    private final NextInvoiceAlphaMapper nextInvoiceAlphaMapper;

    public NextInvoiceAlphaService(NextInvoiceAlphaRepository nextInvoiceAlphaRepository, NextInvoiceAlphaMapper nextInvoiceAlphaMapper) {
        this.nextInvoiceAlphaRepository = nextInvoiceAlphaRepository;
        this.nextInvoiceAlphaMapper = nextInvoiceAlphaMapper;
    }

    /**
     * Save a nextInvoiceAlpha.
     *
     * @param nextInvoiceAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceAlphaDTO save(NextInvoiceAlphaDTO nextInvoiceAlphaDTO) {
        LOG.debug("Request to save NextInvoiceAlpha : {}", nextInvoiceAlphaDTO);
        NextInvoiceAlpha nextInvoiceAlpha = nextInvoiceAlphaMapper.toEntity(nextInvoiceAlphaDTO);
        nextInvoiceAlpha = nextInvoiceAlphaRepository.save(nextInvoiceAlpha);
        return nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);
    }

    /**
     * Update a nextInvoiceAlpha.
     *
     * @param nextInvoiceAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceAlphaDTO update(NextInvoiceAlphaDTO nextInvoiceAlphaDTO) {
        LOG.debug("Request to update NextInvoiceAlpha : {}", nextInvoiceAlphaDTO);
        NextInvoiceAlpha nextInvoiceAlpha = nextInvoiceAlphaMapper.toEntity(nextInvoiceAlphaDTO);
        nextInvoiceAlpha = nextInvoiceAlphaRepository.save(nextInvoiceAlpha);
        return nextInvoiceAlphaMapper.toDto(nextInvoiceAlpha);
    }

    /**
     * Partially update a nextInvoiceAlpha.
     *
     * @param nextInvoiceAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceAlphaDTO> partialUpdate(NextInvoiceAlphaDTO nextInvoiceAlphaDTO) {
        LOG.debug("Request to partially update NextInvoiceAlpha : {}", nextInvoiceAlphaDTO);

        return nextInvoiceAlphaRepository
            .findById(nextInvoiceAlphaDTO.getId())
            .map(existingNextInvoiceAlpha -> {
                nextInvoiceAlphaMapper.partialUpdate(existingNextInvoiceAlpha, nextInvoiceAlphaDTO);

                return existingNextInvoiceAlpha;
            })
            .map(nextInvoiceAlphaRepository::save)
            .map(nextInvoiceAlphaMapper::toDto);
    }

    /**
     * Get one nextInvoiceAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceAlpha : {}", id);
        return nextInvoiceAlphaRepository.findById(id).map(nextInvoiceAlphaMapper::toDto);
    }

    /**
     * Delete the nextInvoiceAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceAlpha : {}", id);
        nextInvoiceAlphaRepository.deleteById(id);
    }
}
