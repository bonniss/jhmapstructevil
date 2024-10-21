package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlInquiryVi;
import ai.realworld.repository.AlInquiryViRepository;
import ai.realworld.service.criteria.AlInquiryViCriteria;
import ai.realworld.service.dto.AlInquiryViDTO;
import ai.realworld.service.mapper.AlInquiryViMapper;
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
 * Service for executing complex queries for {@link AlInquiryVi} entities in the database.
 * The main input is a {@link AlInquiryViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlInquiryViDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlInquiryViQueryService extends QueryService<AlInquiryVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlInquiryViQueryService.class);

    private final AlInquiryViRepository alInquiryViRepository;

    private final AlInquiryViMapper alInquiryViMapper;

    public AlInquiryViQueryService(AlInquiryViRepository alInquiryViRepository, AlInquiryViMapper alInquiryViMapper) {
        this.alInquiryViRepository = alInquiryViRepository;
        this.alInquiryViMapper = alInquiryViMapper;
    }

    /**
     * Return a {@link Page} of {@link AlInquiryViDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlInquiryViDTO> findByCriteria(AlInquiryViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlInquiryVi> specification = createSpecification(criteria);
        return alInquiryViRepository.findAll(specification, page).map(alInquiryViMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlInquiryViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlInquiryVi> specification = createSpecification(criteria);
        return alInquiryViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlInquiryViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlInquiryVi> createSpecification(AlInquiryViCriteria criteria) {
        Specification<AlInquiryVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlInquiryVi_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AlInquiryVi_.title));
            }
            if (criteria.getBody() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBody(), AlInquiryVi_.body));
            }
            if (criteria.getSender() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSender(), AlInquiryVi_.sender));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), AlInquiryVi_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), AlInquiryVi_.phone));
            }
            if (criteria.getContentJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentJason(), AlInquiryVi_.contentJason));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(AlInquiryVi_.customer, JoinType.LEFT).get(AlPacino_.id))
                );
            }
            if (criteria.getAgencyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgencyId(), root -> root.join(AlInquiryVi_.agency, JoinType.LEFT).get(AlAppleVi_.id))
                );
            }
            if (criteria.getPersonInChargeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPersonInChargeId(), root ->
                        root.join(AlInquiryVi_.personInCharge, JoinType.LEFT).get(EdSheeranVi_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlInquiryVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
