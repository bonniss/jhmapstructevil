package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlBestToothVi;
import ai.realworld.repository.AlBestToothViRepository;
import ai.realworld.service.criteria.AlBestToothViCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AlBestToothVi} entities in the database.
 * The main input is a {@link AlBestToothViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlBestToothVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlBestToothViQueryService extends QueryService<AlBestToothVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlBestToothViQueryService.class);

    private final AlBestToothViRepository alBestToothViRepository;

    public AlBestToothViQueryService(AlBestToothViRepository alBestToothViRepository) {
        this.alBestToothViRepository = alBestToothViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlBestToothVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlBestToothVi> findByCriteria(AlBestToothViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlBestToothVi> specification = createSpecification(criteria);
        return alBestToothViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlBestToothViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlBestToothVi> specification = createSpecification(criteria);
        return alBestToothViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlBestToothViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlBestToothVi> createSpecification(AlBestToothViCriteria criteria) {
        Specification<AlBestToothVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlBestToothVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlBestToothVi_.name));
            }
        }
        return specification;
    }
}
