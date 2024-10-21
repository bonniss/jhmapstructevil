package ai.realworld.service;

import ai.realworld.domain.EdSheeranVi;
import ai.realworld.repository.EdSheeranViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.EdSheeranVi}.
 */
@Service
@Transactional
public class EdSheeranViService {

    private static final Logger LOG = LoggerFactory.getLogger(EdSheeranViService.class);

    private final EdSheeranViRepository edSheeranViRepository;

    public EdSheeranViService(EdSheeranViRepository edSheeranViRepository) {
        this.edSheeranViRepository = edSheeranViRepository;
    }

    /**
     * Save a edSheeranVi.
     *
     * @param edSheeranVi the entity to save.
     * @return the persisted entity.
     */
    public EdSheeranVi save(EdSheeranVi edSheeranVi) {
        LOG.debug("Request to save EdSheeranVi : {}", edSheeranVi);
        return edSheeranViRepository.save(edSheeranVi);
    }

    /**
     * Update a edSheeranVi.
     *
     * @param edSheeranVi the entity to save.
     * @return the persisted entity.
     */
    public EdSheeranVi update(EdSheeranVi edSheeranVi) {
        LOG.debug("Request to update EdSheeranVi : {}", edSheeranVi);
        return edSheeranViRepository.save(edSheeranVi);
    }

    /**
     * Partially update a edSheeranVi.
     *
     * @param edSheeranVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EdSheeranVi> partialUpdate(EdSheeranVi edSheeranVi) {
        LOG.debug("Request to partially update EdSheeranVi : {}", edSheeranVi);

        return edSheeranViRepository
            .findById(edSheeranVi.getId())
            .map(existingEdSheeranVi -> {
                if (edSheeranVi.getFamilyName() != null) {
                    existingEdSheeranVi.setFamilyName(edSheeranVi.getFamilyName());
                }
                if (edSheeranVi.getGivenName() != null) {
                    existingEdSheeranVi.setGivenName(edSheeranVi.getGivenName());
                }
                if (edSheeranVi.getDisplay() != null) {
                    existingEdSheeranVi.setDisplay(edSheeranVi.getDisplay());
                }
                if (edSheeranVi.getDob() != null) {
                    existingEdSheeranVi.setDob(edSheeranVi.getDob());
                }
                if (edSheeranVi.getGender() != null) {
                    existingEdSheeranVi.setGender(edSheeranVi.getGender());
                }
                if (edSheeranVi.getPhone() != null) {
                    existingEdSheeranVi.setPhone(edSheeranVi.getPhone());
                }
                if (edSheeranVi.getContactsJason() != null) {
                    existingEdSheeranVi.setContactsJason(edSheeranVi.getContactsJason());
                }
                if (edSheeranVi.getIsEnabled() != null) {
                    existingEdSheeranVi.setIsEnabled(edSheeranVi.getIsEnabled());
                }

                return existingEdSheeranVi;
            })
            .map(edSheeranViRepository::save);
    }

    /**
     * Get all the edSheeranVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EdSheeranVi> findAllWithEagerRelationships(Pageable pageable) {
        return edSheeranViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one edSheeranVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EdSheeranVi> findOne(Long id) {
        LOG.debug("Request to get EdSheeranVi : {}", id);
        return edSheeranViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the edSheeranVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EdSheeranVi : {}", id);
        edSheeranViRepository.deleteById(id);
    }
}
