package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.SaisanCogVi;
import ai.realworld.repository.SaisanCogViRepository;
import ai.realworld.service.criteria.SaisanCogViCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SaisanCogVi} entities in the database.
 * The main input is a {@link SaisanCogViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SaisanCogVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SaisanCogViQueryService extends QueryService<SaisanCogVi> {

    private static final Logger LOG = LoggerFactory.getLogger(SaisanCogViQueryService.class);

    private final SaisanCogViRepository saisanCogViRepository;

    public SaisanCogViQueryService(SaisanCogViRepository saisanCogViRepository) {
        this.saisanCogViRepository = saisanCogViRepository;
    }

    /**
     * Return a {@link Page} of {@link SaisanCogVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SaisanCogVi> findByCriteria(SaisanCogViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SaisanCogVi> specification = createSpecification(criteria);
        return saisanCogViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SaisanCogViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SaisanCogVi> specification = createSpecification(criteria);
        return saisanCogViRepository.count(specification);
    }

    /**
     * Function to convert {@link SaisanCogViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SaisanCogVi> createSpecification(SaisanCogViCriteria criteria) {
        Specification<SaisanCogVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SaisanCogVi_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), SaisanCogVi_.key));
            }
            if (criteria.getValueJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValueJason(), SaisanCogVi_.valueJason));
            }
        }
        return specification;
    }
}
