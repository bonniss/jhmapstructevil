package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.MagharettiVi;
import ai.realworld.repository.MagharettiViRepository;
import ai.realworld.service.criteria.MagharettiViCriteria;
import ai.realworld.service.dto.MagharettiViDTO;
import ai.realworld.service.mapper.MagharettiViMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MagharettiVi} entities in the database.
 * The main input is a {@link MagharettiViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MagharettiViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MagharettiViQueryService extends QueryService<MagharettiVi> {

    private static final Logger LOG = LoggerFactory.getLogger(MagharettiViQueryService.class);

    private final MagharettiViRepository magharettiViRepository;

    private final MagharettiViMapper magharettiViMapper;

    public MagharettiViQueryService(MagharettiViRepository magharettiViRepository, MagharettiViMapper magharettiViMapper) {
        this.magharettiViRepository = magharettiViRepository;
        this.magharettiViMapper = magharettiViMapper;
    }

    /**
     * Return a {@link Page} of {@link MagharettiViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MagharettiViDTO> findByCriteria(MagharettiViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MagharettiVi> specification = createSpecification(criteria);
        return magharettiViRepository.findAll(specification, page).map(magharettiViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MagharettiViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MagharettiVi> specification = createSpecification(criteria);
        return magharettiViRepository.count(specification);
    }

    /**
     * Function to convert {@link MagharettiViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MagharettiVi> createSpecification(MagharettiViCriteria criteria) {
        Specification<MagharettiVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MagharettiVi_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MagharettiVi_.name));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), MagharettiVi_.label));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), MagharettiVi_.type));
            }
        }
        return specification;
    }
}
