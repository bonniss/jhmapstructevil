package ai.realworld.service;

import ai.realworld.domain.Magharetti;
import ai.realworld.repository.MagharettiRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.Magharetti}.
 */
@Service
@Transactional
public class MagharettiService {

    private static final Logger LOG = LoggerFactory.getLogger(MagharettiService.class);

    private final MagharettiRepository magharettiRepository;

    public MagharettiService(MagharettiRepository magharettiRepository) {
        this.magharettiRepository = magharettiRepository;
    }

    /**
     * Save a magharetti.
     *
     * @param magharetti the entity to save.
     * @return the persisted entity.
     */
    public Magharetti save(Magharetti magharetti) {
        LOG.debug("Request to save Magharetti : {}", magharetti);
        return magharettiRepository.save(magharetti);
    }

    /**
     * Update a magharetti.
     *
     * @param magharetti the entity to save.
     * @return the persisted entity.
     */
    public Magharetti update(Magharetti magharetti) {
        LOG.debug("Request to update Magharetti : {}", magharetti);
        return magharettiRepository.save(magharetti);
    }

    /**
     * Partially update a magharetti.
     *
     * @param magharetti the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Magharetti> partialUpdate(Magharetti magharetti) {
        LOG.debug("Request to partially update Magharetti : {}", magharetti);

        return magharettiRepository
            .findById(magharetti.getId())
            .map(existingMagharetti -> {
                if (magharetti.getName() != null) {
                    existingMagharetti.setName(magharetti.getName());
                }
                if (magharetti.getLabel() != null) {
                    existingMagharetti.setLabel(magharetti.getLabel());
                }
                if (magharetti.getType() != null) {
                    existingMagharetti.setType(magharetti.getType());
                }

                return existingMagharetti;
            })
            .map(magharettiRepository::save);
    }

    /**
     * Get one magharetti by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Magharetti> findOne(Long id) {
        LOG.debug("Request to get Magharetti : {}", id);
        return magharettiRepository.findById(id);
    }

    /**
     * Delete the magharetti by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Magharetti : {}", id);
        magharettiRepository.deleteById(id);
    }
}
