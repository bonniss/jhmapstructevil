package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceGamma;
import xyz.jhmapstruct.repository.NextInvoiceGammaRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceGammaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceGamma}.
 */
@Service
@Transactional
public class NextInvoiceGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceGammaService.class);

    private final NextInvoiceGammaRepository nextInvoiceGammaRepository;

    private final NextInvoiceGammaMapper nextInvoiceGammaMapper;

    public NextInvoiceGammaService(NextInvoiceGammaRepository nextInvoiceGammaRepository, NextInvoiceGammaMapper nextInvoiceGammaMapper) {
        this.nextInvoiceGammaRepository = nextInvoiceGammaRepository;
        this.nextInvoiceGammaMapper = nextInvoiceGammaMapper;
    }

    /**
     * Save a nextInvoiceGamma.
     *
     * @param nextInvoiceGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceGammaDTO save(NextInvoiceGammaDTO nextInvoiceGammaDTO) {
        LOG.debug("Request to save NextInvoiceGamma : {}", nextInvoiceGammaDTO);
        NextInvoiceGamma nextInvoiceGamma = nextInvoiceGammaMapper.toEntity(nextInvoiceGammaDTO);
        nextInvoiceGamma = nextInvoiceGammaRepository.save(nextInvoiceGamma);
        return nextInvoiceGammaMapper.toDto(nextInvoiceGamma);
    }

    /**
     * Update a nextInvoiceGamma.
     *
     * @param nextInvoiceGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceGammaDTO update(NextInvoiceGammaDTO nextInvoiceGammaDTO) {
        LOG.debug("Request to update NextInvoiceGamma : {}", nextInvoiceGammaDTO);
        NextInvoiceGamma nextInvoiceGamma = nextInvoiceGammaMapper.toEntity(nextInvoiceGammaDTO);
        nextInvoiceGamma = nextInvoiceGammaRepository.save(nextInvoiceGamma);
        return nextInvoiceGammaMapper.toDto(nextInvoiceGamma);
    }

    /**
     * Partially update a nextInvoiceGamma.
     *
     * @param nextInvoiceGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceGammaDTO> partialUpdate(NextInvoiceGammaDTO nextInvoiceGammaDTO) {
        LOG.debug("Request to partially update NextInvoiceGamma : {}", nextInvoiceGammaDTO);

        return nextInvoiceGammaRepository
            .findById(nextInvoiceGammaDTO.getId())
            .map(existingNextInvoiceGamma -> {
                nextInvoiceGammaMapper.partialUpdate(existingNextInvoiceGamma, nextInvoiceGammaDTO);

                return existingNextInvoiceGamma;
            })
            .map(nextInvoiceGammaRepository::save)
            .map(nextInvoiceGammaMapper::toDto);
    }

    /**
     * Get one nextInvoiceGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceGammaDTO> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceGamma : {}", id);
        return nextInvoiceGammaRepository.findById(id).map(nextInvoiceGammaMapper::toDto);
    }

    /**
     * Delete the nextInvoiceGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceGamma : {}", id);
        nextInvoiceGammaRepository.deleteById(id);
    }
}
