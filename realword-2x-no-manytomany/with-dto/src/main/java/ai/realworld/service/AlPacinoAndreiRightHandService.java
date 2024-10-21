package ai.realworld.service;

import ai.realworld.domain.AlPacinoAndreiRightHand;
import ai.realworld.repository.AlPacinoAndreiRightHandRepository;
import ai.realworld.service.dto.AlPacinoAndreiRightHandDTO;
import ai.realworld.service.mapper.AlPacinoAndreiRightHandMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoAndreiRightHand}.
 */
@Service
@Transactional
public class AlPacinoAndreiRightHandService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoAndreiRightHandService.class);

    private final AlPacinoAndreiRightHandRepository alPacinoAndreiRightHandRepository;

    private final AlPacinoAndreiRightHandMapper alPacinoAndreiRightHandMapper;

    public AlPacinoAndreiRightHandService(
        AlPacinoAndreiRightHandRepository alPacinoAndreiRightHandRepository,
        AlPacinoAndreiRightHandMapper alPacinoAndreiRightHandMapper
    ) {
        this.alPacinoAndreiRightHandRepository = alPacinoAndreiRightHandRepository;
        this.alPacinoAndreiRightHandMapper = alPacinoAndreiRightHandMapper;
    }

    /**
     * Save a alPacinoAndreiRightHand.
     *
     * @param alPacinoAndreiRightHandDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoAndreiRightHandDTO save(AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO) {
        LOG.debug("Request to save AlPacinoAndreiRightHand : {}", alPacinoAndreiRightHandDTO);
        AlPacinoAndreiRightHand alPacinoAndreiRightHand = alPacinoAndreiRightHandMapper.toEntity(alPacinoAndreiRightHandDTO);
        alPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.save(alPacinoAndreiRightHand);
        return alPacinoAndreiRightHandMapper.toDto(alPacinoAndreiRightHand);
    }

    /**
     * Update a alPacinoAndreiRightHand.
     *
     * @param alPacinoAndreiRightHandDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoAndreiRightHandDTO update(AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO) {
        LOG.debug("Request to update AlPacinoAndreiRightHand : {}", alPacinoAndreiRightHandDTO);
        AlPacinoAndreiRightHand alPacinoAndreiRightHand = alPacinoAndreiRightHandMapper.toEntity(alPacinoAndreiRightHandDTO);
        alPacinoAndreiRightHand = alPacinoAndreiRightHandRepository.save(alPacinoAndreiRightHand);
        return alPacinoAndreiRightHandMapper.toDto(alPacinoAndreiRightHand);
    }

    /**
     * Partially update a alPacinoAndreiRightHand.
     *
     * @param alPacinoAndreiRightHandDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoAndreiRightHandDTO> partialUpdate(AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO) {
        LOG.debug("Request to partially update AlPacinoAndreiRightHand : {}", alPacinoAndreiRightHandDTO);

        return alPacinoAndreiRightHandRepository
            .findById(alPacinoAndreiRightHandDTO.getId())
            .map(existingAlPacinoAndreiRightHand -> {
                alPacinoAndreiRightHandMapper.partialUpdate(existingAlPacinoAndreiRightHand, alPacinoAndreiRightHandDTO);

                return existingAlPacinoAndreiRightHand;
            })
            .map(alPacinoAndreiRightHandRepository::save)
            .map(alPacinoAndreiRightHandMapper::toDto);
    }

    /**
     * Get one alPacinoAndreiRightHand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoAndreiRightHandDTO> findOne(Long id) {
        LOG.debug("Request to get AlPacinoAndreiRightHand : {}", id);
        return alPacinoAndreiRightHandRepository.findById(id).map(alPacinoAndreiRightHandMapper::toDto);
    }

    /**
     * Delete the alPacinoAndreiRightHand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPacinoAndreiRightHand : {}", id);
        alPacinoAndreiRightHandRepository.deleteById(id);
    }
}
