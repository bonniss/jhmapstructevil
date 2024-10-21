package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlVueVueViUsage;
import ai.realworld.repository.AlVueVueViUsageRepository;
import ai.realworld.service.criteria.AlVueVueViUsageCriteria;
import ai.realworld.service.dto.AlVueVueViUsageDTO;
import ai.realworld.service.mapper.AlVueVueViUsageMapper;
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
 * Service for executing complex queries for {@link AlVueVueViUsage} entities in the database.
 * The main input is a {@link AlVueVueViUsageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlVueVueViUsageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlVueVueViUsageQueryService extends QueryService<AlVueVueViUsage> {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueViUsageQueryService.class);

    private final AlVueVueViUsageRepository alVueVueViUsageRepository;

    private final AlVueVueViUsageMapper alVueVueViUsageMapper;

    public AlVueVueViUsageQueryService(AlVueVueViUsageRepository alVueVueViUsageRepository, AlVueVueViUsageMapper alVueVueViUsageMapper) {
        this.alVueVueViUsageRepository = alVueVueViUsageRepository;
        this.alVueVueViUsageMapper = alVueVueViUsageMapper;
    }

    /**
     * Return a {@link Page} of {@link AlVueVueViUsageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlVueVueViUsageDTO> findByCriteria(AlVueVueViUsageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlVueVueViUsage> specification = createSpecification(criteria);
        return alVueVueViUsageRepository.findAll(specification, page).map(alVueVueViUsageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlVueVueViUsageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlVueVueViUsage> specification = createSpecification(criteria);
        return alVueVueViUsageRepository.count(specification);
    }

    /**
     * Function to convert {@link AlVueVueViUsageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlVueVueViUsage> createSpecification(AlVueVueViUsageCriteria criteria) {
        Specification<AlVueVueViUsage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlVueVueViUsage_.id));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlVueVueViUsage_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
            if (criteria.getVoucherId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getVoucherId(), root ->
                        root.join(AlVueVueViUsage_.vouchers, JoinType.LEFT).get(AlVueVueVi_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root ->
                        root.join(AlVueVueViUsage_.customers, JoinType.LEFT).get(AlPacino_.id)
                    )
                );
            }
        }
        return specification;
    }
}
