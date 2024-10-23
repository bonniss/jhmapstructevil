package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.SupplierAlpha;

/**
 * Spring Data JPA repository for the SupplierAlpha entity.
 *
 * When extending this class, extend SupplierAlphaRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SupplierAlphaRepository
    extends SupplierAlphaRepositoryWithBagRelationships, JpaRepository<SupplierAlpha, Long>, JpaSpecificationExecutor<SupplierAlpha> {
    default Optional<SupplierAlpha> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<SupplierAlpha> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<SupplierAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
