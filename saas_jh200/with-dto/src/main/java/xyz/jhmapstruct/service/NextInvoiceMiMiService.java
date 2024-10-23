package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceMiMi;
import xyz.jhmapstruct.repository.NextInvoiceMiMiRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceMiMi}.
 */
@Service
@Transactional
public class NextInvoiceMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceMiMiService.class);

    private final NextInvoiceMiMiRepository nextInvoiceMiMiRepository;

    private final NextInvoiceMiMiMapper nextInvoiceMiMiMapper;

    public NextInvoiceMiMiService(NextInvoiceMiMiRepository nextInvoiceMiMiRepository, NextInvoiceMiMiMapper nextInvoiceMiMiMapper) {
        this.nextInvoiceMiMiRepository = nextInvoiceMiMiRepository;
        this.nextInvoiceMiMiMapper = nextInvoiceMiMiMapper;
    }

    /**
     * Save a nextInvoiceMiMi.
     *
     * @param nextInvoiceMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceMiMiDTO save(NextInvoiceMiMiDTO nextInvoiceMiMiDTO) {
        LOG.debug("Request to save NextInvoiceMiMi : {}", nextInvoiceMiMiDTO);
        NextInvoiceMiMi nextInvoiceMiMi = nextInvoiceMiMiMapper.toEntity(nextInvoiceMiMiDTO);
        nextInvoiceMiMi = nextInvoiceMiMiRepository.save(nextInvoiceMiMi);
        return nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);
    }

    /**
     * Update a nextInvoiceMiMi.
     *
     * @param nextInvoiceMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceMiMiDTO update(NextInvoiceMiMiDTO nextInvoiceMiMiDTO) {
        LOG.debug("Request to update NextInvoiceMiMi : {}", nextInvoiceMiMiDTO);
        NextInvoiceMiMi nextInvoiceMiMi = nextInvoiceMiMiMapper.toEntity(nextInvoiceMiMiDTO);
        nextInvoiceMiMi = nextInvoiceMiMiRepository.save(nextInvoiceMiMi);
        return nextInvoiceMiMiMapper.toDto(nextInvoiceMiMi);
    }

    /**
     * Partially update a nextInvoiceMiMi.
     *
     * @param nextInvoiceMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceMiMiDTO> partialUpdate(NextInvoiceMiMiDTO nextInvoiceMiMiDTO) {
        LOG.debug("Request to partially update NextInvoiceMiMi : {}", nextInvoiceMiMiDTO);

        return nextInvoiceMiMiRepository
            .findById(nextInvoiceMiMiDTO.getId())
            .map(existingNextInvoiceMiMi -> {
                nextInvoiceMiMiMapper.partialUpdate(existingNextInvoiceMiMi, nextInvoiceMiMiDTO);

                return existingNextInvoiceMiMi;
            })
            .map(nextInvoiceMiMiRepository::save)
            .map(nextInvoiceMiMiMapper::toDto);
    }

    /**
     * Get one nextInvoiceMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceMiMi : {}", id);
        return nextInvoiceMiMiRepository.findById(id).map(nextInvoiceMiMiMapper::toDto);
    }

    /**
     * Delete the nextInvoiceMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceMiMi : {}", id);
        nextInvoiceMiMiRepository.deleteById(id);
    }
}
