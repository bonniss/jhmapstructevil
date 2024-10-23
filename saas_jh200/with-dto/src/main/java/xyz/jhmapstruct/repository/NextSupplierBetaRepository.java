package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextSupplierBeta;

/**
 * Spring Data JPA repository for the NextSupplierBeta entity.
 *
 * When extending this class, extend NextSupplierBetaRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface NextSupplierBetaRepository
    extends
        NextSupplierBetaRepositoryWithBagRelationships, JpaRepository<NextSupplierBeta, Long>, JpaSpecificationExecutor<NextSupplierBeta> {
    default Optional<NextSupplierBeta> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<NextSupplierBeta> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<NextSupplierBeta> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
