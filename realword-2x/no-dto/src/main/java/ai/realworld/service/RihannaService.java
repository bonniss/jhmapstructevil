package ai.realworld.service;

import ai.realworld.domain.Rihanna;
import ai.realworld.repository.RihannaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.Rihanna}.
 */
@Service
@Transactional
public class RihannaService {

    private static final Logger LOG = LoggerFactory.getLogger(RihannaService.class);

    private final RihannaRepository rihannaRepository;

    public RihannaService(RihannaRepository rihannaRepository) {
        this.rihannaRepository = rihannaRepository;
    }

    /**
     * Save a rihanna.
     *
     * @param rihanna the entity to save.
     * @return the persisted entity.
     */
    public Rihanna save(Rihanna rihanna) {
        LOG.debug("Request to save Rihanna : {}", rihanna);
        return rihannaRepository.save(rihanna);
    }

    /**
     * Update a rihanna.
     *
     * @param rihanna the entity to save.
     * @return the persisted entity.
     */
    public Rihanna update(Rihanna rihanna) {
        LOG.debug("Request to update Rihanna : {}", rihanna);
        return rihannaRepository.save(rihanna);
    }

    /**
     * Partially update a rihanna.
     *
     * @param rihanna the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Rihanna> partialUpdate(Rihanna rihanna) {
        LOG.debug("Request to partially update Rihanna : {}", rihanna);

        return rihannaRepository
            .findById(rihanna.getId())
            .map(existingRihanna -> {
                if (rihanna.getName() != null) {
                    existingRihanna.setName(rihanna.getName());
                }
                if (rihanna.getDescription() != null) {
                    existingRihanna.setDescription(rihanna.getDescription());
                }
                if (rihanna.getPermissionGridJason() != null) {
                    existingRihanna.setPermissionGridJason(rihanna.getPermissionGridJason());
                }

                return existingRihanna;
            })
            .map(rihannaRepository::save);
    }

    /**
     * Get one rihanna by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Rihanna> findOne(Long id) {
        LOG.debug("Request to get Rihanna : {}", id);
        return rihannaRepository.findById(id);
    }

    /**
     * Delete the rihanna by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Rihanna : {}", id);
        rihannaRepository.deleteById(id);
    }
}
