package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentMi;
import xyz.jhmapstruct.repository.NextPaymentMiRepository;
import xyz.jhmapstruct.service.dto.NextPaymentMiDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentMi}.
 */
@Service
@Transactional
public class NextPaymentMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentMiService.class);

    private final NextPaymentMiRepository nextPaymentMiRepository;

    private final NextPaymentMiMapper nextPaymentMiMapper;

    public NextPaymentMiService(NextPaymentMiRepository nextPaymentMiRepository, NextPaymentMiMapper nextPaymentMiMapper) {
        this.nextPaymentMiRepository = nextPaymentMiRepository;
        this.nextPaymentMiMapper = nextPaymentMiMapper;
    }

    /**
     * Save a nextPaymentMi.
     *
     * @param nextPaymentMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentMiDTO save(NextPaymentMiDTO nextPaymentMiDTO) {
        LOG.debug("Request to save NextPaymentMi : {}", nextPaymentMiDTO);
        NextPaymentMi nextPaymentMi = nextPaymentMiMapper.toEntity(nextPaymentMiDTO);
        nextPaymentMi = nextPaymentMiRepository.save(nextPaymentMi);
        return nextPaymentMiMapper.toDto(nextPaymentMi);
    }

    /**
     * Update a nextPaymentMi.
     *
     * @param nextPaymentMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentMiDTO update(NextPaymentMiDTO nextPaymentMiDTO) {
        LOG.debug("Request to update NextPaymentMi : {}", nextPaymentMiDTO);
        NextPaymentMi nextPaymentMi = nextPaymentMiMapper.toEntity(nextPaymentMiDTO);
        nextPaymentMi = nextPaymentMiRepository.save(nextPaymentMi);
        return nextPaymentMiMapper.toDto(nextPaymentMi);
    }

    /**
     * Partially update a nextPaymentMi.
     *
     * @param nextPaymentMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentMiDTO> partialUpdate(NextPaymentMiDTO nextPaymentMiDTO) {
        LOG.debug("Request to partially update NextPaymentMi : {}", nextPaymentMiDTO);

        return nextPaymentMiRepository
            .findById(nextPaymentMiDTO.getId())
            .map(existingNextPaymentMi -> {
                nextPaymentMiMapper.partialUpdate(existingNextPaymentMi, nextPaymentMiDTO);

                return existingNextPaymentMi;
            })
            .map(nextPaymentMiRepository::save)
            .map(nextPaymentMiMapper::toDto);
    }

    /**
     * Get one nextPaymentMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextPaymentMi : {}", id);
        return nextPaymentMiRepository.findById(id).map(nextPaymentMiMapper::toDto);
    }

    /**
     * Delete the nextPaymentMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentMi : {}", id);
        nextPaymentMiRepository.deleteById(id);
    }
}
