package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceSigma;
import xyz.jhmapstruct.repository.NextInvoiceSigmaRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceSigma}.
 */
@Service
@Transactional
public class NextInvoiceSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceSigmaService.class);

    private final NextInvoiceSigmaRepository nextInvoiceSigmaRepository;

    private final NextInvoiceSigmaMapper nextInvoiceSigmaMapper;

    public NextInvoiceSigmaService(NextInvoiceSigmaRepository nextInvoiceSigmaRepository, NextInvoiceSigmaMapper nextInvoiceSigmaMapper) {
        this.nextInvoiceSigmaRepository = nextInvoiceSigmaRepository;
        this.nextInvoiceSigmaMapper = nextInvoiceSigmaMapper;
    }

    /**
     * Save a nextInvoiceSigma.
     *
     * @param nextInvoiceSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceSigmaDTO save(NextInvoiceSigmaDTO nextInvoiceSigmaDTO) {
        LOG.debug("Request to save NextInvoiceSigma : {}", nextInvoiceSigmaDTO);
        NextInvoiceSigma nextInvoiceSigma = nextInvoiceSigmaMapper.toEntity(nextInvoiceSigmaDTO);
        nextInvoiceSigma = nextInvoiceSigmaRepository.save(nextInvoiceSigma);
        return nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);
    }

    /**
     * Update a nextInvoiceSigma.
     *
     * @param nextInvoiceSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceSigmaDTO update(NextInvoiceSigmaDTO nextInvoiceSigmaDTO) {
        LOG.debug("Request to update NextInvoiceSigma : {}", nextInvoiceSigmaDTO);
        NextInvoiceSigma nextInvoiceSigma = nextInvoiceSigmaMapper.toEntity(nextInvoiceSigmaDTO);
        nextInvoiceSigma = nextInvoiceSigmaRepository.save(nextInvoiceSigma);
        return nextInvoiceSigmaMapper.toDto(nextInvoiceSigma);
    }

    /**
     * Partially update a nextInvoiceSigma.
     *
     * @param nextInvoiceSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceSigmaDTO> partialUpdate(NextInvoiceSigmaDTO nextInvoiceSigmaDTO) {
        LOG.debug("Request to partially update NextInvoiceSigma : {}", nextInvoiceSigmaDTO);

        return nextInvoiceSigmaRepository
            .findById(nextInvoiceSigmaDTO.getId())
            .map(existingNextInvoiceSigma -> {
                nextInvoiceSigmaMapper.partialUpdate(existingNextInvoiceSigma, nextInvoiceSigmaDTO);

                return existingNextInvoiceSigma;
            })
            .map(nextInvoiceSigmaRepository::save)
            .map(nextInvoiceSigmaMapper::toDto);
    }

    /**
     * Get one nextInvoiceSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceSigma : {}", id);
        return nextInvoiceSigmaRepository.findById(id).map(nextInvoiceSigmaMapper::toDto);
    }

    /**
     * Delete the nextInvoiceSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceSigma : {}", id);
        nextInvoiceSigmaRepository.deleteById(id);
    }
}
