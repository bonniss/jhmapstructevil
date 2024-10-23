package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.SupplierTheta;

/**
 * Spring Data JPA repository for the SupplierTheta entity.
 *
 * When extending this class, extend SupplierThetaRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SupplierThetaRepository
    extends SupplierThetaRepositoryWithBagRelationships, JpaRepository<SupplierTheta, Long>, JpaSpecificationExecutor<SupplierTheta> {
    default Optional<SupplierTheta> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<SupplierTheta> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<SupplierTheta> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
