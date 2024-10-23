package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentVi;
import xyz.jhmapstruct.repository.NextPaymentViRepository;
import xyz.jhmapstruct.service.dto.NextPaymentViDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentVi}.
 */
@Service
@Transactional
public class NextPaymentViService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentViService.class);

    private final NextPaymentViRepository nextPaymentViRepository;

    private final NextPaymentViMapper nextPaymentViMapper;

    public NextPaymentViService(NextPaymentViRepository nextPaymentViRepository, NextPaymentViMapper nextPaymentViMapper) {
        this.nextPaymentViRepository = nextPaymentViRepository;
        this.nextPaymentViMapper = nextPaymentViMapper;
    }

    /**
     * Save a nextPaymentVi.
     *
     * @param nextPaymentViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentViDTO save(NextPaymentViDTO nextPaymentViDTO) {
        LOG.debug("Request to save NextPaymentVi : {}", nextPaymentViDTO);
        NextPaymentVi nextPaymentVi = nextPaymentViMapper.toEntity(nextPaymentViDTO);
        nextPaymentVi = nextPaymentViRepository.save(nextPaymentVi);
        return nextPaymentViMapper.toDto(nextPaymentVi);
    }

    /**
     * Update a nextPaymentVi.
     *
     * @param nextPaymentViDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentViDTO update(NextPaymentViDTO nextPaymentViDTO) {
        LOG.debug("Request to update NextPaymentVi : {}", nextPaymentViDTO);
        NextPaymentVi nextPaymentVi = nextPaymentViMapper.toEntity(nextPaymentViDTO);
        nextPaymentVi = nextPaymentViRepository.save(nextPaymentVi);
        return nextPaymentViMapper.toDto(nextPaymentVi);
    }

    /**
     * Partially update a nextPaymentVi.
     *
     * @param nextPaymentViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentViDTO> partialUpdate(NextPaymentViDTO nextPaymentViDTO) {
        LOG.debug("Request to partially update NextPaymentVi : {}", nextPaymentViDTO);

        return nextPaymentViRepository
            .findById(nextPaymentViDTO.getId())
            .map(existingNextPaymentVi -> {
                nextPaymentViMapper.partialUpdate(existingNextPaymentVi, nextPaymentViDTO);

                return existingNextPaymentVi;
            })
            .map(nextPaymentViRepository::save)
            .map(nextPaymentViMapper::toDto);
    }

    /**
     * Get one nextPaymentVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentViDTO> findOne(Long id) {
        LOG.debug("Request to get NextPaymentVi : {}", id);
        return nextPaymentViRepository.findById(id).map(nextPaymentViMapper::toDto);
    }

    /**
     * Delete the nextPaymentVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentVi : {}", id);
        nextPaymentViRepository.deleteById(id);
    }
}
