package ai.realworld.service;

import ai.realworld.domain.AlVueVue;
import ai.realworld.repository.AlVueVueRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlVueVue}.
 */
@Service
@Transactional
public class AlVueVueService {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueService.class);

    private final AlVueVueRepository alVueVueRepository;

    public AlVueVueService(AlVueVueRepository alVueVueRepository) {
        this.alVueVueRepository = alVueVueRepository;
    }

    /**
     * Save a alVueVue.
     *
     * @param alVueVue the entity to save.
     * @return the persisted entity.
     */
    public AlVueVue save(AlVueVue alVueVue) {
        LOG.debug("Request to save AlVueVue : {}", alVueVue);
        return alVueVueRepository.save(alVueVue);
    }

    /**
     * Update a alVueVue.
     *
     * @param alVueVue the entity to save.
     * @return the persisted entity.
     */
    public AlVueVue update(AlVueVue alVueVue) {
        LOG.debug("Request to update AlVueVue : {}", alVueVue);
        return alVueVueRepository.save(alVueVue);
    }

    /**
     * Partially update a alVueVue.
     *
     * @param alVueVue the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVue> partialUpdate(AlVueVue alVueVue) {
        LOG.debug("Request to partially update AlVueVue : {}", alVueVue);

        return alVueVueRepository
            .findById(alVueVue.getId())
            .map(existingAlVueVue -> {
                if (alVueVue.getCode() != null) {
                    existingAlVueVue.setCode(alVueVue.getCode());
                }
                if (alVueVue.getName() != null) {
                    existingAlVueVue.setName(alVueVue.getName());
                }
                if (alVueVue.getContentHeitiga() != null) {
                    existingAlVueVue.setContentHeitiga(alVueVue.getContentHeitiga());
                }
                if (alVueVue.getDiscountType() != null) {
                    existingAlVueVue.setDiscountType(alVueVue.getDiscountType());
                }
                if (alVueVue.getDiscountRate() != null) {
                    existingAlVueVue.setDiscountRate(alVueVue.getDiscountRate());
                }
                if (alVueVue.getScope() != null) {
                    existingAlVueVue.setScope(alVueVue.getScope());
                }
                if (alVueVue.getIsIndividuallyUsedOnly() != null) {
                    existingAlVueVue.setIsIndividuallyUsedOnly(alVueVue.getIsIndividuallyUsedOnly());
                }
                if (alVueVue.getUsageLifeTimeLimit() != null) {
                    existingAlVueVue.setUsageLifeTimeLimit(alVueVue.getUsageLifeTimeLimit());
                }
                if (alVueVue.getUsageLimitPerUser() != null) {
                    existingAlVueVue.setUsageLimitPerUser(alVueVue.getUsageLimitPerUser());
                }
                if (alVueVue.getUsageQuantity() != null) {
                    existingAlVueVue.setUsageQuantity(alVueVue.getUsageQuantity());
                }
                if (alVueVue.getMinimumSpend() != null) {
                    existingAlVueVue.setMinimumSpend(alVueVue.getMinimumSpend());
                }
                if (alVueVue.getMaximumSpend() != null) {
                    existingAlVueVue.setMaximumSpend(alVueVue.getMaximumSpend());
                }
                if (alVueVue.getCanBeCollectedByUser() != null) {
                    existingAlVueVue.setCanBeCollectedByUser(alVueVue.getCanBeCollectedByUser());
                }
                if (alVueVue.getSalePriceFromDate() != null) {
                    existingAlVueVue.setSalePriceFromDate(alVueVue.getSalePriceFromDate());
                }
                if (alVueVue.getSalePriceToDate() != null) {
                    existingAlVueVue.setSalePriceToDate(alVueVue.getSalePriceToDate());
                }
                if (alVueVue.getPublicationStatus() != null) {
                    existingAlVueVue.setPublicationStatus(alVueVue.getPublicationStatus());
                }
                if (alVueVue.getPublishedDate() != null) {
                    existingAlVueVue.setPublishedDate(alVueVue.getPublishedDate());
                }

                return existingAlVueVue;
            })
            .map(alVueVueRepository::save);
    }

    /**
     * Get one alVueVue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVue> findOne(UUID id) {
        LOG.debug("Request to get AlVueVue : {}", id);
        return alVueVueRepository.findById(id);
    }

    /**
     * Delete the alVueVue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlVueVue : {}", id);
        alVueVueRepository.deleteById(id);
    }
}
