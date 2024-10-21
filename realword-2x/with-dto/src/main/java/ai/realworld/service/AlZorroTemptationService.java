package ai.realworld.service;

import ai.realworld.domain.AlZorroTemptation;
import ai.realworld.repository.AlZorroTemptationRepository;
import ai.realworld.service.dto.AlZorroTemptationDTO;
import ai.realworld.service.mapper.AlZorroTemptationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlZorroTemptation}.
 */
@Service
@Transactional
public class AlZorroTemptationService {

    private static final Logger LOG = LoggerFactory.getLogger(AlZorroTemptationService.class);

    private final AlZorroTemptationRepository alZorroTemptationRepository;

    private final AlZorroTemptationMapper alZorroTemptationMapper;

    public AlZorroTemptationService(
        AlZorroTemptationRepository alZorroTemptationRepository,
        AlZorroTemptationMapper alZorroTemptationMapper
    ) {
        this.alZorroTemptationRepository = alZorroTemptationRepository;
        this.alZorroTemptationMapper = alZorroTemptationMapper;
    }

    /**
     * Save a alZorroTemptation.
     *
     * @param alZorroTemptationDTO the entity to save.
     * @return the persisted entity.
     */
    public AlZorroTemptationDTO save(AlZorroTemptationDTO alZorroTemptationDTO) {
        LOG.debug("Request to save AlZorroTemptation : {}", alZorroTemptationDTO);
        AlZorroTemptation alZorroTemptation = alZorroTemptationMapper.toEntity(alZorroTemptationDTO);
        alZorroTemptation = alZorroTemptationRepository.save(alZorroTemptation);
        return alZorroTemptationMapper.toDto(alZorroTemptation);
    }

    /**
     * Update a alZorroTemptation.
     *
     * @param alZorroTemptationDTO the entity to save.
     * @return the persisted entity.
     */
    public AlZorroTemptationDTO update(AlZorroTemptationDTO alZorroTemptationDTO) {
        LOG.debug("Request to update AlZorroTemptation : {}", alZorroTemptationDTO);
        AlZorroTemptation alZorroTemptation = alZorroTemptationMapper.toEntity(alZorroTemptationDTO);
        alZorroTemptation = alZorroTemptationRepository.save(alZorroTemptation);
        return alZorroTemptationMapper.toDto(alZorroTemptation);
    }

    /**
     * Partially update a alZorroTemptation.
     *
     * @param alZorroTemptationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlZorroTemptationDTO> partialUpdate(AlZorroTemptationDTO alZorroTemptationDTO) {
        LOG.debug("Request to partially update AlZorroTemptation : {}", alZorroTemptationDTO);

        return alZorroTemptationRepository
            .findById(alZorroTemptationDTO.getId())
            .map(existingAlZorroTemptation -> {
                alZorroTemptationMapper.partialUpdate(existingAlZorroTemptation, alZorroTemptationDTO);

                return existingAlZorroTemptation;
            })
            .map(alZorroTemptationRepository::save)
            .map(alZorroTemptationMapper::toDto);
    }

    /**
     * Get one alZorroTemptation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlZorroTemptationDTO> findOne(Long id) {
        LOG.debug("Request to get AlZorroTemptation : {}", id);
        return alZorroTemptationRepository.findById(id).map(alZorroTemptationMapper::toDto);
    }

    /**
     * Delete the alZorroTemptation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlZorroTemptation : {}", id);
        alZorroTemptationRepository.deleteById(id);
    }
}
