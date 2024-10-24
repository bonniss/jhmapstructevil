package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.SaisanCog;
import ai.realworld.repository.SaisanCogRepository;
import ai.realworld.service.criteria.SaisanCogCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SaisanCog} entities in the database.
 * The main input is a {@link SaisanCogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SaisanCog} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SaisanCogQueryService extends QueryService<SaisanCog> {

    private static final Logger LOG = LoggerFactory.getLogger(SaisanCogQueryService.class);

    private final SaisanCogRepository saisanCogRepository;

    public SaisanCogQueryService(SaisanCogRepository saisanCogRepository) {
        this.saisanCogRepository = saisanCogRepository;
    }

    /**
     * Return a {@link Page} of {@link SaisanCog} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SaisanCog> findByCriteria(SaisanCogCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SaisanCog> specification = createSpecification(criteria);
        return saisanCogRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SaisanCogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SaisanCog> specification = createSpecification(criteria);
        return saisanCogRepository.count(specification);
    }

    /**
     * Function to convert {@link SaisanCogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SaisanCog> createSpecification(SaisanCogCriteria criteria) {
        Specification<SaisanCog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SaisanCog_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), SaisanCog_.key));
            }
            if (criteria.getValueJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValueJason(), SaisanCog_.valueJason));
            }
        }
        return specification;
    }
}
