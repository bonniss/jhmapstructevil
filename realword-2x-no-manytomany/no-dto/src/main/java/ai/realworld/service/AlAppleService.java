package ai.realworld.service;

import ai.realworld.domain.AlApple;
import ai.realworld.repository.AlAppleRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlApple}.
 */
@Service
@Transactional
public class AlAppleService {

    private static final Logger LOG = LoggerFactory.getLogger(AlAppleService.class);

    private final AlAppleRepository alAppleRepository;

    public AlAppleService(AlAppleRepository alAppleRepository) {
        this.alAppleRepository = alAppleRepository;
    }

    /**
     * Save a alApple.
     *
     * @param alApple the entity to save.
     * @return the persisted entity.
     */
    public AlApple save(AlApple alApple) {
        LOG.debug("Request to save AlApple : {}", alApple);
        return alAppleRepository.save(alApple);
    }

    /**
     * Update a alApple.
     *
     * @param alApple the entity to save.
     * @return the persisted entity.
     */
    public AlApple update(AlApple alApple) {
        LOG.debug("Request to update AlApple : {}", alApple);
        return alAppleRepository.save(alApple);
    }

    /**
     * Partially update a alApple.
     *
     * @param alApple the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlApple> partialUpdate(AlApple alApple) {
        LOG.debug("Request to partially update AlApple : {}", alApple);

        return alAppleRepository
            .findById(alApple.getId())
            .map(existingAlApple -> {
                if (alApple.getName() != null) {
                    existingAlApple.setName(alApple.getName());
                }
                if (alApple.getDescription() != null) {
                    existingAlApple.setDescription(alApple.getDescription());
                }
                if (alApple.getHotline() != null) {
                    existingAlApple.setHotline(alApple.getHotline());
                }
                if (alApple.getTaxCode() != null) {
                    existingAlApple.setTaxCode(alApple.getTaxCode());
                }
                if (alApple.getContactsJason() != null) {
                    existingAlApple.setContactsJason(alApple.getContactsJason());
                }
                if (alApple.getExtensionJason() != null) {
                    existingAlApple.setExtensionJason(alApple.getExtensionJason());
                }
                if (alApple.getIsEnabled() != null) {
                    existingAlApple.setIsEnabled(alApple.getIsEnabled());
                }

                return existingAlApple;
            })
            .map(alAppleRepository::save);
    }

    /**
     * Get one alApple by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlApple> findOne(UUID id) {
        LOG.debug("Request to get AlApple : {}", id);
        return alAppleRepository.findById(id);
    }

    /**
     * Delete the alApple by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlApple : {}", id);
        alAppleRepository.deleteById(id);
    }
}
