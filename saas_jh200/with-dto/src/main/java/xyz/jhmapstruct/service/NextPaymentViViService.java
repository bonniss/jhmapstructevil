package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentViVi;
import xyz.jhmapstruct.repository.NextPaymentViViRepository;
import xyz.jhmapstruct.service.dto.NextPaymentViViDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentViVi}.
 */
@Service
@Transactional
public class NextPaymentViViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentViViService.class);

    private final NextPaymentViViRepository nextPaymentViViRepository;

    private final NextPaymentViViMapper nextPaymentViViMapper;

    public NextPaymentViViService(NextPaymentViViRepository nextPaymentViViRepository, NextPaymentViViMapper nextPaymentViViMapper) {
        this.nextPaymentViViRepository = nextPaymentViViRepository;
        this.nextPaymentViViMapper = nextPaymentViViMapper;
    }

    /**
     * Save a nextPaymentViVi.
     *
     * @param nextPaymentViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentViViDTO save(NextPaymentViViDTO nextPaymentViViDTO) {
        LOG.debug("Request to save NextPaymentViVi : {}", nextPaymentViViDTO);
        NextPaymentViVi nextPaymentViVi = nextPaymentViViMapper.toEntity(nextPaymentViViDTO);
        nextPaymentViVi = nextPaymentViViRepository.save(nextPaymentViVi);
        return nextPaymentViViMapper.toDto(nextPaymentViVi);
    }

    /**
     * Update a nextPaymentViVi.
     *
     * @param nextPaymentViViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentViViDTO update(NextPaymentViViDTO nextPaymentViViDTO) {
        LOG.debug("Request to update NextPaymentViVi : {}", nextPaymentViViDTO);
        NextPaymentViVi nextPaymentViVi = nextPaymentViViMapper.toEntity(nextPaymentViViDTO);
        nextPaymentViVi = nextPaymentViViRepository.save(nextPaymentViVi);
        return nextPaymentViViMapper.toDto(nextPaymentViVi);
    }

    /**
     * Partially update a nextPaymentViVi.
     *
     * @param nextPaymentViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentViViDTO> partialUpdate(NextPaymentViViDTO nextPaymentViViDTO) {
        LOG.debug("Request to partially update NextPaymentViVi : {}", nextPaymentViViDTO);

        return nextPaymentViViRepository
            .findById(nextPaymentViViDTO.getId())
            .map(existingNextPaymentViVi -> {
                nextPaymentViViMapper.partialUpdate(existingNextPaymentViVi, nextPaymentViViDTO);

                return existingNextPaymentViVi;
            })
            .map(nextPaymentViViRepository::save)
            .map(nextPaymentViViMapper::toDto);
    }

    /**
     * Get one nextPaymentViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentViViDTO> findOne(Long id) {
        LOG.debug("Request to get NextPaymentViVi : {}", id);
        return nextPaymentViViRepository.findById(id).map(nextPaymentViViMapper::toDto);
    }

    /**
     * Delete the nextPaymentViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentViVi : {}", id);
        nextPaymentViViRepository.deleteById(id);
    }
}
