package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceVi;
import xyz.jhmapstruct.repository.NextInvoiceViRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceViDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceVi}.
 */
@Service
@Transactional
public class NextInvoiceViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceViService.class);

    private final NextInvoiceViRepository nextInvoiceViRepository;

    private final NextInvoiceViMapper nextInvoiceViMapper;

    public NextInvoiceViService(NextInvoiceViRepository nextInvoiceViRepository, NextInvoiceViMapper nextInvoiceViMapper) {
        this.nextInvoiceViRepository = nextInvoiceViRepository;
        this.nextInvoiceViMapper = nextInvoiceViMapper;
    }

    /**
     * Save a nextInvoiceVi.
     *
     * @param nextInvoiceViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceViDTO save(NextInvoiceViDTO nextInvoiceViDTO) {
        LOG.debug("Request to save NextInvoiceVi : {}", nextInvoiceViDTO);
        NextInvoiceVi nextInvoiceVi = nextInvoiceViMapper.toEntity(nextInvoiceViDTO);
        nextInvoiceVi = nextInvoiceViRepository.save(nextInvoiceVi);
        return nextInvoiceViMapper.toDto(nextInvoiceVi);
    }

    /**
     * Update a nextInvoiceVi.
     *
     * @param nextInvoiceViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceViDTO update(NextInvoiceViDTO nextInvoiceViDTO) {
        LOG.debug("Request to update NextInvoiceVi : {}", nextInvoiceViDTO);
        NextInvoiceVi nextInvoiceVi = nextInvoiceViMapper.toEntity(nextInvoiceViDTO);
        nextInvoiceVi = nextInvoiceViRepository.save(nextInvoiceVi);
        return nextInvoiceViMapper.toDto(nextInvoiceVi);
    }

    /**
     * Partially update a nextInvoiceVi.
     *
     * @param nextInvoiceViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceViDTO> partialUpdate(NextInvoiceViDTO nextInvoiceViDTO) {
        LOG.debug("Request to partially update NextInvoiceVi : {}", nextInvoiceViDTO);

        return nextInvoiceViRepository
            .findById(nextInvoiceViDTO.getId())
            .map(existingNextInvoiceVi -> {
                nextInvoiceViMapper.partialUpdate(existingNextInvoiceVi, nextInvoiceViDTO);

                return existingNextInvoiceVi;
            })
            .map(nextInvoiceViRepository::save)
            .map(nextInvoiceViMapper::toDto);
    }

    /**
     * Get one nextInvoiceVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceViDTO> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceVi : {}", id);
        return nextInvoiceViRepository.findById(id).map(nextInvoiceViMapper::toDto);
    }

    /**
     * Delete the nextInvoiceVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceVi : {}", id);
        nextInvoiceViRepository.deleteById(id);
    }
}
