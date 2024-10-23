package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceMi;
import xyz.jhmapstruct.repository.NextInvoiceMiRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceMiDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceMi}.
 */
@Service
@Transactional
public class NextInvoiceMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceMiService.class);

    private final NextInvoiceMiRepository nextInvoiceMiRepository;

    private final NextInvoiceMiMapper nextInvoiceMiMapper;

    public NextInvoiceMiService(NextInvoiceMiRepository nextInvoiceMiRepository, NextInvoiceMiMapper nextInvoiceMiMapper) {
        this.nextInvoiceMiRepository = nextInvoiceMiRepository;
        this.nextInvoiceMiMapper = nextInvoiceMiMapper;
    }

    /**
     * Save a nextInvoiceMi.
     *
     * @param nextInvoiceMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceMiDTO save(NextInvoiceMiDTO nextInvoiceMiDTO) {
        LOG.debug("Request to save NextInvoiceMi : {}", nextInvoiceMiDTO);
        NextInvoiceMi nextInvoiceMi = nextInvoiceMiMapper.toEntity(nextInvoiceMiDTO);
        nextInvoiceMi = nextInvoiceMiRepository.save(nextInvoiceMi);
        return nextInvoiceMiMapper.toDto(nextInvoiceMi);
    }

    /**
     * Update a nextInvoiceMi.
     *
     * @param nextInvoiceMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceMiDTO update(NextInvoiceMiDTO nextInvoiceMiDTO) {
        LOG.debug("Request to update NextInvoiceMi : {}", nextInvoiceMiDTO);
        NextInvoiceMi nextInvoiceMi = nextInvoiceMiMapper.toEntity(nextInvoiceMiDTO);
        nextInvoiceMi = nextInvoiceMiRepository.save(nextInvoiceMi);
        return nextInvoiceMiMapper.toDto(nextInvoiceMi);
    }

    /**
     * Partially update a nextInvoiceMi.
     *
     * @param nextInvoiceMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceMiDTO> partialUpdate(NextInvoiceMiDTO nextInvoiceMiDTO) {
        LOG.debug("Request to partially update NextInvoiceMi : {}", nextInvoiceMiDTO);

        return nextInvoiceMiRepository
            .findById(nextInvoiceMiDTO.getId())
            .map(existingNextInvoiceMi -> {
                nextInvoiceMiMapper.partialUpdate(existingNextInvoiceMi, nextInvoiceMiDTO);

                return existingNextInvoiceMi;
            })
            .map(nextInvoiceMiRepository::save)
            .map(nextInvoiceMiMapper::toDto);
    }

    /**
     * Get one nextInvoiceMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceMi : {}", id);
        return nextInvoiceMiRepository.findById(id).map(nextInvoiceMiMapper::toDto);
    }

    /**
     * Delete the nextInvoiceMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceMi : {}", id);
        nextInvoiceMiRepository.deleteById(id);
    }
}
