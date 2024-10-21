package ai.realworld.service;

import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.JohnLennonRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.JohnLennon}.
 */
@Service
@Transactional
public class JohnLennonService {

    private static final Logger LOG = LoggerFactory.getLogger(JohnLennonService.class);

    private final JohnLennonRepository johnLennonRepository;

    public JohnLennonService(JohnLennonRepository johnLennonRepository) {
        this.johnLennonRepository = johnLennonRepository;
    }

    /**
     * Save a johnLennon.
     *
     * @param johnLennon the entity to save.
     * @return the persisted entity.
     */
    public JohnLennon save(JohnLennon johnLennon) {
        LOG.debug("Request to save JohnLennon : {}", johnLennon);
        return johnLennonRepository.save(johnLennon);
    }

    /**
     * Update a johnLennon.
     *
     * @param johnLennon the entity to save.
     * @return the persisted entity.
     */
    public JohnLennon update(JohnLennon johnLennon) {
        LOG.debug("Request to update JohnLennon : {}", johnLennon);
        return johnLennonRepository.save(johnLennon);
    }

    /**
     * Partially update a johnLennon.
     *
     * @param johnLennon the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JohnLennon> partialUpdate(JohnLennon johnLennon) {
        LOG.debug("Request to partially update JohnLennon : {}", johnLennon);

        return johnLennonRepository
            .findById(johnLennon.getId())
            .map(existingJohnLennon -> {
                if (johnLennon.getProvider() != null) {
                    existingJohnLennon.setProvider(johnLennon.getProvider());
                }
                if (johnLennon.getProviderAppId() != null) {
                    existingJohnLennon.setProviderAppId(johnLennon.getProviderAppId());
                }
                if (johnLennon.getName() != null) {
                    existingJohnLennon.setName(johnLennon.getName());
                }
                if (johnLennon.getSlug() != null) {
                    existingJohnLennon.setSlug(johnLennon.getSlug());
                }
                if (johnLennon.getIsEnabled() != null) {
                    existingJohnLennon.setIsEnabled(johnLennon.getIsEnabled());
                }

                return existingJohnLennon;
            })
            .map(johnLennonRepository::save);
    }

    /**
     * Get one johnLennon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JohnLennon> findOne(UUID id) {
        LOG.debug("Request to get JohnLennon : {}", id);
        return johnLennonRepository.findById(id);
    }

    /**
     * Delete the johnLennon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete JohnLennon : {}", id);
        johnLennonRepository.deleteById(id);
    }
}
