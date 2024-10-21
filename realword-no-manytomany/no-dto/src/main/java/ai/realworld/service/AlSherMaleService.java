package ai.realworld.service;

import ai.realworld.domain.AlSherMale;
import ai.realworld.repository.AlSherMaleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlSherMale}.
 */
@Service
@Transactional
public class AlSherMaleService {

    private static final Logger LOG = LoggerFactory.getLogger(AlSherMaleService.class);

    private final AlSherMaleRepository alSherMaleRepository;

    public AlSherMaleService(AlSherMaleRepository alSherMaleRepository) {
        this.alSherMaleRepository = alSherMaleRepository;
    }

    /**
     * Save a alSherMale.
     *
     * @param alSherMale the entity to save.
     * @return the persisted entity.
     */
    public AlSherMale save(AlSherMale alSherMale) {
        LOG.debug("Request to save AlSherMale : {}", alSherMale);
        return alSherMaleRepository.save(alSherMale);
    }

    /**
     * Update a alSherMale.
     *
     * @param alSherMale the entity to save.
     * @return the persisted entity.
     */
    public AlSherMale update(AlSherMale alSherMale) {
        LOG.debug("Request to update AlSherMale : {}", alSherMale);
        return alSherMaleRepository.save(alSherMale);
    }

    /**
     * Partially update a alSherMale.
     *
     * @param alSherMale the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlSherMale> partialUpdate(AlSherMale alSherMale) {
        LOG.debug("Request to partially update AlSherMale : {}", alSherMale);

        return alSherMaleRepository
            .findById(alSherMale.getId())
            .map(existingAlSherMale -> {
                if (alSherMale.getDataSourceMappingType() != null) {
                    existingAlSherMale.setDataSourceMappingType(alSherMale.getDataSourceMappingType());
                }
                if (alSherMale.getKeyword() != null) {
                    existingAlSherMale.setKeyword(alSherMale.getKeyword());
                }
                if (alSherMale.getProperty() != null) {
                    existingAlSherMale.setProperty(alSherMale.getProperty());
                }
                if (alSherMale.getTitle() != null) {
                    existingAlSherMale.setTitle(alSherMale.getTitle());
                }

                return existingAlSherMale;
            })
            .map(alSherMaleRepository::save);
    }

    /**
     * Get one alSherMale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlSherMale> findOne(Long id) {
        LOG.debug("Request to get AlSherMale : {}", id);
        return alSherMaleRepository.findById(id);
    }

    /**
     * Delete the alSherMale by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlSherMale : {}", id);
        alSherMaleRepository.deleteById(id);
    }
}
