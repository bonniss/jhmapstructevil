package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.repository.AndreiRightHandViRepository;
import ai.realworld.service.criteria.AndreiRightHandViCriteria;
import ai.realworld.service.dto.AndreiRightHandViDTO;
import ai.realworld.service.mapper.AndreiRightHandViMapper;
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
 * Service for executing complex queries for {@link AndreiRightHandVi} entities in the database.
 * The main input is a {@link AndreiRightHandViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AndreiRightHandViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AndreiRightHandViQueryService extends QueryService<AndreiRightHandVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AndreiRightHandViQueryService.class);

    private final AndreiRightHandViRepository andreiRightHandViRepository;

    private final AndreiRightHandViMapper andreiRightHandViMapper;

    public AndreiRightHandViQueryService(
        AndreiRightHandViRepository andreiRightHandViRepository,
        AndreiRightHandViMapper andreiRightHandViMapper
    ) {
        this.andreiRightHandViRepository = andreiRightHandViRepository;
        this.andreiRightHandViMapper = andreiRightHandViMapper;
    }

    /**
     * Return a {@link Page} of {@link AndreiRightHandViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AndreiRightHandViDTO> findByCriteria(AndreiRightHandViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AndreiRightHandVi> specification = createSpecification(criteria);
        return andreiRightHandViRepository.findAll(specification, page).map(andreiRightHandViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AndreiRightHandViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AndreiRightHandVi> specification = createSpecification(criteria);
        return andreiRightHandViRepository.count(specification);
    }

    /**
     * Function to convert {@link AndreiRightHandViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AndreiRightHandVi> createSpecification(AndreiRightHandViCriteria criteria) {
        Specification<AndreiRightHandVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AndreiRightHandVi_.id));
            }
            if (criteria.getDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetails(), AndreiRightHandVi_.details));
            }
            if (criteria.getLat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLat(), AndreiRightHandVi_.lat));
            }
            if (criteria.getLon() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLon(), AndreiRightHandVi_.lon));
            }
            if (criteria.getCountryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCountryId(), root ->
                        root.join(AndreiRightHandVi_.country, JoinType.LEFT).get(AntonioBanderasVi_.id)
                    )
                );
            }
            if (criteria.getProvinceId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProvinceId(), root ->
                        root.join(AndreiRightHandVi_.province, JoinType.LEFT).get(AntonioBanderasVi_.id)
                    )
                );
            }
            if (criteria.getDistrictId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDistrictId(), root ->
                        root.join(AndreiRightHandVi_.district, JoinType.LEFT).get(AntonioBanderasVi_.id)
                    )
                );
            }
            if (criteria.getWardId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getWardId(), root ->
                        root.join(AndreiRightHandVi_.ward, JoinType.LEFT).get(AntonioBanderasVi_.id)
                    )
                );
            }
        }
        return specification;
    }
}
