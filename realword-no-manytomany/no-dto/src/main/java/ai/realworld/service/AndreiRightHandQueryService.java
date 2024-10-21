package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.repository.AndreiRightHandRepository;
import ai.realworld.service.criteria.AndreiRightHandCriteria;
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
 * Service for executing complex queries for {@link AndreiRightHand} entities in the database.
 * The main input is a {@link AndreiRightHandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AndreiRightHand} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AndreiRightHandQueryService extends QueryService<AndreiRightHand> {

    private static final Logger LOG = LoggerFactory.getLogger(AndreiRightHandQueryService.class);

    private final AndreiRightHandRepository andreiRightHandRepository;

    public AndreiRightHandQueryService(AndreiRightHandRepository andreiRightHandRepository) {
        this.andreiRightHandRepository = andreiRightHandRepository;
    }

    /**
     * Return a {@link Page} of {@link AndreiRightHand} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AndreiRightHand> findByCriteria(AndreiRightHandCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AndreiRightHand> specification = createSpecification(criteria);
        return andreiRightHandRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AndreiRightHandCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AndreiRightHand> specification = createSpecification(criteria);
        return andreiRightHandRepository.count(specification);
    }

    /**
     * Function to convert {@link AndreiRightHandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AndreiRightHand> createSpecification(AndreiRightHandCriteria criteria) {
        Specification<AndreiRightHand> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AndreiRightHand_.id));
            }
            if (criteria.getDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetails(), AndreiRightHand_.details));
            }
            if (criteria.getLat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLat(), AndreiRightHand_.lat));
            }
            if (criteria.getLon() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLon(), AndreiRightHand_.lon));
            }
            if (criteria.getCountryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCountryId(), root ->
                        root.join(AndreiRightHand_.country, JoinType.LEFT).get(AntonioBanderas_.id)
                    )
                );
            }
            if (criteria.getProvinceId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProvinceId(), root ->
                        root.join(AndreiRightHand_.province, JoinType.LEFT).get(AntonioBanderas_.id)
                    )
                );
            }
            if (criteria.getDistrictId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDistrictId(), root ->
                        root.join(AndreiRightHand_.district, JoinType.LEFT).get(AntonioBanderas_.id)
                    )
                );
            }
            if (criteria.getWardId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getWardId(), root ->
                        root.join(AndreiRightHand_.ward, JoinType.LEFT).get(AntonioBanderas_.id)
                    )
                );
            }
        }
        return specification;
    }
}
