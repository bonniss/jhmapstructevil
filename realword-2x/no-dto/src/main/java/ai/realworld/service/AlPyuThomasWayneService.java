package ai.realworld.service;

import ai.realworld.domain.AlPyuThomasWayne;
import ai.realworld.repository.AlPyuThomasWayneRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuThomasWayne}.
 */
@Service
@Transactional
public class AlPyuThomasWayneService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuThomasWayneService.class);

    private final AlPyuThomasWayneRepository alPyuThomasWayneRepository;

    public AlPyuThomasWayneService(AlPyuThomasWayneRepository alPyuThomasWayneRepository) {
        this.alPyuThomasWayneRepository = alPyuThomasWayneRepository;
    }

    /**
     * Save a alPyuThomasWayne.
     *
     * @param alPyuThomasWayne the entity to save.
     * @return the persisted entity.
     */
    public AlPyuThomasWayne save(AlPyuThomasWayne alPyuThomasWayne) {
        LOG.debug("Request to save AlPyuThomasWayne : {}", alPyuThomasWayne);
        return alPyuThomasWayneRepository.save(alPyuThomasWayne);
    }

    /**
     * Update a alPyuThomasWayne.
     *
     * @param alPyuThomasWayne the entity to save.
     * @return the persisted entity.
     */
    public AlPyuThomasWayne update(AlPyuThomasWayne alPyuThomasWayne) {
        LOG.debug("Request to update AlPyuThomasWayne : {}", alPyuThomasWayne);
        return alPyuThomasWayneRepository.save(alPyuThomasWayne);
    }

    /**
     * Partially update a alPyuThomasWayne.
     *
     * @param alPyuThomasWayne the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuThomasWayne> partialUpdate(AlPyuThomasWayne alPyuThomasWayne) {
        LOG.debug("Request to partially update AlPyuThomasWayne : {}", alPyuThomasWayne);

        return alPyuThomasWayneRepository
            .findById(alPyuThomasWayne.getId())
            .map(existingAlPyuThomasWayne -> {
                if (alPyuThomasWayne.getRating() != null) {
                    existingAlPyuThomasWayne.setRating(alPyuThomasWayne.getRating());
                }
                if (alPyuThomasWayne.getComment() != null) {
                    existingAlPyuThomasWayne.setComment(alPyuThomasWayne.getComment());
                }

                return existingAlPyuThomasWayne;
            })
            .map(alPyuThomasWayneRepository::save);
    }

    /**
     * Get one alPyuThomasWayne by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuThomasWayne> findOne(Long id) {
        LOG.debug("Request to get AlPyuThomasWayne : {}", id);
        return alPyuThomasWayneRepository.findById(id);
    }

    /**
     * Delete the alPyuThomasWayne by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPyuThomasWayne : {}", id);
        alPyuThomasWayneRepository.deleteById(id);
    }
}
