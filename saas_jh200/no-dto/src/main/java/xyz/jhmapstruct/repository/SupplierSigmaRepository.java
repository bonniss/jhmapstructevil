package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.SupplierSigma;

/**
 * Spring Data JPA repository for the SupplierSigma entity.
 *
 * When extending this class, extend SupplierSigmaRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SupplierSigmaRepository
    extends SupplierSigmaRepositoryWithBagRelationships, JpaRepository<SupplierSigma, Long>, JpaSpecificationExecutor<SupplierSigma> {
    default Optional<SupplierSigma> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<SupplierSigma> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<SupplierSigma> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
