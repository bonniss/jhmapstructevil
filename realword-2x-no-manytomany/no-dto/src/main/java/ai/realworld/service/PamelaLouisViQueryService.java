package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.PamelaLouisVi;
import ai.realworld.repository.PamelaLouisViRepository;
import ai.realworld.service.criteria.PamelaLouisViCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PamelaLouisVi} entities in the database.
 * The main input is a {@link PamelaLouisViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PamelaLouisVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PamelaLouisViQueryService extends QueryService<PamelaLouisVi> {

    private static final Logger LOG = LoggerFactory.getLogger(PamelaLouisViQueryService.class);

    private final PamelaLouisViRepository pamelaLouisViRepository;

    public PamelaLouisViQueryService(PamelaLouisViRepository pamelaLouisViRepository) {
        this.pamelaLouisViRepository = pamelaLouisViRepository;
    }

    /**
     * Return a {@link Page} of {@link PamelaLouisVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PamelaLouisVi> findByCriteria(PamelaLouisViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PamelaLouisVi> specification = createSpecification(criteria);
        return pamelaLouisViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PamelaLouisViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PamelaLouisVi> specification = createSpecification(criteria);
        return pamelaLouisViRepository.count(specification);
    }

    /**
     * Function to convert {@link PamelaLouisViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PamelaLouisVi> createSpecification(PamelaLouisViCriteria criteria) {
        Specification<PamelaLouisVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PamelaLouisVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PamelaLouisVi_.name));
            }
            if (criteria.getConfigJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigJason(), PamelaLouisVi_.configJason));
            }
        }
        return specification;
    }
}
