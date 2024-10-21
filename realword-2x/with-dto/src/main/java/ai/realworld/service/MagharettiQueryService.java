package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.Magharetti;
import ai.realworld.repository.MagharettiRepository;
import ai.realworld.service.criteria.MagharettiCriteria;
import ai.realworld.service.dto.MagharettiDTO;
import ai.realworld.service.mapper.MagharettiMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Magharetti} entities in the database.
 * The main input is a {@link MagharettiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MagharettiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MagharettiQueryService extends QueryService<Magharetti> {

    private static final Logger LOG = LoggerFactory.getLogger(MagharettiQueryService.class);

    private final MagharettiRepository magharettiRepository;

    private final MagharettiMapper magharettiMapper;

    public MagharettiQueryService(MagharettiRepository magharettiRepository, MagharettiMapper magharettiMapper) {
        this.magharettiRepository = magharettiRepository;
        this.magharettiMapper = magharettiMapper;
    }

    /**
     * Return a {@link Page} of {@link MagharettiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MagharettiDTO> findByCriteria(MagharettiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Magharetti> specification = createSpecification(criteria);
        return magharettiRepository.findAll(specification, page).map(magharettiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MagharettiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Magharetti> specification = createSpecification(criteria);
        return magharettiRepository.count(specification);
    }

    /**
     * Function to convert {@link MagharettiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Magharetti> createSpecification(MagharettiCriteria criteria) {
        Specification<Magharetti> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Magharetti_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Magharetti_.name));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Magharetti_.label));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Magharetti_.type));
            }
        }
        return specification;
    }
}
