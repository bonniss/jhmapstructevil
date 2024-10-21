package ai.realworld.repository;

import ai.realworld.domain.AlProtyVi;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlProtyVi entity.
 *
 * When extending this class, extend AlProtyViRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AlProtyViRepository
    extends AlProtyViRepositoryWithBagRelationships, JpaRepository<AlProtyVi, UUID>, JpaSpecificationExecutor<AlProtyVi> {
    default Optional<AlProtyVi> findOneWithEagerRelationships(UUID id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<AlProtyVi> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<AlProtyVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
