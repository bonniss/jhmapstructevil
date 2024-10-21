package ai.realworld.service;

import ai.realworld.domain.MagharettiVi;
import ai.realworld.repository.MagharettiViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.MagharettiVi}.
 */
@Service
@Transactional
public class MagharettiViService {

    private static final Logger LOG = LoggerFactory.getLogger(MagharettiViService.class);

    private final MagharettiViRepository magharettiViRepository;

    public MagharettiViService(MagharettiViRepository magharettiViRepository) {
        this.magharettiViRepository = magharettiViRepository;
    }

    /**
     * Save a magharettiVi.
     *
     * @param magharettiVi the entity to save.
     * @return the persisted entity.
     */
    public MagharettiVi save(MagharettiVi magharettiVi) {
        LOG.debug("Request to save MagharettiVi : {}", magharettiVi);
        return magharettiViRepository.save(magharettiVi);
    }

    /**
     * Update a magharettiVi.
     *
     * @param magharettiVi the entity to save.
     * @return the persisted entity.
     */
    public MagharettiVi update(MagharettiVi magharettiVi) {
        LOG.debug("Request to update MagharettiVi : {}", magharettiVi);
        return magharettiViRepository.save(magharettiVi);
    }

    /**
     * Partially update a magharettiVi.
     *
     * @param magharettiVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MagharettiVi> partialUpdate(MagharettiVi magharettiVi) {
        LOG.debug("Request to partially update MagharettiVi : {}", magharettiVi);

        return magharettiViRepository
            .findById(magharettiVi.getId())
            .map(existingMagharettiVi -> {
                if (magharettiVi.getName() != null) {
                    existingMagharettiVi.setName(magharettiVi.getName());
                }
                if (magharettiVi.getLabel() != null) {
                    existingMagharettiVi.setLabel(magharettiVi.getLabel());
                }
                if (magharettiVi.getType() != null) {
                    existingMagharettiVi.setType(magharettiVi.getType());
                }

                return existingMagharettiVi;
            })
            .map(magharettiViRepository::save);
    }

    /**
     * Get one magharettiVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MagharettiVi> findOne(Long id) {
        LOG.debug("Request to get MagharettiVi : {}", id);
        return magharettiViRepository.findById(id);
    }

    /**
     * Delete the magharettiVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MagharettiVi : {}", id);
        magharettiViRepository.deleteById(id);
    }
}
