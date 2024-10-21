package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.Initium;
import ai.realworld.repository.InitiumRepository;
import ai.realworld.service.criteria.InitiumCriteria;
import ai.realworld.service.dto.InitiumDTO;
import ai.realworld.service.mapper.InitiumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Initium} entities in the database.
 * The main input is a {@link InitiumCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InitiumDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InitiumQueryService extends QueryService<Initium> {

    private static final Logger LOG = LoggerFactory.getLogger(InitiumQueryService.class);

    private final InitiumRepository initiumRepository;

    private final InitiumMapper initiumMapper;

    public InitiumQueryService(InitiumRepository initiumRepository, InitiumMapper initiumMapper) {
        this.initiumRepository = initiumRepository;
        this.initiumMapper = initiumMapper;
    }

    /**
     * Return a {@link Page} of {@link InitiumDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InitiumDTO> findByCriteria(InitiumCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Initium> specification = createSpecification(criteria);
        return initiumRepository.findAll(specification, page).map(initiumMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InitiumCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Initium> specification = createSpecification(criteria);
        return initiumRepository.count(specification);
    }

    /**
     * Function to convert {@link InitiumCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Initium> createSpecification(InitiumCriteria criteria) {
        Specification<Initium> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Initium_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Initium_.name));
            }
            if (criteria.getSlug() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSlug(), Initium_.slug));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Initium_.description));
            }
            if (criteria.getIsJelloSupported() != null) {
                specification = specification.and(buildSpecification(criteria.getIsJelloSupported(), Initium_.isJelloSupported));
            }
        }
        return specification;
    }
}
