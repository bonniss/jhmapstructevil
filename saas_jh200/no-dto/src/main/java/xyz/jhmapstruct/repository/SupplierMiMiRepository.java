package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.SupplierMiMi;

/**
 * Spring Data JPA repository for the SupplierMiMi entity.
 *
 * When extending this class, extend SupplierMiMiRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SupplierMiMiRepository
    extends SupplierMiMiRepositoryWithBagRelationships, JpaRepository<SupplierMiMi, Long>, JpaSpecificationExecutor<SupplierMiMi> {
    default Optional<SupplierMiMi> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<SupplierMiMi> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<SupplierMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
