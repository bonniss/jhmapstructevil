package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceViVi;
import xyz.jhmapstruct.repository.NextInvoiceViViRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceViViDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceViVi}.
 */
@Service
@Transactional
public class NextInvoiceViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceViViService.class);

    private final NextInvoiceViViRepository nextInvoiceViViRepository;

    private final NextInvoiceViViMapper nextInvoiceViViMapper;

    public NextInvoiceViViService(NextInvoiceViViRepository nextInvoiceViViRepository, NextInvoiceViViMapper nextInvoiceViViMapper) {
        this.nextInvoiceViViRepository = nextInvoiceViViRepository;
        this.nextInvoiceViViMapper = nextInvoiceViViMapper;
    }

    /**
     * Save a nextInvoiceViVi.
     *
     * @param nextInvoiceViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceViViDTO save(NextInvoiceViViDTO nextInvoiceViViDTO) {
        LOG.debug("Request to save NextInvoiceViVi : {}", nextInvoiceViViDTO);
        NextInvoiceViVi nextInvoiceViVi = nextInvoiceViViMapper.toEntity(nextInvoiceViViDTO);
        nextInvoiceViVi = nextInvoiceViViRepository.save(nextInvoiceViVi);
        return nextInvoiceViViMapper.toDto(nextInvoiceViVi);
    }

    /**
     * Update a nextInvoiceViVi.
     *
     * @param nextInvoiceViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceViViDTO update(NextInvoiceViViDTO nextInvoiceViViDTO) {
        LOG.debug("Request to update NextInvoiceViVi : {}", nextInvoiceViViDTO);
        NextInvoiceViVi nextInvoiceViVi = nextInvoiceViViMapper.toEntity(nextInvoiceViViDTO);
        nextInvoiceViVi = nextInvoiceViViRepository.save(nextInvoiceViVi);
        return nextInvoiceViViMapper.toDto(nextInvoiceViVi);
    }

    /**
     * Partially update a nextInvoiceViVi.
     *
     * @param nextInvoiceViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceViViDTO> partialUpdate(NextInvoiceViViDTO nextInvoiceViViDTO) {
        LOG.debug("Request to partially update NextInvoiceViVi : {}", nextInvoiceViViDTO);

        return nextInvoiceViViRepository
            .findById(nextInvoiceViViDTO.getId())
            .map(existingNextInvoiceViVi -> {
                nextInvoiceViViMapper.partialUpdate(existingNextInvoiceViVi, nextInvoiceViViDTO);

                return existingNextInvoiceViVi;
            })
            .map(nextInvoiceViViRepository::save)
            .map(nextInvoiceViViMapper::toDto);
    }

    /**
     * Get one nextInvoiceViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceViViDTO> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceViVi : {}", id);
        return nextInvoiceViViRepository.findById(id).map(nextInvoiceViViMapper::toDto);
    }

    /**
     * Delete the nextInvoiceViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceViVi : {}", id);
        nextInvoiceViViRepository.deleteById(id);
    }
}
