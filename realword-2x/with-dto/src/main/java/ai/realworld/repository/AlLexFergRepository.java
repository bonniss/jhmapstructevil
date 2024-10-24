package ai.realworld.repository;

import ai.realworld.domain.AlLexFerg;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlLexFerg entity.
 *
 * When extending this class, extend AlLexFergRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface AlLexFergRepository
    extends AlLexFergRepositoryWithBagRelationships, JpaRepository<AlLexFerg, Long>, JpaSpecificationExecutor<AlLexFerg> {
    default Optional<AlLexFerg> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<AlLexFerg> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<AlLexFerg> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
