package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.SupplierVi;

/**
 * Spring Data JPA repository for the SupplierVi entity.
 *
 * When extending this class, extend SupplierViRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SupplierViRepository
    extends SupplierViRepositoryWithBagRelationships, JpaRepository<SupplierVi, Long>, JpaSpecificationExecutor<SupplierVi> {
    default Optional<SupplierVi> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<SupplierVi> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<SupplierVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
