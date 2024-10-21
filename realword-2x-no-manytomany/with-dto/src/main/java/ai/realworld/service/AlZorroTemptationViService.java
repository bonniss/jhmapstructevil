package ai.realworld.service;

import ai.realworld.domain.AlZorroTemptationVi;
import ai.realworld.repository.AlZorroTemptationViRepository;
import ai.realworld.service.dto.AlZorroTemptationViDTO;
import ai.realworld.service.mapper.AlZorroTemptationViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlZorroTemptationVi}.
 */
@Service
@Transactional
public class AlZorroTemptationViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlZorroTemptationViService.class);

    private final AlZorroTemptationViRepository alZorroTemptationViRepository;

    private final AlZorroTemptationViMapper alZorroTemptationViMapper;

    public AlZorroTemptationViService(
        AlZorroTemptationViRepository alZorroTemptationViRepository,
        AlZorroTemptationViMapper alZorroTemptationViMapper
    ) {
        this.alZorroTemptationViRepository = alZorroTemptationViRepository;
        this.alZorroTemptationViMapper = alZorroTemptationViMapper;
    }

    /**
     * Save a alZorroTemptationVi.
     *
     * @param alZorroTemptationViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlZorroTemptationViDTO save(AlZorroTemptationViDTO alZorroTemptationViDTO) {
        LOG.debug("Request to save AlZorroTemptationVi : {}", alZorroTemptationViDTO);
        AlZorroTemptationVi alZorroTemptationVi = alZorroTemptationViMapper.toEntity(alZorroTemptationViDTO);
        alZorroTemptationVi = alZorroTemptationViRepository.save(alZorroTemptationVi);
        return alZorroTemptationViMapper.toDto(alZorroTemptationVi);
    }

    /**
     * Update a alZorroTemptationVi.
     *
     * @param alZorroTemptationViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlZorroTemptationViDTO update(AlZorroTemptationViDTO alZorroTemptationViDTO) {
        LOG.debug("Request to update AlZorroTemptationVi : {}", alZorroTemptationViDTO);
        AlZorroTemptationVi alZorroTemptationVi = alZorroTemptationViMapper.toEntity(alZorroTemptationViDTO);
        alZorroTemptationVi = alZorroTemptationViRepository.save(alZorroTemptationVi);
        return alZorroTemptationViMapper.toDto(alZorroTemptationVi);
    }

    /**
     * Partially update a alZorroTemptationVi.
     *
     * @param alZorroTemptationViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlZorroTemptationViDTO> partialUpdate(AlZorroTemptationViDTO alZorroTemptationViDTO) {
        LOG.debug("Request to partially update AlZorroTemptationVi : {}", alZorroTemptationViDTO);

        return alZorroTemptationViRepository
            .findById(alZorroTemptationViDTO.getId())
            .map(existingAlZorroTemptationVi -> {
                alZorroTemptationViMapper.partialUpdate(existingAlZorroTemptationVi, alZorroTemptationViDTO);

                return existingAlZorroTemptationVi;
            })
            .map(alZorroTemptationViRepository::save)
            .map(alZorroTemptationViMapper::toDto);
    }

    /**
     * Get one alZorroTemptationVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlZorroTemptationViDTO> findOne(Long id) {
        LOG.debug("Request to get AlZorroTemptationVi : {}", id);
        return alZorroTemptationViRepository.findById(id).map(alZorroTemptationViMapper::toDto);
    }

    /**
     * Delete the alZorroTemptationVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlZorroTemptationVi : {}", id);
        alZorroTemptationViRepository.deleteById(id);
    }
}
