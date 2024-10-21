package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.SicilyUmeto;
import ai.realworld.repository.SicilyUmetoRepository;
import ai.realworld.service.criteria.SicilyUmetoCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SicilyUmeto} entities in the database.
 * The main input is a {@link SicilyUmetoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SicilyUmeto} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SicilyUmetoQueryService extends QueryService<SicilyUmeto> {

    private static final Logger LOG = LoggerFactory.getLogger(SicilyUmetoQueryService.class);

    private final SicilyUmetoRepository sicilyUmetoRepository;

    public SicilyUmetoQueryService(SicilyUmetoRepository sicilyUmetoRepository) {
        this.sicilyUmetoRepository = sicilyUmetoRepository;
    }

    /**
     * Return a {@link Page} of {@link SicilyUmeto} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SicilyUmeto> findByCriteria(SicilyUmetoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SicilyUmeto> specification = createSpecification(criteria);
        return sicilyUmetoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SicilyUmetoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SicilyUmeto> specification = createSpecification(criteria);
        return sicilyUmetoRepository.count(specification);
    }

    /**
     * Function to convert {@link SicilyUmetoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SicilyUmeto> createSpecification(SicilyUmetoCriteria criteria) {
        Specification<SicilyUmeto> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SicilyUmeto_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), SicilyUmeto_.type));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), SicilyUmeto_.content));
            }
        }
        return specification;
    }
}
