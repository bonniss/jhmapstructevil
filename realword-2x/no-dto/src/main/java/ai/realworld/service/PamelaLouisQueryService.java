package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.PamelaLouis;
import ai.realworld.repository.PamelaLouisRepository;
import ai.realworld.service.criteria.PamelaLouisCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PamelaLouis} entities in the database.
 * The main input is a {@link PamelaLouisCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PamelaLouis} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PamelaLouisQueryService extends QueryService<PamelaLouis> {

    private static final Logger LOG = LoggerFactory.getLogger(PamelaLouisQueryService.class);

    private final PamelaLouisRepository pamelaLouisRepository;

    public PamelaLouisQueryService(PamelaLouisRepository pamelaLouisRepository) {
        this.pamelaLouisRepository = pamelaLouisRepository;
    }

    /**
     * Return a {@link Page} of {@link PamelaLouis} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PamelaLouis> findByCriteria(PamelaLouisCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PamelaLouis> specification = createSpecification(criteria);
        return pamelaLouisRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PamelaLouisCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PamelaLouis> specification = createSpecification(criteria);
        return pamelaLouisRepository.count(specification);
    }

    /**
     * Function to convert {@link PamelaLouisCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PamelaLouis> createSpecification(PamelaLouisCriteria criteria) {
        Specification<PamelaLouis> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PamelaLouis_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PamelaLouis_.name));
            }
            if (criteria.getConfigJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConfigJason(), PamelaLouis_.configJason));
            }
        }
        return specification;
    }
}
