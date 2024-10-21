package ai.realworld.service;

import ai.realworld.domain.AlProPro;
import ai.realworld.repository.AlProProRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlProPro}.
 */
@Service
@Transactional
public class AlProProService {

    private static final Logger LOG = LoggerFactory.getLogger(AlProProService.class);

    private final AlProProRepository alProProRepository;

    public AlProProService(AlProProRepository alProProRepository) {
        this.alProProRepository = alProProRepository;
    }

    /**
     * Save a alProPro.
     *
     * @param alProPro the entity to save.
     * @return the persisted entity.
     */
    public AlProPro save(AlProPro alProPro) {
        LOG.debug("Request to save AlProPro : {}", alProPro);
        return alProProRepository.save(alProPro);
    }

    /**
     * Update a alProPro.
     *
     * @param alProPro the entity to save.
     * @return the persisted entity.
     */
    public AlProPro update(AlProPro alProPro) {
        LOG.debug("Request to update AlProPro : {}", alProPro);
        return alProProRepository.save(alProPro);
    }

    /**
     * Partially update a alProPro.
     *
     * @param alProPro the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlProPro> partialUpdate(AlProPro alProPro) {
        LOG.debug("Request to partially update AlProPro : {}", alProPro);

        return alProProRepository
            .findById(alProPro.getId())
            .map(existingAlProPro -> {
                if (alProPro.getName() != null) {
                    existingAlProPro.setName(alProPro.getName());
                }
                if (alProPro.getDescriptionHeitiga() != null) {
                    existingAlProPro.setDescriptionHeitiga(alProPro.getDescriptionHeitiga());
                }
                if (alProPro.getPropertyType() != null) {
                    existingAlProPro.setPropertyType(alProPro.getPropertyType());
                }
                if (alProPro.getAreaInSquareMeter() != null) {
                    existingAlProPro.setAreaInSquareMeter(alProPro.getAreaInSquareMeter());
                }
                if (alProPro.getNumberOfAdults() != null) {
                    existingAlProPro.setNumberOfAdults(alProPro.getNumberOfAdults());
                }
                if (alProPro.getNumberOfPreschoolers() != null) {
                    existingAlProPro.setNumberOfPreschoolers(alProPro.getNumberOfPreschoolers());
                }
                if (alProPro.getNumberOfChildren() != null) {
                    existingAlProPro.setNumberOfChildren(alProPro.getNumberOfChildren());
                }
                if (alProPro.getNumberOfRooms() != null) {
                    existingAlProPro.setNumberOfRooms(alProPro.getNumberOfRooms());
                }
                if (alProPro.getNumberOfFloors() != null) {
                    existingAlProPro.setNumberOfFloors(alProPro.getNumberOfFloors());
                }
                if (alProPro.getBedSize() != null) {
                    existingAlProPro.setBedSize(alProPro.getBedSize());
                }
                if (alProPro.getIsEnabled() != null) {
                    existingAlProPro.setIsEnabled(alProPro.getIsEnabled());
                }

                return existingAlProPro;
            })
            .map(alProProRepository::save);
    }

    /**
     * Get one alProPro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlProPro> findOne(UUID id) {
        LOG.debug("Request to get AlProPro : {}", id);
        return alProProRepository.findById(id);
    }

    /**
     * Delete the alProPro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlProPro : {}", id);
        alProProRepository.deleteById(id);
    }
}
