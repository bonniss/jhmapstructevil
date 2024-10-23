package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextSupplierSigma;

/**
 * Spring Data JPA repository for the NextSupplierSigma entity.
 *
 * When extending this class, extend NextSupplierSigmaRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface NextSupplierSigmaRepository
    extends
        NextSupplierSigmaRepositoryWithBagRelationships,
        JpaRepository<NextSupplierSigma, Long>,
        JpaSpecificationExecutor<NextSupplierSigma> {
    default Optional<NextSupplierSigma> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<NextSupplierSigma> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<NextSupplierSigma> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
