package ai.realworld.service;

import ai.realworld.domain.EdSheeran;
import ai.realworld.repository.EdSheeranRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.EdSheeran}.
 */
@Service
@Transactional
public class EdSheeranService {

    private static final Logger LOG = LoggerFactory.getLogger(EdSheeranService.class);

    private final EdSheeranRepository edSheeranRepository;

    public EdSheeranService(EdSheeranRepository edSheeranRepository) {
        this.edSheeranRepository = edSheeranRepository;
    }

    /**
     * Save a edSheeran.
     *
     * @param edSheeran the entity to save.
     * @return the persisted entity.
     */
    public EdSheeran save(EdSheeran edSheeran) {
        LOG.debug("Request to save EdSheeran : {}", edSheeran);
        return edSheeranRepository.save(edSheeran);
    }

    /**
     * Update a edSheeran.
     *
     * @param edSheeran the entity to save.
     * @return the persisted entity.
     */
    public EdSheeran update(EdSheeran edSheeran) {
        LOG.debug("Request to update EdSheeran : {}", edSheeran);
        return edSheeranRepository.save(edSheeran);
    }

    /**
     * Partially update a edSheeran.
     *
     * @param edSheeran the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EdSheeran> partialUpdate(EdSheeran edSheeran) {
        LOG.debug("Request to partially update EdSheeran : {}", edSheeran);

        return edSheeranRepository
            .findById(edSheeran.getId())
            .map(existingEdSheeran -> {
                if (edSheeran.getFamilyName() != null) {
                    existingEdSheeran.setFamilyName(edSheeran.getFamilyName());
                }
                if (edSheeran.getGivenName() != null) {
                    existingEdSheeran.setGivenName(edSheeran.getGivenName());
                }
                if (edSheeran.getDisplay() != null) {
                    existingEdSheeran.setDisplay(edSheeran.getDisplay());
                }
                if (edSheeran.getDob() != null) {
                    existingEdSheeran.setDob(edSheeran.getDob());
                }
                if (edSheeran.getGender() != null) {
                    existingEdSheeran.setGender(edSheeran.getGender());
                }
                if (edSheeran.getPhone() != null) {
                    existingEdSheeran.setPhone(edSheeran.getPhone());
                }
                if (edSheeran.getContactsJason() != null) {
                    existingEdSheeran.setContactsJason(edSheeran.getContactsJason());
                }
                if (edSheeran.getIsEnabled() != null) {
                    existingEdSheeran.setIsEnabled(edSheeran.getIsEnabled());
                }

                return existingEdSheeran;
            })
            .map(edSheeranRepository::save);
    }

    /**
     * Get all the edSheerans with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EdSheeran> findAllWithEagerRelationships(Pageable pageable) {
        return edSheeranRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one edSheeran by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EdSheeran> findOne(Long id) {
        LOG.debug("Request to get EdSheeran : {}", id);
        return edSheeranRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the edSheeran by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EdSheeran : {}", id);
        edSheeranRepository.deleteById(id);
    }
}
