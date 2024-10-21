package ai.realworld.service;

import ai.realworld.domain.AlCatalina;
import ai.realworld.repository.AlCatalinaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlCatalina}.
 */
@Service
@Transactional
public class AlCatalinaService {

    private static final Logger LOG = LoggerFactory.getLogger(AlCatalinaService.class);

    private final AlCatalinaRepository alCatalinaRepository;

    public AlCatalinaService(AlCatalinaRepository alCatalinaRepository) {
        this.alCatalinaRepository = alCatalinaRepository;
    }

    /**
     * Save a alCatalina.
     *
     * @param alCatalina the entity to save.
     * @return the persisted entity.
     */
    public AlCatalina save(AlCatalina alCatalina) {
        LOG.debug("Request to save AlCatalina : {}", alCatalina);
        return alCatalinaRepository.save(alCatalina);
    }

    /**
     * Update a alCatalina.
     *
     * @param alCatalina the entity to save.
     * @return the persisted entity.
     */
    public AlCatalina update(AlCatalina alCatalina) {
        LOG.debug("Request to update AlCatalina : {}", alCatalina);
        return alCatalinaRepository.save(alCatalina);
    }

    /**
     * Partially update a alCatalina.
     *
     * @param alCatalina the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlCatalina> partialUpdate(AlCatalina alCatalina) {
        LOG.debug("Request to partially update AlCatalina : {}", alCatalina);

        return alCatalinaRepository
            .findById(alCatalina.getId())
            .map(existingAlCatalina -> {
                if (alCatalina.getName() != null) {
                    existingAlCatalina.setName(alCatalina.getName());
                }
                if (alCatalina.getDescription() != null) {
                    existingAlCatalina.setDescription(alCatalina.getDescription());
                }
                if (alCatalina.getTreeDepth() != null) {
                    existingAlCatalina.setTreeDepth(alCatalina.getTreeDepth());
                }

                return existingAlCatalina;
            })
            .map(alCatalinaRepository::save);
    }

    /**
     * Get one alCatalina by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlCatalina> findOne(Long id) {
        LOG.debug("Request to get AlCatalina : {}", id);
        return alCatalinaRepository.findById(id);
    }

    /**
     * Delete the alCatalina by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlCatalina : {}", id);
        alCatalinaRepository.deleteById(id);
    }
}
