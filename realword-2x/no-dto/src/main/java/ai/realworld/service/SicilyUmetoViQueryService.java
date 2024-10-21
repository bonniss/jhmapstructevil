package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.SicilyUmetoVi;
import ai.realworld.repository.SicilyUmetoViRepository;
import ai.realworld.service.criteria.SicilyUmetoViCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SicilyUmetoVi} entities in the database.
 * The main input is a {@link SicilyUmetoViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SicilyUmetoVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SicilyUmetoViQueryService extends QueryService<SicilyUmetoVi> {

    private static final Logger LOG = LoggerFactory.getLogger(SicilyUmetoViQueryService.class);

    private final SicilyUmetoViRepository sicilyUmetoViRepository;

    public SicilyUmetoViQueryService(SicilyUmetoViRepository sicilyUmetoViRepository) {
        this.sicilyUmetoViRepository = sicilyUmetoViRepository;
    }

    /**
     * Return a {@link Page} of {@link SicilyUmetoVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SicilyUmetoVi> findByCriteria(SicilyUmetoViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SicilyUmetoVi> specification = createSpecification(criteria);
        return sicilyUmetoViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SicilyUmetoViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SicilyUmetoVi> specification = createSpecification(criteria);
        return sicilyUmetoViRepository.count(specification);
    }

    /**
     * Function to convert {@link SicilyUmetoViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SicilyUmetoVi> createSpecification(SicilyUmetoViCriteria criteria) {
        Specification<SicilyUmetoVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SicilyUmetoVi_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), SicilyUmetoVi_.type));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), SicilyUmetoVi_.content));
            }
        }
        return specification;
    }
}
