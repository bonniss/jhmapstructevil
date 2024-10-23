package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextSupplierTheta;

/**
 * Spring Data JPA repository for the NextSupplierTheta entity.
 *
 * When extending this class, extend NextSupplierThetaRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface NextSupplierThetaRepository
    extends
        NextSupplierThetaRepositoryWithBagRelationships,
        JpaRepository<NextSupplierTheta, Long>,
        JpaSpecificationExecutor<NextSupplierTheta> {
    default Optional<NextSupplierTheta> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<NextSupplierTheta> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<NextSupplierTheta> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
