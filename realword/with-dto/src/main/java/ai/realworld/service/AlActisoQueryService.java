package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlActiso;
import ai.realworld.repository.AlActisoRepository;
import ai.realworld.service.criteria.AlActisoCriteria;
import ai.realworld.service.dto.AlActisoDTO;
import ai.realworld.service.mapper.AlActisoMapper;
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
 * Service for executing complex queries for {@link AlActiso} entities in the database.
 * The main input is a {@link AlActisoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlActisoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlActisoQueryService extends QueryService<AlActiso> {

    private static final Logger LOG = LoggerFactory.getLogger(AlActisoQueryService.class);

    private final AlActisoRepository alActisoRepository;

    private final AlActisoMapper alActisoMapper;

    public AlActisoQueryService(AlActisoRepository alActisoRepository, AlActisoMapper alActisoMapper) {
        this.alActisoRepository = alActisoRepository;
        this.alActisoMapper = alActisoMapper;
    }

    /**
     * Return a {@link Page} of {@link AlActisoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlActisoDTO> findByCriteria(AlActisoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlActiso> specification = createSpecification(criteria);
        return alActisoRepository.findAll(specification, page).map(alActisoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlActisoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlActiso> specification = createSpecification(criteria);
        return alActisoRepository.count(specification);
    }

    /**
     * Function to convert {@link AlActisoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlActiso> createSpecification(AlActisoCriteria criteria) {
        Specification<AlActiso> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AlActiso_.id));
            }
            if (criteria.getKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKey(), AlActiso_.key));
            }
            if (criteria.getValueJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValueJason(), AlActiso_.valueJason));
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlActiso_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
