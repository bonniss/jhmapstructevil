package ai.realworld.service;

import ai.realworld.domain.AlPyuThomasWayneVi;
import ai.realworld.repository.AlPyuThomasWayneViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuThomasWayneVi}.
 */
@Service
@Transactional
public class AlPyuThomasWayneViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuThomasWayneViService.class);

    private final AlPyuThomasWayneViRepository alPyuThomasWayneViRepository;

    public AlPyuThomasWayneViService(AlPyuThomasWayneViRepository alPyuThomasWayneViRepository) {
        this.alPyuThomasWayneViRepository = alPyuThomasWayneViRepository;
    }

    /**
     * Save a alPyuThomasWayneVi.
     *
     * @param alPyuThomasWayneVi the entity to save.
     * @return the persisted entity.
     */
    public AlPyuThomasWayneVi save(AlPyuThomasWayneVi alPyuThomasWayneVi) {
        LOG.debug("Request to save AlPyuThomasWayneVi : {}", alPyuThomasWayneVi);
        return alPyuThomasWayneViRepository.save(alPyuThomasWayneVi);
    }

    /**
     * Update a alPyuThomasWayneVi.
     *
     * @param alPyuThomasWayneVi the entity to save.
     * @return the persisted entity.
     */
    public AlPyuThomasWayneVi update(AlPyuThomasWayneVi alPyuThomasWayneVi) {
        LOG.debug("Request to update AlPyuThomasWayneVi : {}", alPyuThomasWayneVi);
        return alPyuThomasWayneViRepository.save(alPyuThomasWayneVi);
    }

    /**
     * Partially update a alPyuThomasWayneVi.
     *
     * @param alPyuThomasWayneVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuThomasWayneVi> partialUpdate(AlPyuThomasWayneVi alPyuThomasWayneVi) {
        LOG.debug("Request to partially update AlPyuThomasWayneVi : {}", alPyuThomasWayneVi);

        return alPyuThomasWayneViRepository
            .findById(alPyuThomasWayneVi.getId())
            .map(existingAlPyuThomasWayneVi -> {
                if (alPyuThomasWayneVi.getRating() != null) {
                    existingAlPyuThomasWayneVi.setRating(alPyuThomasWayneVi.getRating());
                }
                if (alPyuThomasWayneVi.getComment() != null) {
                    existingAlPyuThomasWayneVi.setComment(alPyuThomasWayneVi.getComment());
                }

                return existingAlPyuThomasWayneVi;
            })
            .map(alPyuThomasWayneViRepository::save);
    }

    /**
     * Get one alPyuThomasWayneVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuThomasWayneVi> findOne(Long id) {
        LOG.debug("Request to get AlPyuThomasWayneVi : {}", id);
        return alPyuThomasWayneViRepository.findById(id);
    }

    /**
     * Delete the alPyuThomasWayneVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPyuThomasWayneVi : {}", id);
        alPyuThomasWayneViRepository.deleteById(id);
    }
}
