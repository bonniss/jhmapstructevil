package ai.realworld.service;

import ai.realworld.domain.OlMaster;
import ai.realworld.repository.OlMasterRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.OlMaster}.
 */
@Service
@Transactional
public class OlMasterService {

    private static final Logger LOG = LoggerFactory.getLogger(OlMasterService.class);

    private final OlMasterRepository olMasterRepository;

    public OlMasterService(OlMasterRepository olMasterRepository) {
        this.olMasterRepository = olMasterRepository;
    }

    /**
     * Save a olMaster.
     *
     * @param olMaster the entity to save.
     * @return the persisted entity.
     */
    public OlMaster save(OlMaster olMaster) {
        LOG.debug("Request to save OlMaster : {}", olMaster);
        return olMasterRepository.save(olMaster);
    }

    /**
     * Update a olMaster.
     *
     * @param olMaster the entity to save.
     * @return the persisted entity.
     */
    public OlMaster update(OlMaster olMaster) {
        LOG.debug("Request to update OlMaster : {}", olMaster);
        return olMasterRepository.save(olMaster);
    }

    /**
     * Partially update a olMaster.
     *
     * @param olMaster the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OlMaster> partialUpdate(OlMaster olMaster) {
        LOG.debug("Request to partially update OlMaster : {}", olMaster);

        return olMasterRepository
            .findById(olMaster.getId())
            .map(existingOlMaster -> {
                if (olMaster.getName() != null) {
                    existingOlMaster.setName(olMaster.getName());
                }
                if (olMaster.getSlug() != null) {
                    existingOlMaster.setSlug(olMaster.getSlug());
                }
                if (olMaster.getDescriptionHeitiga() != null) {
                    existingOlMaster.setDescriptionHeitiga(olMaster.getDescriptionHeitiga());
                }
                if (olMaster.getBusinessType() != null) {
                    existingOlMaster.setBusinessType(olMaster.getBusinessType());
                }
                if (olMaster.getEmail() != null) {
                    existingOlMaster.setEmail(olMaster.getEmail());
                }
                if (olMaster.getHotline() != null) {
                    existingOlMaster.setHotline(olMaster.getHotline());
                }
                if (olMaster.getTaxCode() != null) {
                    existingOlMaster.setTaxCode(olMaster.getTaxCode());
                }
                if (olMaster.getContactsJason() != null) {
                    existingOlMaster.setContactsJason(olMaster.getContactsJason());
                }
                if (olMaster.getExtensionJason() != null) {
                    existingOlMaster.setExtensionJason(olMaster.getExtensionJason());
                }
                if (olMaster.getIsEnabled() != null) {
                    existingOlMaster.setIsEnabled(olMaster.getIsEnabled());
                }

                return existingOlMaster;
            })
            .map(olMasterRepository::save);
    }

    /**
     * Get one olMaster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OlMaster> findOne(UUID id) {
        LOG.debug("Request to get OlMaster : {}", id);
        return olMasterRepository.findById(id);
    }

    /**
     * Delete the olMaster by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete OlMaster : {}", id);
        olMasterRepository.deleteById(id);
    }
}
