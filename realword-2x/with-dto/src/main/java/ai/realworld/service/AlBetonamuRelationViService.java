package ai.realworld.service;

import ai.realworld.domain.AlBetonamuRelationVi;
import ai.realworld.repository.AlBetonamuRelationViRepository;
import ai.realworld.service.dto.AlBetonamuRelationViDTO;
import ai.realworld.service.mapper.AlBetonamuRelationViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlBetonamuRelationVi}.
 */
@Service
@Transactional
public class AlBetonamuRelationViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlBetonamuRelationViService.class);

    private final AlBetonamuRelationViRepository alBetonamuRelationViRepository;

    private final AlBetonamuRelationViMapper alBetonamuRelationViMapper;

    public AlBetonamuRelationViService(
        AlBetonamuRelationViRepository alBetonamuRelationViRepository,
        AlBetonamuRelationViMapper alBetonamuRelationViMapper
    ) {
        this.alBetonamuRelationViRepository = alBetonamuRelationViRepository;
        this.alBetonamuRelationViMapper = alBetonamuRelationViMapper;
    }

    /**
     * Save a alBetonamuRelationVi.
     *
     * @param alBetonamuRelationViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlBetonamuRelationViDTO save(AlBetonamuRelationViDTO alBetonamuRelationViDTO) {
        LOG.debug("Request to save AlBetonamuRelationVi : {}", alBetonamuRelationViDTO);
        AlBetonamuRelationVi alBetonamuRelationVi = alBetonamuRelationViMapper.toEntity(alBetonamuRelationViDTO);
        alBetonamuRelationVi = alBetonamuRelationViRepository.save(alBetonamuRelationVi);
        return alBetonamuRelationViMapper.toDto(alBetonamuRelationVi);
    }

    /**
     * Update a alBetonamuRelationVi.
     *
     * @param alBetonamuRelationViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlBetonamuRelationViDTO update(AlBetonamuRelationViDTO alBetonamuRelationViDTO) {
        LOG.debug("Request to update AlBetonamuRelationVi : {}", alBetonamuRelationViDTO);
        AlBetonamuRelationVi alBetonamuRelationVi = alBetonamuRelationViMapper.toEntity(alBetonamuRelationViDTO);
        alBetonamuRelationVi = alBetonamuRelationViRepository.save(alBetonamuRelationVi);
        return alBetonamuRelationViMapper.toDto(alBetonamuRelationVi);
    }

    /**
     * Partially update a alBetonamuRelationVi.
     *
     * @param alBetonamuRelationViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlBetonamuRelationViDTO> partialUpdate(AlBetonamuRelationViDTO alBetonamuRelationViDTO) {
        LOG.debug("Request to partially update AlBetonamuRelationVi : {}", alBetonamuRelationViDTO);

        return alBetonamuRelationViRepository
            .findById(alBetonamuRelationViDTO.getId())
            .map(existingAlBetonamuRelationVi -> {
                alBetonamuRelationViMapper.partialUpdate(existingAlBetonamuRelationVi, alBetonamuRelationViDTO);

                return existingAlBetonamuRelationVi;
            })
            .map(alBetonamuRelationViRepository::save)
            .map(alBetonamuRelationViMapper::toDto);
    }

    /**
     * Get one alBetonamuRelationVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlBetonamuRelationViDTO> findOne(Long id) {
        LOG.debug("Request to get AlBetonamuRelationVi : {}", id);
        return alBetonamuRelationViRepository.findById(id).map(alBetonamuRelationViMapper::toDto);
    }

    /**
     * Delete the alBetonamuRelationVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlBetonamuRelationVi : {}", id);
        alBetonamuRelationViRepository.deleteById(id);
    }
}
