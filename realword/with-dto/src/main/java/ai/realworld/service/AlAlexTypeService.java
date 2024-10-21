package ai.realworld.service;

import ai.realworld.domain.AlAlexType;
import ai.realworld.repository.AlAlexTypeRepository;
import ai.realworld.service.dto.AlAlexTypeDTO;
import ai.realworld.service.mapper.AlAlexTypeMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlAlexType}.
 */
@Service
@Transactional
public class AlAlexTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(AlAlexTypeService.class);

    private final AlAlexTypeRepository alAlexTypeRepository;

    private final AlAlexTypeMapper alAlexTypeMapper;

    public AlAlexTypeService(AlAlexTypeRepository alAlexTypeRepository, AlAlexTypeMapper alAlexTypeMapper) {
        this.alAlexTypeRepository = alAlexTypeRepository;
        this.alAlexTypeMapper = alAlexTypeMapper;
    }

    /**
     * Save a alAlexType.
     *
     * @param alAlexTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public AlAlexTypeDTO save(AlAlexTypeDTO alAlexTypeDTO) {
        LOG.debug("Request to save AlAlexType : {}", alAlexTypeDTO);
        AlAlexType alAlexType = alAlexTypeMapper.toEntity(alAlexTypeDTO);
        alAlexType = alAlexTypeRepository.save(alAlexType);
        return alAlexTypeMapper.toDto(alAlexType);
    }

    /**
     * Update a alAlexType.
     *
     * @param alAlexTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public AlAlexTypeDTO update(AlAlexTypeDTO alAlexTypeDTO) {
        LOG.debug("Request to update AlAlexType : {}", alAlexTypeDTO);
        AlAlexType alAlexType = alAlexTypeMapper.toEntity(alAlexTypeDTO);
        alAlexType = alAlexTypeRepository.save(alAlexType);
        return alAlexTypeMapper.toDto(alAlexType);
    }

    /**
     * Partially update a alAlexType.
     *
     * @param alAlexTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlAlexTypeDTO> partialUpdate(AlAlexTypeDTO alAlexTypeDTO) {
        LOG.debug("Request to partially update AlAlexType : {}", alAlexTypeDTO);

        return alAlexTypeRepository
            .findById(alAlexTypeDTO.getId())
            .map(existingAlAlexType -> {
                alAlexTypeMapper.partialUpdate(existingAlAlexType, alAlexTypeDTO);

                return existingAlAlexType;
            })
            .map(alAlexTypeRepository::save)
            .map(alAlexTypeMapper::toDto);
    }

    /**
     * Get one alAlexType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlAlexTypeDTO> findOne(UUID id) {
        LOG.debug("Request to get AlAlexType : {}", id);
        return alAlexTypeRepository.findById(id).map(alAlexTypeMapper::toDto);
    }

    /**
     * Delete the alAlexType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlAlexType : {}", id);
        alAlexTypeRepository.deleteById(id);
    }
}
