package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextShipmentSigma;
import xyz.jhmapstruct.repository.NextShipmentSigmaRepository;
import xyz.jhmapstruct.service.dto.NextShipmentSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextShipmentSigma}.
 */
@Service
@Transactional
public class NextShipmentSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentSigmaService.class);

    private final NextShipmentSigmaRepository nextShipmentSigmaRepository;

    private final NextShipmentSigmaMapper nextShipmentSigmaMapper;

    public NextShipmentSigmaService(
        NextShipmentSigmaRepository nextShipmentSigmaRepository,
        NextShipmentSigmaMapper nextShipmentSigmaMapper
    ) {
        this.nextShipmentSigmaRepository = nextShipmentSigmaRepository;
        this.nextShipmentSigmaMapper = nextShipmentSigmaMapper;
    }

    /**
     * Save a nextShipmentSigma.
     *
     * @param nextShipmentSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentSigmaDTO save(NextShipmentSigmaDTO nextShipmentSigmaDTO) {
        LOG.debug("Request to save NextShipmentSigma : {}", nextShipmentSigmaDTO);
        NextShipmentSigma nextShipmentSigma = nextShipmentSigmaMapper.toEntity(nextShipmentSigmaDTO);
        nextShipmentSigma = nextShipmentSigmaRepository.save(nextShipmentSigma);
        return nextShipmentSigmaMapper.toDto(nextShipmentSigma);
    }

    /**
     * Update a nextShipmentSigma.
     *
     * @param nextShipmentSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextShipmentSigmaDTO update(NextShipmentSigmaDTO nextShipmentSigmaDTO) {
        LOG.debug("Request to update NextShipmentSigma : {}", nextShipmentSigmaDTO);
        NextShipmentSigma nextShipmentSigma = nextShipmentSigmaMapper.toEntity(nextShipmentSigmaDTO);
        nextShipmentSigma = nextShipmentSigmaRepository.save(nextShipmentSigma);
        return nextShipmentSigmaMapper.toDto(nextShipmentSigma);
    }

    /**
     * Partially update a nextShipmentSigma.
     *
     * @param nextShipmentSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextShipmentSigmaDTO> partialUpdate(NextShipmentSigmaDTO nextShipmentSigmaDTO) {
        LOG.debug("Request to partially update NextShipmentSigma : {}", nextShipmentSigmaDTO);

        return nextShipmentSigmaRepository
            .findById(nextShipmentSigmaDTO.getId())
            .map(existingNextShipmentSigma -> {
                nextShipmentSigmaMapper.partialUpdate(existingNextShipmentSigma, nextShipmentSigmaDTO);

                return existingNextShipmentSigma;
            })
            .map(nextShipmentSigmaRepository::save)
            .map(nextShipmentSigmaMapper::toDto);
    }

    /**
     * Get one nextShipmentSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextShipmentSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get NextShipmentSigma : {}", id);
        return nextShipmentSigmaRepository.findById(id).map(nextShipmentSigmaMapper::toDto);
    }

    /**
     * Delete the nextShipmentSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextShipmentSigma : {}", id);
        nextShipmentSigmaRepository.deleteById(id);
    }
}
