package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPacinoAndreiRightHand;
import ai.realworld.repository.AlPacinoAndreiRightHandRepository;
import ai.realworld.service.criteria.AlPacinoAndreiRightHandCriteria;
import ai.realworld.service.dto.AlPacinoAndreiRightHandDTO;
import ai.realworld.service.mapper.AlPacinoAndreiRightHandMapper;
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
 * Service for executing complex queries for {@link AlPacinoAndreiRightHand} entities in the database.
 * The main input is a {@link AlPacinoAndreiRightHandCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPacinoAndreiRightHandDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPacinoAndreiRightHandQueryService extends QueryService<AlPacinoAndreiRightHand> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoAndreiRightHandQueryService.class);

    private final AlPacinoAndreiRightHandRepository alPacinoAndreiRightHandRepository;

    private final AlPacinoAndreiRightHandMapper alPacinoAndreiRightHandMapper;

    public AlPacinoAndreiRightHandQueryService(
        AlPacinoAndreiRightHandRepository alPacinoAndreiRightHandRepository,
        AlPacinoAndreiRightHandMapper alPacinoAndreiRightHandMapper
    ) {
        this.alPacinoAndreiRightHandRepository = alPacinoAndreiRightHandRepository;
        this.alPacinoAndreiRightHandMapper = alPacinoAndreiRightHandMapper;
    }

    /**
     * Return a {@link Page} of {@link AlPacinoAndreiRightHandDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPacinoAndreiRightHandDTO> findByCriteria(AlPacinoAndreiRightHandCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPacinoAndreiRightHand> specification = createSpecification(criteria);
        return alPacinoAndreiRightHandRepository.findAll(specification, page).map(alPacinoAndreiRightHandMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPacinoAndreiRightHandCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPacinoAndreiRightHand> specification = createSpecification(criteria);
        return alPacinoAndreiRightHandRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPacinoAndreiRightHandCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPacinoAndreiRightHand> createSpecification(AlPacinoAndreiRightHandCriteria criteria) {
        Specification<AlPacinoAndreiRightHand> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlPacinoAndreiRightHand_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlPacinoAndreiRightHand_.name));
            }
            if (criteria.getIsDefault() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDefault(), AlPacinoAndreiRightHand_.isDefault));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root ->
                        root.join(AlPacinoAndreiRightHand_.user, JoinType.LEFT).get(AlPacino_.id)
                    )
                );
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAddressId(), root ->
                        root.join(AlPacinoAndreiRightHand_.address, JoinType.LEFT).get(AndreiRightHand_.id)
                    )
                );
            }
        }
        return specification;
    }
}
