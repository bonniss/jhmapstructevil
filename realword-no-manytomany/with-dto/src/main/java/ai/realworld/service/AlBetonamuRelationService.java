package ai.realworld.service;

import ai.realworld.domain.AlBetonamuRelation;
import ai.realworld.repository.AlBetonamuRelationRepository;
import ai.realworld.service.dto.AlBetonamuRelationDTO;
import ai.realworld.service.mapper.AlBetonamuRelationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlBetonamuRelation}.
 */
@Service
@Transactional
public class AlBetonamuRelationService {

    private static final Logger LOG = LoggerFactory.getLogger(AlBetonamuRelationService.class);

    private final AlBetonamuRelationRepository alBetonamuRelationRepository;

    private final AlBetonamuRelationMapper alBetonamuRelationMapper;

    public AlBetonamuRelationService(
        AlBetonamuRelationRepository alBetonamuRelationRepository,
        AlBetonamuRelationMapper alBetonamuRelationMapper
    ) {
        this.alBetonamuRelationRepository = alBetonamuRelationRepository;
        this.alBetonamuRelationMapper = alBetonamuRelationMapper;
    }

    /**
     * Save a alBetonamuRelation.
     *
     * @param alBetonamuRelationDTO the entity to save.
     * @return the persisted entity.
     */
    public AlBetonamuRelationDTO save(AlBetonamuRelationDTO alBetonamuRelationDTO) {
        LOG.debug("Request to save AlBetonamuRelation : {}", alBetonamuRelationDTO);
        AlBetonamuRelation alBetonamuRelation = alBetonamuRelationMapper.toEntity(alBetonamuRelationDTO);
        alBetonamuRelation = alBetonamuRelationRepository.save(alBetonamuRelation);
        return alBetonamuRelationMapper.toDto(alBetonamuRelation);
    }

    /**
     * Update a alBetonamuRelation.
     *
     * @param alBetonamuRelationDTO the entity to save.
     * @return the persisted entity.
     */
    public AlBetonamuRelationDTO update(AlBetonamuRelationDTO alBetonamuRelationDTO) {
        LOG.debug("Request to update AlBetonamuRelation : {}", alBetonamuRelationDTO);
        AlBetonamuRelation alBetonamuRelation = alBetonamuRelationMapper.toEntity(alBetonamuRelationDTO);
        alBetonamuRelation = alBetonamuRelationRepository.save(alBetonamuRelation);
        return alBetonamuRelationMapper.toDto(alBetonamuRelation);
    }

    /**
     * Partially update a alBetonamuRelation.
     *
     * @param alBetonamuRelationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlBetonamuRelationDTO> partialUpdate(AlBetonamuRelationDTO alBetonamuRelationDTO) {
        LOG.debug("Request to partially update AlBetonamuRelation : {}", alBetonamuRelationDTO);

        return alBetonamuRelationRepository
            .findById(alBetonamuRelationDTO.getId())
            .map(existingAlBetonamuRelation -> {
                alBetonamuRelationMapper.partialUpdate(existingAlBetonamuRelation, alBetonamuRelationDTO);

                return existingAlBetonamuRelation;
            })
            .map(alBetonamuRelationRepository::save)
            .map(alBetonamuRelationMapper::toDto);
    }

    /**
     * Get one alBetonamuRelation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlBetonamuRelationDTO> findOne(Long id) {
        LOG.debug("Request to get AlBetonamuRelation : {}", id);
        return alBetonamuRelationRepository.findById(id).map(alBetonamuRelationMapper::toDto);
    }

    /**
     * Delete the alBetonamuRelation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlBetonamuRelation : {}", id);
        alBetonamuRelationRepository.deleteById(id);
    }
}
