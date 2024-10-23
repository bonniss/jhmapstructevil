package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextSupplierGamma;

/**
 * Spring Data JPA repository for the NextSupplierGamma entity.
 *
 * When extending this class, extend NextSupplierGammaRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface NextSupplierGammaRepository
    extends
        NextSupplierGammaRepositoryWithBagRelationships,
        JpaRepository<NextSupplierGamma, Long>,
        JpaSpecificationExecutor<NextSupplierGamma> {
    default Optional<NextSupplierGamma> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<NextSupplierGamma> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<NextSupplierGamma> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
