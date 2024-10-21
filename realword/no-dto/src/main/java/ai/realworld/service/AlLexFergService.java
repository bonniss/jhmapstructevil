package ai.realworld.service;

import ai.realworld.domain.AlLexFerg;
import ai.realworld.repository.AlLexFergRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLexFerg}.
 */
@Service
@Transactional
public class AlLexFergService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLexFergService.class);

    private final AlLexFergRepository alLexFergRepository;

    public AlLexFergService(AlLexFergRepository alLexFergRepository) {
        this.alLexFergRepository = alLexFergRepository;
    }

    /**
     * Save a alLexFerg.
     *
     * @param alLexFerg the entity to save.
     * @return the persisted entity.
     */
    public AlLexFerg save(AlLexFerg alLexFerg) {
        LOG.debug("Request to save AlLexFerg : {}", alLexFerg);
        return alLexFergRepository.save(alLexFerg);
    }

    /**
     * Update a alLexFerg.
     *
     * @param alLexFerg the entity to save.
     * @return the persisted entity.
     */
    public AlLexFerg update(AlLexFerg alLexFerg) {
        LOG.debug("Request to update AlLexFerg : {}", alLexFerg);
        return alLexFergRepository.save(alLexFerg);
    }

    /**
     * Partially update a alLexFerg.
     *
     * @param alLexFerg the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLexFerg> partialUpdate(AlLexFerg alLexFerg) {
        LOG.debug("Request to partially update AlLexFerg : {}", alLexFerg);

        return alLexFergRepository
            .findById(alLexFerg.getId())
            .map(existingAlLexFerg -> {
                if (alLexFerg.getTitle() != null) {
                    existingAlLexFerg.setTitle(alLexFerg.getTitle());
                }
                if (alLexFerg.getSlug() != null) {
                    existingAlLexFerg.setSlug(alLexFerg.getSlug());
                }
                if (alLexFerg.getSummary() != null) {
                    existingAlLexFerg.setSummary(alLexFerg.getSummary());
                }
                if (alLexFerg.getContentHeitiga() != null) {
                    existingAlLexFerg.setContentHeitiga(alLexFerg.getContentHeitiga());
                }
                if (alLexFerg.getPublicationStatus() != null) {
                    existingAlLexFerg.setPublicationStatus(alLexFerg.getPublicationStatus());
                }
                if (alLexFerg.getPublishedDate() != null) {
                    existingAlLexFerg.setPublishedDate(alLexFerg.getPublishedDate());
                }

                return existingAlLexFerg;
            })
            .map(alLexFergRepository::save);
    }

    /**
     * Get all the alLexFergs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AlLexFerg> findAllWithEagerRelationships(Pageable pageable) {
        return alLexFergRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one alLexFerg by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLexFerg> findOne(Long id) {
        LOG.debug("Request to get AlLexFerg : {}", id);
        return alLexFergRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the alLexFerg by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlLexFerg : {}", id);
        alLexFergRepository.deleteById(id);
    }
}
