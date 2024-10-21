package ai.realworld.service;

import ai.realworld.domain.Metaverse;
import ai.realworld.repository.MetaverseRepository;
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

    public MetaverseService(MetaverseRepository metaverseRepository) {
        this.metaverseRepository = metaverseRepository;
    }

    /**
     * Save a metaverse.
     *
     * @param metaverse the entity to save.
     * @return the persisted entity.
     */
    public Metaverse save(Metaverse metaverse) {
        LOG.debug("Request to save Metaverse : {}", metaverse);
        return metaverseRepository.save(metaverse);
    }

    /**
     * Update a metaverse.
     *
     * @param metaverse the entity to save.
     * @return the persisted entity.
     */
    public Metaverse update(Metaverse metaverse) {
        LOG.debug("Request to update Metaverse : {}", metaverse);
        return metaverseRepository.save(metaverse);
    }

    /**
     * Partially update a metaverse.
     *
     * @param metaverse the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Metaverse> partialUpdate(Metaverse metaverse) {
        LOG.debug("Request to partially update Metaverse : {}", metaverse);

        return metaverseRepository
            .findById(metaverse.getId())
            .map(existingMetaverse -> {
                if (metaverse.getFilename() != null) {
                    existingMetaverse.setFilename(metaverse.getFilename());
                }
                if (metaverse.getContentType() != null) {
                    existingMetaverse.setContentType(metaverse.getContentType());
                }
                if (metaverse.getFileExt() != null) {
                    existingMetaverse.setFileExt(metaverse.getFileExt());
                }
                if (metaverse.getFileSize() != null) {
                    existingMetaverse.setFileSize(metaverse.getFileSize());
                }
                if (metaverse.getFileUrl() != null) {
                    existingMetaverse.setFileUrl(metaverse.getFileUrl());
                }
                if (metaverse.getThumbnailUrl() != null) {
                    existingMetaverse.setThumbnailUrl(metaverse.getThumbnailUrl());
                }
                if (metaverse.getBlurhash() != null) {
                    existingMetaverse.setBlurhash(metaverse.getBlurhash());
                }
                if (metaverse.getObjectName() != null) {
                    existingMetaverse.setObjectName(metaverse.getObjectName());
                }
                if (metaverse.getObjectMetaJason() != null) {
                    existingMetaverse.setObjectMetaJason(metaverse.getObjectMetaJason());
                }
                if (metaverse.getUrlLifespanInSeconds() != null) {
                    existingMetaverse.setUrlLifespanInSeconds(metaverse.getUrlLifespanInSeconds());
                }
                if (metaverse.getUrlExpiredDate() != null) {
                    existingMetaverse.setUrlExpiredDate(metaverse.getUrlExpiredDate());
                }
                if (metaverse.getAutoRenewUrl() != null) {
                    existingMetaverse.setAutoRenewUrl(metaverse.getAutoRenewUrl());
                }
                if (metaverse.getIsEnabled() != null) {
                    existingMetaverse.setIsEnabled(metaverse.getIsEnabled());
                }

                return existingMetaverse;
            })
            .map(metaverseRepository::save);
    }

    /**
     * Get one metaverse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Metaverse> findOne(Long id) {
        LOG.debug("Request to get Metaverse : {}", id);
        return metaverseRepository.findById(id);
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
