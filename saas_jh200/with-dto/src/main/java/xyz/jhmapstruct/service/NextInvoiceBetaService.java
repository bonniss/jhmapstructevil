package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceBeta;
import xyz.jhmapstruct.repository.NextInvoiceBetaRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceBetaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceBeta}.
 */
@Service
@Transactional
public class NextInvoiceBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceBetaService.class);

    private final NextInvoiceBetaRepository nextInvoiceBetaRepository;

    private final NextInvoiceBetaMapper nextInvoiceBetaMapper;

    public NextInvoiceBetaService(NextInvoiceBetaRepository nextInvoiceBetaRepository, NextInvoiceBetaMapper nextInvoiceBetaMapper) {
        this.nextInvoiceBetaRepository = nextInvoiceBetaRepository;
        this.nextInvoiceBetaMapper = nextInvoiceBetaMapper;
    }

    /**
     * Save a nextInvoiceBeta.
     *
     * @param nextInvoiceBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceBetaDTO save(NextInvoiceBetaDTO nextInvoiceBetaDTO) {
        LOG.debug("Request to save NextInvoiceBeta : {}", nextInvoiceBetaDTO);
        NextInvoiceBeta nextInvoiceBeta = nextInvoiceBetaMapper.toEntity(nextInvoiceBetaDTO);
        nextInvoiceBeta = nextInvoiceBetaRepository.save(nextInvoiceBeta);
        return nextInvoiceBetaMapper.toDto(nextInvoiceBeta);
    }

    /**
     * Update a nextInvoiceBeta.
     *
     * @param nextInvoiceBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceBetaDTO update(NextInvoiceBetaDTO nextInvoiceBetaDTO) {
        LOG.debug("Request to update NextInvoiceBeta : {}", nextInvoiceBetaDTO);
        NextInvoiceBeta nextInvoiceBeta = nextInvoiceBetaMapper.toEntity(nextInvoiceBetaDTO);
        nextInvoiceBeta = nextInvoiceBetaRepository.save(nextInvoiceBeta);
        return nextInvoiceBetaMapper.toDto(nextInvoiceBeta);
    }

    /**
     * Partially update a nextInvoiceBeta.
     *
     * @param nextInvoiceBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceBetaDTO> partialUpdate(NextInvoiceBetaDTO nextInvoiceBetaDTO) {
        LOG.debug("Request to partially update NextInvoiceBeta : {}", nextInvoiceBetaDTO);

        return nextInvoiceBetaRepository
            .findById(nextInvoiceBetaDTO.getId())
            .map(existingNextInvoiceBeta -> {
                nextInvoiceBetaMapper.partialUpdate(existingNextInvoiceBeta, nextInvoiceBetaDTO);

                return existingNextInvoiceBeta;
            })
            .map(nextInvoiceBetaRepository::save)
            .map(nextInvoiceBetaMapper::toDto);
    }

    /**
     * Get one nextInvoiceBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceBetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceBeta : {}", id);
        return nextInvoiceBetaRepository.findById(id).map(nextInvoiceBetaMapper::toDto);
    }

    /**
     * Delete the nextInvoiceBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceBeta : {}", id);
        nextInvoiceBetaRepository.deleteById(id);
    }
}
