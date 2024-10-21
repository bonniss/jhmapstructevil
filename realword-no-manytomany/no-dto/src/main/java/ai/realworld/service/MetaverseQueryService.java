package ai.realworld.service;

import ai.realworld.domain.*; // for static metamodels
import ai.realworld.domain.Metaverse;
import ai.realworld.repository.MetaverseRepository;
import ai.realworld.service.criteria.MetaverseCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Metaverse} entities in the database.
 * The main input is a {@link MetaverseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Metaverse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetaverseQueryService extends QueryService<Metaverse> {

    private static final Logger LOG = LoggerFactory.getLogger(MetaverseQueryService.class);

    private final MetaverseRepository metaverseRepository;

    public MetaverseQueryService(MetaverseRepository metaverseRepository) {
        this.metaverseRepository = metaverseRepository;
    }

    /**
     * Return a {@link Page} of {@link Metaverse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Metaverse> findByCriteria(MetaverseCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Metaverse> specification = createSpecification(criteria);
        return metaverseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MetaverseCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Metaverse> specification = createSpecification(criteria);
        return metaverseRepository.count(specification);
    }

    /**
     * Function to convert {@link MetaverseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Metaverse> createSpecification(MetaverseCriteria criteria) {
        Specification<Metaverse> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Metaverse_.id));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFilename(), Metaverse_.filename));
            }
            if (criteria.getContentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentType(), Metaverse_.contentType));
            }
            if (criteria.getFileExt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileExt(), Metaverse_.fileExt));
            }
            if (criteria.getFileSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileSize(), Metaverse_.fileSize));
            }
            if (criteria.getFileUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUrl(), Metaverse_.fileUrl));
            }
            if (criteria.getThumbnailUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getThumbnailUrl(), Metaverse_.thumbnailUrl));
            }
            if (criteria.getBlurhash() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBlurhash(), Metaverse_.blurhash));
            }
            if (criteria.getObjectName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObjectName(), Metaverse_.objectName));
            }
            if (criteria.getObjectMetaJason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObjectMetaJason(), Metaverse_.objectMetaJason));
            }
            if (criteria.getUrlLifespanInSeconds() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getUrlLifespanInSeconds(), Metaverse_.urlLifespanInSeconds)
                );
            }
            if (criteria.getUrlExpiredDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUrlExpiredDate(), Metaverse_.urlExpiredDate));
            }
            if (criteria.getAutoRenewUrl() != null) {
                specification = specification.and(buildSpecification(criteria.getAutoRenewUrl(), Metaverse_.autoRenewUrl));
            }
            if (criteria.getIsEnabled() != null) {
                specification = specification.and(buildSpecification(criteria.getIsEnabled(), Metaverse_.isEnabled));
            }
        }
        return specification;
    }
}
