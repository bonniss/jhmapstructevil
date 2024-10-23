package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentGamma;
import xyz.jhmapstruct.repository.NextPaymentGammaRepository;
import xyz.jhmapstruct.service.dto.NextPaymentGammaDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentGamma}.
 */
@Service
@Transactional
public class NextPaymentGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentGammaService.class);

    private final NextPaymentGammaRepository nextPaymentGammaRepository;

    private final NextPaymentGammaMapper nextPaymentGammaMapper;

    public NextPaymentGammaService(NextPaymentGammaRepository nextPaymentGammaRepository, NextPaymentGammaMapper nextPaymentGammaMapper) {
        this.nextPaymentGammaRepository = nextPaymentGammaRepository;
        this.nextPaymentGammaMapper = nextPaymentGammaMapper;
    }

    /**
     * Save a nextPaymentGamma.
     *
     * @param nextPaymentGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentGammaDTO save(NextPaymentGammaDTO nextPaymentGammaDTO) {
        LOG.debug("Request to save NextPaymentGamma : {}", nextPaymentGammaDTO);
        NextPaymentGamma nextPaymentGamma = nextPaymentGammaMapper.toEntity(nextPaymentGammaDTO);
        nextPaymentGamma = nextPaymentGammaRepository.save(nextPaymentGamma);
        return nextPaymentGammaMapper.toDto(nextPaymentGamma);
    }

    /**
     * Update a nextPaymentGamma.
     *
     * @param nextPaymentGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentGammaDTO update(NextPaymentGammaDTO nextPaymentGammaDTO) {
        LOG.debug("Request to update NextPaymentGamma : {}", nextPaymentGammaDTO);
        NextPaymentGamma nextPaymentGamma = nextPaymentGammaMapper.toEntity(nextPaymentGammaDTO);
        nextPaymentGamma = nextPaymentGammaRepository.save(nextPaymentGamma);
        return nextPaymentGammaMapper.toDto(nextPaymentGamma);
    }

    /**
     * Partially update a nextPaymentGamma.
     *
     * @param nextPaymentGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentGammaDTO> partialUpdate(NextPaymentGammaDTO nextPaymentGammaDTO) {
        LOG.debug("Request to partially update NextPaymentGamma : {}", nextPaymentGammaDTO);

        return nextPaymentGammaRepository
            .findById(nextPaymentGammaDTO.getId())
            .map(existingNextPaymentGamma -> {
                nextPaymentGammaMapper.partialUpdate(existingNextPaymentGamma, nextPaymentGammaDTO);

                return existingNextPaymentGamma;
            })
            .map(nextPaymentGammaRepository::save)
            .map(nextPaymentGammaMapper::toDto);
    }

    /**
     * Get one nextPaymentGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentGammaDTO> findOne(Long id) {
        LOG.debug("Request to get NextPaymentGamma : {}", id);
        return nextPaymentGammaRepository.findById(id).map(nextPaymentGammaMapper::toDto);
    }

    /**
     * Delete the nextPaymentGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentGamma : {}", id);
        nextPaymentGammaRepository.deleteById(id);
    }
}
