package ai.realworld.service;

import ai.realworld.domain.AlAlexType;
import ai.realworld.repository.AlAlexTypeRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlAlexType}.
 */
@Service
@Transactional
public class AlAlexTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(AlAlexTypeService.class);

    private final AlAlexTypeRepository alAlexTypeRepository;

    public AlAlexTypeService(AlAlexTypeRepository alAlexTypeRepository) {
        this.alAlexTypeRepository = alAlexTypeRepository;
    }

    /**
     * Save a alAlexType.
     *
     * @param alAlexType the entity to save.
     * @return the persisted entity.
     */
    public AlAlexType save(AlAlexType alAlexType) {
        LOG.debug("Request to save AlAlexType : {}", alAlexType);
        return alAlexTypeRepository.save(alAlexType);
    }

    /**
     * Update a alAlexType.
     *
     * @param alAlexType the entity to save.
     * @return the persisted entity.
     */
    public AlAlexType update(AlAlexType alAlexType) {
        LOG.debug("Request to update AlAlexType : {}", alAlexType);
        return alAlexTypeRepository.save(alAlexType);
    }

    /**
     * Partially update a alAlexType.
     *
     * @param alAlexType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlAlexType> partialUpdate(AlAlexType alAlexType) {
        LOG.debug("Request to partially update AlAlexType : {}", alAlexType);

        return alAlexTypeRepository
            .findById(alAlexType.getId())
            .map(existingAlAlexType -> {
                if (alAlexType.getName() != null) {
                    existingAlAlexType.setName(alAlexType.getName());
                }
                if (alAlexType.getDescription() != null) {
                    existingAlAlexType.setDescription(alAlexType.getDescription());
                }
                if (alAlexType.getCanDoRetail() != null) {
                    existingAlAlexType.setCanDoRetail(alAlexType.getCanDoRetail());
                }
                if (alAlexType.getIsOrgDivision() != null) {
                    existingAlAlexType.setIsOrgDivision(alAlexType.getIsOrgDivision());
                }
                if (alAlexType.getConfigJason() != null) {
                    existingAlAlexType.setConfigJason(alAlexType.getConfigJason());
                }
                if (alAlexType.getTreeDepth() != null) {
                    existingAlAlexType.setTreeDepth(alAlexType.getTreeDepth());
                }

                return existingAlAlexType;
            })
            .map(alAlexTypeRepository::save);
    }

    /**
     * Get one alAlexType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlAlexType> findOne(UUID id) {
        LOG.debug("Request to get AlAlexType : {}", id);
        return alAlexTypeRepository.findById(id);
    }

    /**
     * Delete the alAlexType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlAlexType : {}", id);
        alAlexTypeRepository.deleteById(id);
    }
}
