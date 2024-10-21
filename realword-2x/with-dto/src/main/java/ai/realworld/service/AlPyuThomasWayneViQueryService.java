package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPyuThomasWayneVi;
import ai.realworld.repository.AlPyuThomasWayneViRepository;
import ai.realworld.service.criteria.AlPyuThomasWayneViCriteria;
import ai.realworld.service.dto.AlPyuThomasWayneViDTO;
import ai.realworld.service.mapper.AlPyuThomasWayneViMapper;
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
 * Service for executing complex queries for {@link AlPyuThomasWayneVi} entities in the database.
 * The main input is a {@link AlPyuThomasWayneViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPyuThomasWayneViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPyuThomasWayneViQueryService extends QueryService<AlPyuThomasWayneVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuThomasWayneViQueryService.class);

    private final AlPyuThomasWayneViRepository alPyuThomasWayneViRepository;

    private final AlPyuThomasWayneViMapper alPyuThomasWayneViMapper;

    public AlPyuThomasWayneViQueryService(
        AlPyuThomasWayneViRepository alPyuThomasWayneViRepository,
        AlPyuThomasWayneViMapper alPyuThomasWayneViMapper
    ) {
        this.alPyuThomasWayneViRepository = alPyuThomasWayneViRepository;
        this.alPyuThomasWayneViMapper = alPyuThomasWayneViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlPyuThomasWayneViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPyuThomasWayneViDTO> findByCriteria(AlPyuThomasWayneViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPyuThomasWayneVi> specification = createSpecification(criteria);
        return alPyuThomasWayneViRepository.findAll(specification, page).map(alPyuThomasWayneViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPyuThomasWayneViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPyuThomasWayneVi> specification = createSpecification(criteria);
        return alPyuThomasWayneViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPyuThomasWayneViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPyuThomasWayneVi> createSpecification(AlPyuThomasWayneViCriteria criteria) {
        Specification<AlPyuThomasWayneVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPyuThomasWayneVi_.id));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), AlPyuThomasWayneVi_.rating));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), AlPyuThomasWayneVi_.comment));
            }
            if (criteria.getBookingId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBookingId(), root ->
                        root.join(AlPyuThomasWayneVi_.booking, JoinType.LEFT).get(AlPyuJokerVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
