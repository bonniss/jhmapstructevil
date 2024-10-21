package ai.realworld.service;

import ai.realworld.domain.AlAlexTypeVi;
import ai.realworld.repository.AlAlexTypeViRepository;
import ai.realworld.service.dto.AlAlexTypeViDTO;
import ai.realworld.service.mapper.AlAlexTypeViMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlAlexTypeVi}.
 */
@Service
@Transactional
public class AlAlexTypeViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlAlexTypeViService.class);

    private final AlAlexTypeViRepository alAlexTypeViRepository;

    private final AlAlexTypeViMapper alAlexTypeViMapper;

    public AlAlexTypeViService(AlAlexTypeViRepository alAlexTypeViRepository, AlAlexTypeViMapper alAlexTypeViMapper) {
        this.alAlexTypeViRepository = alAlexTypeViRepository;
        this.alAlexTypeViMapper = alAlexTypeViMapper;
    }

    /**
     * Save a alAlexTypeVi.
     *
     * @param alAlexTypeViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlAlexTypeViDTO save(AlAlexTypeViDTO alAlexTypeViDTO) {
        LOG.debug("Request to save AlAlexTypeVi : {}", alAlexTypeViDTO);
        AlAlexTypeVi alAlexTypeVi = alAlexTypeViMapper.toEntity(alAlexTypeViDTO);
        alAlexTypeVi = alAlexTypeViRepository.save(alAlexTypeVi);
        return alAlexTypeViMapper.toDto(alAlexTypeVi);
    }

    /**
     * Update a alAlexTypeVi.
     *
     * @param alAlexTypeViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlAlexTypeViDTO update(AlAlexTypeViDTO alAlexTypeViDTO) {
        LOG.debug("Request to update AlAlexTypeVi : {}", alAlexTypeViDTO);
        AlAlexTypeVi alAlexTypeVi = alAlexTypeViMapper.toEntity(alAlexTypeViDTO);
        alAlexTypeVi = alAlexTypeViRepository.save(alAlexTypeVi);
        return alAlexTypeViMapper.toDto(alAlexTypeVi);
    }

    /**
     * Partially update a alAlexTypeVi.
     *
     * @param alAlexTypeViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlAlexTypeViDTO> partialUpdate(AlAlexTypeViDTO alAlexTypeViDTO) {
        LOG.debug("Request to partially update AlAlexTypeVi : {}", alAlexTypeViDTO);

        return alAlexTypeViRepository
            .findById(alAlexTypeViDTO.getId())
            .map(existingAlAlexTypeVi -> {
                alAlexTypeViMapper.partialUpdate(existingAlAlexTypeVi, alAlexTypeViDTO);

                return existingAlAlexTypeVi;
            })
            .map(alAlexTypeViRepository::save)
            .map(alAlexTypeViMapper::toDto);
    }

    /**
     * Get one alAlexTypeVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlAlexTypeViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlAlexTypeVi : {}", id);
        return alAlexTypeViRepository.findById(id).map(alAlexTypeViMapper::toDto);
    }

    /**
     * Delete the alAlexTypeVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlAlexTypeVi : {}", id);
        alAlexTypeViRepository.deleteById(id);
    }
}
