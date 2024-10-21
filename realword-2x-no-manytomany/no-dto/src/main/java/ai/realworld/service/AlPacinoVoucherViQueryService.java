package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPacinoVoucherVi;
import ai.realworld.repository.AlPacinoVoucherViRepository;
import ai.realworld.service.criteria.AlPacinoVoucherViCriteria;
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
 * Service for executing complex queries for {@link AlPacinoVoucherVi} entities in the database.
 * The main input is a {@link AlPacinoVoucherViCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPacinoVoucherVi} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPacinoVoucherViQueryService extends QueryService<AlPacinoVoucherVi> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoVoucherViQueryService.class);

    private final AlPacinoVoucherViRepository alPacinoVoucherViRepository;

    public AlPacinoVoucherViQueryService(AlPacinoVoucherViRepository alPacinoVoucherViRepository) {
        this.alPacinoVoucherViRepository = alPacinoVoucherViRepository;
    }

    /**
     * Return a {@link Page} of {@link AlPacinoVoucherVi} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPacinoVoucherVi> findByCriteria(AlPacinoVoucherViCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPacinoVoucherVi> specification = createSpecification(criteria);
        return alPacinoVoucherViRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPacinoVoucherViCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPacinoVoucherVi> specification = createSpecification(criteria);
        return alPacinoVoucherViRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPacinoVoucherViCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPacinoVoucherVi> createSpecification(AlPacinoVoucherViCriteria criteria) {
        Specification<AlPacinoVoucherVi> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlPacinoVoucherVi_.id));
            }
            if (criteria.getSourceTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceTitle(), AlPacinoVoucherVi_.sourceTitle));
            }
            if (criteria.getSourceUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceUrl(), AlPacinoVoucherVi_.sourceUrl));
            }
            if (criteria.getCollectedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCollectedDate(), AlPacinoVoucherVi_.collectedDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(AlPacinoVoucherVi_.user, JoinType.LEFT).get(AlPacino_.id))
                );
            }
            if (criteria.getVoucherId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getVoucherId(), root ->
                        root.join(AlPacinoVoucherVi_.voucher, JoinType.LEFT).get(AlVueVueVi_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPacinoVoucherVi_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
