package ai.realworld.service;

import ai.realworld.domain.AlProProVi;
import ai.realworld.repository.AlProProViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlProProVi}.
 */
@Service
@Transactional
public class AlProProViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlProProViService.class);

    private final AlProProViRepository alProProViRepository;

    public AlProProViService(AlProProViRepository alProProViRepository) {
        this.alProProViRepository = alProProViRepository;
    }

    /**
     * Save a alProProVi.
     *
     * @param alProProVi the entity to save.
     * @return the persisted entity.
     */
    public AlProProVi save(AlProProVi alProProVi) {
        LOG.debug("Request to save AlProProVi : {}", alProProVi);
        return alProProViRepository.save(alProProVi);
    }

    /**
     * Update a alProProVi.
     *
     * @param alProProVi the entity to save.
     * @return the persisted entity.
     */
    public AlProProVi update(AlProProVi alProProVi) {
        LOG.debug("Request to update AlProProVi : {}", alProProVi);
        return alProProViRepository.save(alProProVi);
    }

    /**
     * Partially update a alProProVi.
     *
     * @param alProProVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlProProVi> partialUpdate(AlProProVi alProProVi) {
        LOG.debug("Request to partially update AlProProVi : {}", alProProVi);

        return alProProViRepository
            .findById(alProProVi.getId())
            .map(existingAlProProVi -> {
                if (alProProVi.getName() != null) {
                    existingAlProProVi.setName(alProProVi.getName());
                }
                if (alProProVi.getDescriptionHeitiga() != null) {
                    existingAlProProVi.setDescriptionHeitiga(alProProVi.getDescriptionHeitiga());
                }
                if (alProProVi.getPropertyType() != null) {
                    existingAlProProVi.setPropertyType(alProProVi.getPropertyType());
                }
                if (alProProVi.getAreaInSquareMeter() != null) {
                    existingAlProProVi.setAreaInSquareMeter(alProProVi.getAreaInSquareMeter());
                }
                if (alProProVi.getNumberOfAdults() != null) {
                    existingAlProProVi.setNumberOfAdults(alProProVi.getNumberOfAdults());
                }
                if (alProProVi.getNumberOfPreschoolers() != null) {
                    existingAlProProVi.setNumberOfPreschoolers(alProProVi.getNumberOfPreschoolers());
                }
                if (alProProVi.getNumberOfChildren() != null) {
                    existingAlProProVi.setNumberOfChildren(alProProVi.getNumberOfChildren());
                }
                if (alProProVi.getNumberOfRooms() != null) {
                    existingAlProProVi.setNumberOfRooms(alProProVi.getNumberOfRooms());
                }
                if (alProProVi.getNumberOfFloors() != null) {
                    existingAlProProVi.setNumberOfFloors(alProProVi.getNumberOfFloors());
                }
                if (alProProVi.getBedSize() != null) {
                    existingAlProProVi.setBedSize(alProProVi.getBedSize());
                }
                if (alProProVi.getIsEnabled() != null) {
                    existingAlProProVi.setIsEnabled(alProProVi.getIsEnabled());
                }

                return existingAlProProVi;
            })
            .map(alProProViRepository::save);
    }

    /**
     * Get one alProProVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlProProVi> findOne(UUID id) {
        LOG.debug("Request to get AlProProVi : {}", id);
        return alProProViRepository.findById(id);
    }

    /**
     * Delete the alProProVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlProProVi : {}", id);
        alProProViRepository.deleteById(id);
    }
}
