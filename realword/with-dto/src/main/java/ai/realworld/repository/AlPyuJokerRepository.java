package ai.realworld.repository;

import ai.realworld.domain.AlPyuJoker;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPyuJoker entity.
 *
 * When extending this class, extend AlPyuJokerRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AlPyuJokerRepository
    extends AlPyuJokerRepositoryWithBagRelationships, JpaRepository<AlPyuJoker, UUID>, JpaSpecificationExecutor<AlPyuJoker> {
    default Optional<AlPyuJoker> findOneWithEagerRelationships(UUID id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<AlPyuJoker> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<AlPyuJoker> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
