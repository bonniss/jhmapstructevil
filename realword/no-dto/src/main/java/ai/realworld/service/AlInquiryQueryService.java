package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlInquiry;
import ai.realworld.repository.AlInquiryRepository;
import ai.realworld.service.criteria.AlInquiryCriteria;
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
 * Service for executing complex queries for {@link AlInquiry} entities in the database.
 * The main input is a {@link AlInquiryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlInquiry} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlInquiryQueryService extends QueryService<AlInquiry> {

    private static final Logger LOG = LoggerFactory.getLogger(AlInquiryQueryService.class);

    private final AlInquiryRepository alInquiryRepository;

    public AlInquiryQueryService(AlInquiryRepository alInquiryRepository) {
        this.alInquiryRepository = alInquiryRepository;
    }

    /**
     * Return a {@link Page} of {@link AlInquiry} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlInquiry> findByCriteria(AlInquiryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlInquiry> specification = createSpecification(criteria);
        return alInquiryRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlInquiryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlInquiry> specification = createSpecification(criteria);
        return alInquiryRepository.count(specification);
    }

    /**
     * Function to convert {@link AlInquiryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlInquiry> createSpecification(AlInquiryCriteria criteria) {
        Specification<AlInquiry> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlInquiry_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), AlInquiry_.title));
            }
            if (criteria.getBody() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBody(), AlInquiry_.body));
            }
            if (criteria.getSender() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSender(), AlInquiry_.sender));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), AlInquiry_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), AlInquiry_.phone));
            }
            if (criteria.getContentJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentJason(), AlInquiry_.contentJason));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(AlInquiry_.customer, JoinType.LEFT).get(AlPacino_.id))
                );
            }
            if (criteria.getAgencyId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAgencyId(), root -> root.join(AlInquiry_.agency, JoinType.LEFT).get(AlApple_.id))
                );
            }
            if (criteria.getPersonInChargeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPersonInChargeId(), root ->
                        root.join(AlInquiry_.personInCharge, JoinType.LEFT).get(EdSheeran_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlInquiry_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
