package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.AlPacinoVoucher;
import ai.realworld.repository.AlPacinoVoucherRepository;
import ai.realworld.service.criteria.AlPacinoVoucherCriteria;
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
 * Service for executing complex queries for {@link AlPacinoVoucher} entities in the database.
 * The main input is a {@link AlPacinoVoucherCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlPacinoVoucher} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlPacinoVoucherQueryService extends QueryService<AlPacinoVoucher> {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoVoucherQueryService.class);

    private final AlPacinoVoucherRepository alPacinoVoucherRepository;

    public AlPacinoVoucherQueryService(AlPacinoVoucherRepository alPacinoVoucherRepository) {
        this.alPacinoVoucherRepository = alPacinoVoucherRepository;
    }

    /**
     * Return a {@link Page} of {@link AlPacinoVoucher} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlPacinoVoucher> findByCriteria(AlPacinoVoucherCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlPacinoVoucher> specification = createSpecification(criteria);
        return alPacinoVoucherRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlPacinoVoucherCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlPacinoVoucher> specification = createSpecification(criteria);
        return alPacinoVoucherRepository.count(specification);
    }

    /**
     * Function to convert {@link AlPacinoVoucherCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlPacinoVoucher> createSpecification(AlPacinoVoucherCriteria criteria) {
        Specification<AlPacinoVoucher> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), AlPacinoVoucher_.id));
            }
            if (criteria.getSourceTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceTitle(), AlPacinoVoucher_.sourceTitle));
            }
            if (criteria.getSourceUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceUrl(), AlPacinoVoucher_.sourceUrl));
            }
            if (criteria.getCollectedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCollectedDate(), AlPacinoVoucher_.collectedDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(AlPacinoVoucher_.user, JoinType.LEFT).get(AlPacino_.id))
                );
            }
            if (criteria.getVoucherId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getVoucherId(), root -> root.join(AlPacinoVoucher_.voucher, JoinType.LEFT).get(AlVueVue_.id)
                    )
                );
            }
            if (criteria.getApplicationId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getApplicationId(), root ->
                        root.join(AlPacinoVoucher_.application, JoinType.LEFT).get(JohnLennon_.id)
                    )
                );
            }
        }
        return specification;
    }
}
