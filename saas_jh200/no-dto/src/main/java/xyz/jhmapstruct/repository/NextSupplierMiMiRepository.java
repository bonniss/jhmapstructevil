package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextSupplierMiMi;

/**
 * Spring Data JPA repository for the NextSupplierMiMi entity.
 *
 * When extending this class, extend NextSupplierMiMiRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface NextSupplierMiMiRepository
    extends
        NextSupplierMiMiRepositoryWithBagRelationships, JpaRepository<NextSupplierMiMi, Long>, JpaSpecificationExecutor<NextSupplierMiMi> {
    default Optional<NextSupplierMiMi> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<NextSupplierMiMi> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<NextSupplierMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
