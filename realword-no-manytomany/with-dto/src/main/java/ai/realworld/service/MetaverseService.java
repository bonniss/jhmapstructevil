package ai.realworld.service;

import ai.realworld.domain.Metaverse;
import ai.realworld.repository.MetaverseRepository;
import ai.realworld.service.dto.MetaverseDTO;
import ai.realworld.service.mapper.MetaverseMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.Metaverse}.
 */
@Service
@Transactional
public class MetaverseService {

    private static final Logger LOG = LoggerFactory.getLogger(MetaverseService.class);

    private final MetaverseRepository metaverseRepository;

    private final MetaverseMapper metaverseMapper;

    public MetaverseService(MetaverseRepository metaverseRepository, MetaverseMapper metaverseMapper) {
        this.metaverseRepository = metaverseRepository;
        this.metaverseMapper = metaverseMapper;
    }

    /**
     * Save a metaverse.
     *
     * @param metaverseDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaverseDTO save(MetaverseDTO metaverseDTO) {
        LOG.debug("Request to save Metaverse : {}", metaverseDTO);
        Metaverse metaverse = metaverseMapper.toEntity(metaverseDTO);
        metaverse = metaverseRepository.save(metaverse);
        return metaverseMapper.toDto(metaverse);
    }

    /**
     * Update a metaverse.
     *
     * @param metaverseDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaverseDTO update(MetaverseDTO metaverseDTO) {
        LOG.debug("Request to update Metaverse : {}", metaverseDTO);
        Metaverse metaverse = metaverseMapper.toEntity(metaverseDTO);
        metaverse = metaverseRepository.save(metaverse);
        return metaverseMapper.toDto(metaverse);
    }

    /**
     * Partially update a metaverse.
     *
     * @param metaverseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MetaverseDTO> partialUpdate(MetaverseDTO metaverseDTO) {
        LOG.debug("Request to partially update Metaverse : {}", metaverseDTO);

        return metaverseRepository
            .findById(metaverseDTO.getId())
            .map(existingMetaverse -> {
                metaverseMapper.partialUpdate(existingMetaverse, metaverseDTO);

                return existingMetaverse;
            })
            .map(metaverseRepository::save)
            .map(metaverseMapper::toDto);
    }

    /**
     * Get one metaverse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MetaverseDTO> findOne(Long id) {
        LOG.debug("Request to get Metaverse : {}", id);
        return metaverseRepository.findById(id).map(metaverseMapper::toDto);
    }

    /**
     * Delete the metaverse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Metaverse : {}", id);
        metaverseRepository.deleteById(id);
    }
}
