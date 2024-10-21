package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPyuThomasWayne;
import ai.realworld.repository.AlPyuThomasWayneRepository;
import ai.realworld.service.criteria.AlPyuThomasWayneCriteria;
import ai.realworld.service.dto.AlPyuThomasWayneDTO;
import ai.realworld.service.mapper.AlPyuThomasWayneMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AlPyuThomasWayne} entities in the database.
 * The main input is a {@link AlPyuThomasWayneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPyuThomasWayneDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPyuThomasWayneQueryService extends QueryService<AlPyuThomasWayne> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuThomasWayneQueryService.class);

    private final AlPyuThomasWayneRepository alPyuThomasWayneRepository;

    private final AlPyuThomasWayneMapper alPyuThomasWayneMapper;

    public AlPyuThomasWayneQueryService(
        AlPyuThomasWayneRepository alPyuThomasWayneRepository,
        AlPyuThomasWayneMapper alPyuThomasWayneMapper
    ) {
        this.alPyuThomasWayneRepository = alPyuThomasWayneRepository;
        this.alPyuThomasWayneMapper = alPyuThomasWayneMapper;
    }

    /**
     * Return a {@link Page} of {@link AlPyuThomasWayneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPyuThomasWayneDTO> findByCriteria(AlPyuThomasWayneCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPyuThomasWayne> specification = createSpecification(criteria);
        return alPyuThomasWayneRepository.findAll(specification, page).map(alPyuThomasWayneMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPyuThomasWayneCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPyuThomasWayne> specification = createSpecification(criteria);
        return alPyuThomasWayneRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPyuThomasWayneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPyuThomasWayne> createSpecification(AlPyuThomasWayneCriteria criteria) {
        Specification<AlPyuThomasWayne> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPyuThomasWayne_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), AlPyuThomasWayne_.rating));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), AlPyuThomasWayne_.comment));
            }
            if (criteria.getBookingId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBookingId(), root ->
                        root.join(AlPyuThomasWayne_.booking, JoinType.LEFT).get(AlPyuJoker_.id)
                    )
                );
            }
        }
        return specification;
    }
}
