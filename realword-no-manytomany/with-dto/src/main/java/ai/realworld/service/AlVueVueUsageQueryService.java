package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.repository.AlVueVueUsageRepository;
import ai.realworld.service.criteria.AlVueVueUsageCriteria;
import ai.realworld.service.dto.AlVueVueUsageDTO;
import ai.realworld.service.mapper.AlVueVueUsageMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AlVueVueUsage} entities in the database.
 * The main input is a {@link AlVueVueUsageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlVueVueUsageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlVueVueUsageQueryService extends QueryService<AlVueVueUsage> {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueUsageQueryService.class);

    private final AlVueVueUsageRepository alVueVueUsageRepository;

    private final AlVueVueUsageMapper alVueVueUsageMapper;

    public AlVueVueUsageQueryService(AlVueVueUsageRepository alVueVueUsageRepository, AlVueVueUsageMapper alVueVueUsageMapper) {
        this.alVueVueUsageRepository = alVueVueUsageRepository;
        this.alVueVueUsageMapper = alVueVueUsageMapper;
    }

    /**
     * Return a {@link Page} of {@link AlVueVueUsageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlVueVueUsageDTO> findByCriteria(AlVueVueUsageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlVueVueUsage> specification = createSpecification(criteria);
        return alVueVueUsageRepository.findAll(specification, page).map(alVueVueUsageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlVueVueUsageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlVueVueUsage> specification = createSpecification(criteria);
        return alVueVueUsageRepository.count(specification);
    }

    /**
     * Function to convert {@link AlVueVueUsageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlVueVueUsage> createSpecification(AlVueVueUsageCriteria criteria) {
        Specification<AlVueVueUsage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlVueVueUsage_.id));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlVueVueUsage_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getVoucherId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getVoucherId(), root -> root.join(AlVueVueUsage_.vouchers, JoinType.LEFT).get(AlVueVue_.id))
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(AlVueVueUsage_.customers, JoinType.LEFT).get(AlPacino_.id)
                    )
                );
            }
        }
        return specification;
    }
}
