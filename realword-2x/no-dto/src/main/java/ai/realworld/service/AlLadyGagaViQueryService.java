package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlLadyGagaVi;
import ai.realworld.repository.AlLadyGagaViRepository;
import ai.realworld.service.criteria.AlLadyGagaViCriteria;
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
 * Service for executing complex queries for {@link AlLadyGagaVi} entities in the database.
 * The main input is a {@link AlLadyGagaViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlLadyGagaVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlLadyGagaViQueryService extends QueryService<AlLadyGagaVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlLadyGagaViQueryService.class);

    private final AlLadyGagaViRepository alLadyGagaViRepository;

    public AlLadyGagaViQueryService(AlLadyGagaViRepository alLadyGagaViRepository) {
        this.alLadyGagaViRepository = alLadyGagaViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlLadyGagaVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlLadyGagaVi> findByCriteria(AlLadyGagaViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlLadyGagaVi> specification = createSpecification(criteria);
        return alLadyGagaViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlLadyGagaViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlLadyGagaVi> specification = createSpecification(criteria);
        return alLadyGagaViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlLadyGagaViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlLadyGagaVi> createSpecification(AlLadyGagaViCriteria criteria) {
        Specification<AlLadyGagaVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlLadyGagaVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlLadyGagaVi_.name));
            }
            if (criteria.getDescriptionHeitiga() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getDescriptionHeitiga(), AlLadyGagaVi_.descriptionHeitiga)
                );
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAddressId(), root ->
                        root.join(AlLadyGagaVi_.address, JoinType.LEFT).get(AndreiRightHandVi_.id)
                    )
                );
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAvatarId(), root -> root.join(AlLadyGagaVi_.avatar, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlLadyGagaVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
