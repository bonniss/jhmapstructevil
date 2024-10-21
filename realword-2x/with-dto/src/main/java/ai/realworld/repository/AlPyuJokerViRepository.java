package ai.realworld.repository;

import ai.realworld.domain.AlPyuJokerVi;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPyuJokerVi entity.
 *
 * When extending this class, extend AlPyuJokerViRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AlPyuJokerViRepository
    extends AlPyuJokerViRepositoryWithBagRelationships, JpaRepository<AlPyuJokerVi, UUID>, JpaSpecificationExecutor<AlPyuJokerVi> {
    default Optional<AlPyuJokerVi> findOneWithEagerRelationships(UUID id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<AlPyuJokerVi> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<AlPyuJokerVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
