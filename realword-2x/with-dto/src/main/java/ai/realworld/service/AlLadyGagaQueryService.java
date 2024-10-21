package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlLadyGaga;
import ai.realworld.repository.AlLadyGagaRepository;
import ai.realworld.service.criteria.AlLadyGagaCriteria;
import ai.realworld.service.dto.AlLadyGagaDTO;
import ai.realworld.service.mapper.AlLadyGagaMapper;
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
 * Service for executing complex queries for {@link AlLadyGaga} entities in the database.
 * The main input is a {@link AlLadyGagaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlLadyGagaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlLadyGagaQueryService extends QueryService<AlLadyGaga> {

    private static final Logger LOG = LoggerFactory.getLogger(AlLadyGagaQueryService.class);

    private final AlLadyGagaRepository alLadyGagaRepository;

    private final AlLadyGagaMapper alLadyGagaMapper;

    public AlLadyGagaQueryService(AlLadyGagaRepository alLadyGagaRepository, AlLadyGagaMapper alLadyGagaMapper) {
        this.alLadyGagaRepository = alLadyGagaRepository;
        this.alLadyGagaMapper = alLadyGagaMapper;
    }

    /**
     * Return a {@link Page} of {@link AlLadyGagaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlLadyGagaDTO> findByCriteria(AlLadyGagaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlLadyGaga> specification = createSpecification(criteria);
        return alLadyGagaRepository.findAll(specification, page).map(alLadyGagaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlLadyGagaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlLadyGaga> specification = createSpecification(criteria);
        return alLadyGagaRepository.count(specification);
    }

    /**
     * Function to convert {@link AlLadyGagaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlLadyGaga> createSpecification(AlLadyGagaCriteria criteria) {
        Specification<AlLadyGaga> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlLadyGaga_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AlLadyGaga_.name));
            }
            if (criteria.getDescriptionHeitiga() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getDescriptionHeitiga(), AlLadyGaga_.descriptionHeitiga)
                );
            }
            if (criteria.getAddressId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAddressId(), root ->
                        root.join(AlLadyGaga_.address, JoinType.LEFT).get(AndreiRightHand_.id)
                    )
                );
            }
            if (criteria.getAvatarId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAvatarId(), root -> root.join(AlLadyGaga_.avatar, JoinType.LEFT).get(Metaverse_.id))
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlLadyGaga_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
