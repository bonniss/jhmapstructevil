package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextSupplierViVi;

/**
 * Spring Data JPA repository for the NextSupplierViVi entity.
 *
 * When extending this class, extend NextSupplierViViRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface NextSupplierViViRepository
    extends
        NextSupplierViViRepositoryWithBagRelationships, JpaRepository<NextSupplierViVi, Long>, JpaSpecificationExecutor<NextSupplierViVi> {
    default Optional<NextSupplierViVi> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<NextSupplierViVi> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<NextSupplierViVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
