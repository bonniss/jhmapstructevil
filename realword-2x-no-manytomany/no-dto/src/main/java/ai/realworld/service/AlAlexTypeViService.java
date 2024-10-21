package ai.realworld.service;

import ai.realworld.domain.AlAlexTypeVi;
import ai.realworld.repository.AlAlexTypeViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlAlexTypeVi}.
 */
@Service
@Transactional
public class AlAlexTypeViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlAlexTypeViService.class);

    private final AlAlexTypeViRepository alAlexTypeViRepository;

    public AlAlexTypeViService(AlAlexTypeViRepository alAlexTypeViRepository) {
        this.alAlexTypeViRepository = alAlexTypeViRepository;
    }

    /**
     * Save a alAlexTypeVi.
     *
     * @param alAlexTypeVi the entity to save.
     * @return the persisted entity.
     */
    public AlAlexTypeVi save(AlAlexTypeVi alAlexTypeVi) {
        LOG.debug("Request to save AlAlexTypeVi : {}", alAlexTypeVi);
        return alAlexTypeViRepository.save(alAlexTypeVi);
    }

    /**
     * Update a alAlexTypeVi.
     *
     * @param alAlexTypeVi the entity to save.
     * @return the persisted entity.
     */
    public AlAlexTypeVi update(AlAlexTypeVi alAlexTypeVi) {
        LOG.debug("Request to update AlAlexTypeVi : {}", alAlexTypeVi);
        return alAlexTypeViRepository.save(alAlexTypeVi);
    }

    /**
     * Partially update a alAlexTypeVi.
     *
     * @param alAlexTypeVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlAlexTypeVi> partialUpdate(AlAlexTypeVi alAlexTypeVi) {
        LOG.debug("Request to partially update AlAlexTypeVi : {}", alAlexTypeVi);

        return alAlexTypeViRepository
            .findById(alAlexTypeVi.getId())
            .map(existingAlAlexTypeVi -> {
                if (alAlexTypeVi.getName() != null) {
                    existingAlAlexTypeVi.setName(alAlexTypeVi.getName());
                }
                if (alAlexTypeVi.getDescription() != null) {
                    existingAlAlexTypeVi.setDescription(alAlexTypeVi.getDescription());
                }
                if (alAlexTypeVi.getCanDoRetail() != null) {
                    existingAlAlexTypeVi.setCanDoRetail(alAlexTypeVi.getCanDoRetail());
                }
                if (alAlexTypeVi.getIsOrgDivision() != null) {
                    existingAlAlexTypeVi.setIsOrgDivision(alAlexTypeVi.getIsOrgDivision());
                }
                if (alAlexTypeVi.getConfigJason() != null) {
                    existingAlAlexTypeVi.setConfigJason(alAlexTypeVi.getConfigJason());
                }
                if (alAlexTypeVi.getTreeDepth() != null) {
                    existingAlAlexTypeVi.setTreeDepth(alAlexTypeVi.getTreeDepth());
                }

                return existingAlAlexTypeVi;
            })
            .map(alAlexTypeViRepository::save);
    }

    /**
     * Get one alAlexTypeVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlAlexTypeVi> findOne(UUID id) {
        LOG.debug("Request to get AlAlexTypeVi : {}", id);
        return alAlexTypeViRepository.findById(id);
    }

    /**
     * Delete the alAlexTypeVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlAlexTypeVi : {}", id);
        alAlexTypeViRepository.deleteById(id);
    }
}
