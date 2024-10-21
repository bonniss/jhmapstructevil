package ai.realworld.service;

import ai.realworld.domain.AlVueVueVi;
import ai.realworld.repository.AlVueVueViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlVueVueVi}.
 */
@Service
@Transactional
public class AlVueVueViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueViService.class);

    private final AlVueVueViRepository alVueVueViRepository;

    public AlVueVueViService(AlVueVueViRepository alVueVueViRepository) {
        this.alVueVueViRepository = alVueVueViRepository;
    }

    /**
     * Save a alVueVueVi.
     *
     * @param alVueVueVi the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueVi save(AlVueVueVi alVueVueVi) {
        LOG.debug("Request to save AlVueVueVi : {}", alVueVueVi);
        return alVueVueViRepository.save(alVueVueVi);
    }

    /**
     * Update a alVueVueVi.
     *
     * @param alVueVueVi the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueVi update(AlVueVueVi alVueVueVi) {
        LOG.debug("Request to update AlVueVueVi : {}", alVueVueVi);
        return alVueVueViRepository.save(alVueVueVi);
    }

    /**
     * Partially update a alVueVueVi.
     *
     * @param alVueVueVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVueVi> partialUpdate(AlVueVueVi alVueVueVi) {
        LOG.debug("Request to partially update AlVueVueVi : {}", alVueVueVi);

        return alVueVueViRepository
            .findById(alVueVueVi.getId())
            .map(existingAlVueVueVi -> {
                if (alVueVueVi.getCode() != null) {
                    existingAlVueVueVi.setCode(alVueVueVi.getCode());
                }
                if (alVueVueVi.getName() != null) {
                    existingAlVueVueVi.setName(alVueVueVi.getName());
                }
                if (alVueVueVi.getContentHeitiga() != null) {
                    existingAlVueVueVi.setContentHeitiga(alVueVueVi.getContentHeitiga());
                }
                if (alVueVueVi.getDiscountType() != null) {
                    existingAlVueVueVi.setDiscountType(alVueVueVi.getDiscountType());
                }
                if (alVueVueVi.getDiscountRate() != null) {
                    existingAlVueVueVi.setDiscountRate(alVueVueVi.getDiscountRate());
                }
                if (alVueVueVi.getScope() != null) {
                    existingAlVueVueVi.setScope(alVueVueVi.getScope());
                }
                if (alVueVueVi.getIsIndividuallyUsedOnly() != null) {
                    existingAlVueVueVi.setIsIndividuallyUsedOnly(alVueVueVi.getIsIndividuallyUsedOnly());
                }
                if (alVueVueVi.getUsageLifeTimeLimit() != null) {
                    existingAlVueVueVi.setUsageLifeTimeLimit(alVueVueVi.getUsageLifeTimeLimit());
                }
                if (alVueVueVi.getUsageLimitPerUser() != null) {
                    existingAlVueVueVi.setUsageLimitPerUser(alVueVueVi.getUsageLimitPerUser());
                }
                if (alVueVueVi.getUsageQuantity() != null) {
                    existingAlVueVueVi.setUsageQuantity(alVueVueVi.getUsageQuantity());
                }
                if (alVueVueVi.getMinimumSpend() != null) {
                    existingAlVueVueVi.setMinimumSpend(alVueVueVi.getMinimumSpend());
                }
                if (alVueVueVi.getMaximumSpend() != null) {
                    existingAlVueVueVi.setMaximumSpend(alVueVueVi.getMaximumSpend());
                }
                if (alVueVueVi.getCanBeCollectedByUser() != null) {
                    existingAlVueVueVi.setCanBeCollectedByUser(alVueVueVi.getCanBeCollectedByUser());
                }
                if (alVueVueVi.getSalePriceFromDate() != null) {
                    existingAlVueVueVi.setSalePriceFromDate(alVueVueVi.getSalePriceFromDate());
                }
                if (alVueVueVi.getSalePriceToDate() != null) {
                    existingAlVueVueVi.setSalePriceToDate(alVueVueVi.getSalePriceToDate());
                }
                if (alVueVueVi.getPublicationStatus() != null) {
                    existingAlVueVueVi.setPublicationStatus(alVueVueVi.getPublicationStatus());
                }
                if (alVueVueVi.getPublishedDate() != null) {
                    existingAlVueVueVi.setPublishedDate(alVueVueVi.getPublishedDate());
                }

                return existingAlVueVueVi;
            })
            .map(alVueVueViRepository::save);
    }

    /**
     * Get one alVueVueVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVueVi> findOne(UUID id) {
        LOG.debug("Request to get AlVueVueVi : {}", id);
        return alVueVueViRepository.findById(id);
    }

    /**
     * Delete the alVueVueVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlVueVueVi : {}", id);
        alVueVueViRepository.deleteById(id);
    }
}
