package ai.realworld.service;

import ai.realworld.domain.AlAppleVi;
import ai.realworld.repository.AlAppleViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlAppleVi}.
 */
@Service
@Transactional
public class AlAppleViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlAppleViService.class);

    private final AlAppleViRepository alAppleViRepository;

    public AlAppleViService(AlAppleViRepository alAppleViRepository) {
        this.alAppleViRepository = alAppleViRepository;
    }

    /**
     * Save a alAppleVi.
     *
     * @param alAppleVi the entity to save.
     * @return the persisted entity.
     */
    public AlAppleVi save(AlAppleVi alAppleVi) {
        LOG.debug("Request to save AlAppleVi : {}", alAppleVi);
        return alAppleViRepository.save(alAppleVi);
    }

    /**
     * Update a alAppleVi.
     *
     * @param alAppleVi the entity to save.
     * @return the persisted entity.
     */
    public AlAppleVi update(AlAppleVi alAppleVi) {
        LOG.debug("Request to update AlAppleVi : {}", alAppleVi);
        return alAppleViRepository.save(alAppleVi);
    }

    /**
     * Partially update a alAppleVi.
     *
     * @param alAppleVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlAppleVi> partialUpdate(AlAppleVi alAppleVi) {
        LOG.debug("Request to partially update AlAppleVi : {}", alAppleVi);

        return alAppleViRepository
            .findById(alAppleVi.getId())
            .map(existingAlAppleVi -> {
                if (alAppleVi.getName() != null) {
                    existingAlAppleVi.setName(alAppleVi.getName());
                }
                if (alAppleVi.getDescription() != null) {
                    existingAlAppleVi.setDescription(alAppleVi.getDescription());
                }
                if (alAppleVi.getHotline() != null) {
                    existingAlAppleVi.setHotline(alAppleVi.getHotline());
                }
                if (alAppleVi.getTaxCode() != null) {
                    existingAlAppleVi.setTaxCode(alAppleVi.getTaxCode());
                }
                if (alAppleVi.getContactsJason() != null) {
                    existingAlAppleVi.setContactsJason(alAppleVi.getContactsJason());
                }
                if (alAppleVi.getExtensionJason() != null) {
                    existingAlAppleVi.setExtensionJason(alAppleVi.getExtensionJason());
                }
                if (alAppleVi.getIsEnabled() != null) {
                    existingAlAppleVi.setIsEnabled(alAppleVi.getIsEnabled());
                }

                return existingAlAppleVi;
            })
            .map(alAppleViRepository::save);
    }

    /**
     * Get one alAppleVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlAppleVi> findOne(UUID id) {
        LOG.debug("Request to get AlAppleVi : {}", id);
        return alAppleViRepository.findById(id);
    }

    /**
     * Delete the alAppleVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlAppleVi : {}", id);
        alAppleViRepository.deleteById(id);
    }
}
