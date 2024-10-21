package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPacinoAndreiRightHandVi;
import ai.realworld.repository.AlPacinoAndreiRightHandViRepository;
import ai.realworld.service.criteria.AlPacinoAndreiRightHandViCriteria;
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
 * Service for executing complex queries for {@link AlPacinoAndreiRightHandVi} entities in the database.
 * The main input is a {@link AlPacinoAndreiRightHandViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPacinoAndreiRightHandVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPacinoAndreiRightHandViQueryService extends QueryService<AlPacinoAndreiRightHandVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoAndreiRightHandViQueryService.class);

    private final AlPacinoAndreiRightHandViRepository alPacinoAndreiRightHandViRepository;

    public AlPacinoAndreiRightHandViQueryService(AlPacinoAndreiRightHandViRepository alPacinoAndreiRightHandViRepository) {
        this.alPacinoAndreiRightHandViRepository = alPacinoAndreiRightHandViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlPacinoAndreiRightHandVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPacinoAndreiRightHandVi> findByCriteria(AlPacinoAndreiRightHandViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPacinoAndreiRightHandVi> specification = createSpecification(criteria);
        return alPacinoAndreiRightHandViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPacinoAndreiRightHandViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPacinoAndreiRightHandVi> specification = createSpecification(criteria);
        return alPacinoAndreiRightHandViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPacinoAndreiRightHandViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPacinoAndreiRightHandVi> createSpecification(AlPacinoAndreiRightHandViCriteria criteria) {
        Specification<AlPacinoAndreiRightHandVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPacinoAndreiRightHandVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlPacinoAndreiRightHandVi_.name));
            }
            if (criteria.getIsDefault() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDefault(), AlPacinoAndreiRightHandVi_.isDefault));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root ->
                        root.join(AlPacinoAndreiRightHandVi_.user, JoinType.LEFT).get(AlPacino_.id)
                    )
                );
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAddressId(), root ->
                        root.join(AlPacinoAndreiRightHandVi_.address, JoinType.LEFT).get(AndreiRightHandVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
