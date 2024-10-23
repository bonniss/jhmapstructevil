package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextSupplierAlpha;

/**
 * Spring Data JPA repository for the NextSupplierAlpha entity.
 *
 * When extending this class, extend NextSupplierAlphaRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface NextSupplierAlphaRepository
    extends
        NextSupplierAlphaRepositoryWithBagRelationships,
        JpaRepository<NextSupplierAlpha, Long>,
        JpaSpecificationExecutor<NextSupplierAlpha> {
    default Optional<NextSupplierAlpha> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<NextSupplierAlpha> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<NextSupplierAlpha> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
