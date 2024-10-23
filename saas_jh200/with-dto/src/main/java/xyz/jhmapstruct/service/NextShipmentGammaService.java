package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentGamma;
import xyz.jhmapstruct.repository.NextShipmentGammaRepository;
import xyz.jhmapstruct.service.dto.NextShipmentGammaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentGamma}.
 */
@Service
@Transactional
public class NextShipmentGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentGammaService.class);

    private final NextShipmentGammaRepository nextShipmentGammaRepository;

    private final NextShipmentGammaMapper nextShipmentGammaMapper;

    public NextShipmentGammaService(
        NextShipmentGammaRepository nextShipmentGammaRepository,
        NextShipmentGammaMapper nextShipmentGammaMapper
    ) {
        this.nextShipmentGammaRepository = nextShipmentGammaRepository;
        this.nextShipmentGammaMapper = nextShipmentGammaMapper;
    }

    /**
     * Save a nextShipmentGamma.
     *
     * @param nextShipmentGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentGammaDTO save(NextShipmentGammaDTO nextShipmentGammaDTO) {
        LOG.debug("Request to save NextShipmentGamma : {}", nextShipmentGammaDTO);
        NextShipmentGamma nextShipmentGamma = nextShipmentGammaMapper.toEntity(nextShipmentGammaDTO);
        nextShipmentGamma = nextShipmentGammaRepository.save(nextShipmentGamma);
        return nextShipmentGammaMapper.toDto(nextShipmentGamma);
    }

    /**
     * Update a nextShipmentGamma.
     *
     * @param nextShipmentGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentGammaDTO update(NextShipmentGammaDTO nextShipmentGammaDTO) {
        LOG.debug("Request to update NextShipmentGamma : {}", nextShipmentGammaDTO);
        NextShipmentGamma nextShipmentGamma = nextShipmentGammaMapper.toEntity(nextShipmentGammaDTO);
        nextShipmentGamma = nextShipmentGammaRepository.save(nextShipmentGamma);
        return nextShipmentGammaMapper.toDto(nextShipmentGamma);
    }

    /**
     * Partially update a nextShipmentGamma.
     *
     * @param nextShipmentGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentGammaDTO> partialUpdate(NextShipmentGammaDTO nextShipmentGammaDTO) {
        LOG.debug("Request to partially update NextShipmentGamma : {}", nextShipmentGammaDTO);

        return nextShipmentGammaRepository
            .findById(nextShipmentGammaDTO.getId())
            .map(existingNextShipmentGamma -> {
                nextShipmentGammaMapper.partialUpdate(existingNextShipmentGamma, nextShipmentGammaDTO);

                return existingNextShipmentGamma;
            })
            .map(nextShipmentGammaRepository::save)
            .map(nextShipmentGammaMapper::toDto);
    }

    /**
     * Get one nextShipmentGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentGammaDTO> findOne(Long id) {
        LOG.debug("Request to get NextShipmentGamma : {}", id);
        return nextShipmentGammaRepository.findById(id).map(nextShipmentGammaMapper::toDto);
    }

    /**
     * Delete the nextShipmentGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentGamma : {}", id);
        nextShipmentGammaRepository.deleteById(id);
    }
}
