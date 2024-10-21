package ai.realworld.service;

import ai.realworld.domain.AlLexFergVi;
import ai.realworld.repository.AlLexFergViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLexFergVi}.
 */
@Service
@Transactional
public class AlLexFergViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLexFergViService.class);

    private final AlLexFergViRepository alLexFergViRepository;

    public AlLexFergViService(AlLexFergViRepository alLexFergViRepository) {
        this.alLexFergViRepository = alLexFergViRepository;
    }

    /**
     * Save a alLexFergVi.
     *
     * @param alLexFergVi the entity to save.
     * @return the persisted entity.
     */
    public AlLexFergVi save(AlLexFergVi alLexFergVi) {
        LOG.debug("Request to save AlLexFergVi : {}", alLexFergVi);
        return alLexFergViRepository.save(alLexFergVi);
    }

    /**
     * Update a alLexFergVi.
     *
     * @param alLexFergVi the entity to save.
     * @return the persisted entity.
     */
    public AlLexFergVi update(AlLexFergVi alLexFergVi) {
        LOG.debug("Request to update AlLexFergVi : {}", alLexFergVi);
        return alLexFergViRepository.save(alLexFergVi);
    }

    /**
     * Partially update a alLexFergVi.
     *
     * @param alLexFergVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLexFergVi> partialUpdate(AlLexFergVi alLexFergVi) {
        LOG.debug("Request to partially update AlLexFergVi : {}", alLexFergVi);

        return alLexFergViRepository
            .findById(alLexFergVi.getId())
            .map(existingAlLexFergVi -> {
                if (alLexFergVi.getTitle() != null) {
                    existingAlLexFergVi.setTitle(alLexFergVi.getTitle());
                }
                if (alLexFergVi.getSlug() != null) {
                    existingAlLexFergVi.setSlug(alLexFergVi.getSlug());
                }
                if (alLexFergVi.getSummary() != null) {
                    existingAlLexFergVi.setSummary(alLexFergVi.getSummary());
                }
                if (alLexFergVi.getContentHeitiga() != null) {
                    existingAlLexFergVi.setContentHeitiga(alLexFergVi.getContentHeitiga());
                }
                if (alLexFergVi.getPublicationStatus() != null) {
                    existingAlLexFergVi.setPublicationStatus(alLexFergVi.getPublicationStatus());
                }
                if (alLexFergVi.getPublishedDate() != null) {
                    existingAlLexFergVi.setPublishedDate(alLexFergVi.getPublishedDate());
                }

                return existingAlLexFergVi;
            })
            .map(alLexFergViRepository::save);
    }

    /**
     * Get one alLexFergVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLexFergVi> findOne(Long id) {
        LOG.debug("Request to get AlLexFergVi : {}", id);
        return alLexFergViRepository.findById(id);
    }

    /**
     * Delete the alLexFergVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlLexFergVi : {}", id);
        alLexFergViRepository.deleteById(id);
    }
}
