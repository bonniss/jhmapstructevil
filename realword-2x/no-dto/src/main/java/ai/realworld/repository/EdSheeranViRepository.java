package ai.realworld.repository;

import ai.realworld.domain.EdSheeranVi;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EdSheeranVi entity.
 */
@Repository
public interface EdSheeranViRepository extends JpaRepository<EdSheeranVi, Long>, JpaSpecificationExecutor<EdSheeranVi> {
    @Query("select edSheeranVi from EdSheeranVi edSheeranVi where edSheeranVi.internalUser.login = ?#{authentication.name}")
    List<EdSheeranVi> findByInternalUserIsCurrentUser();

    default Optional<EdSheeranVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EdSheeranVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EdSheeranVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select edSheeranVi from EdSheeranVi edSheeranVi left join fetch edSheeranVi.internalUser",
        countQuery = "select count(edSheeranVi) from EdSheeranVi edSheeranVi"
    )
    Page<EdSheeranVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select edSheeranVi from EdSheeranVi edSheeranVi left join fetch edSheeranVi.internalUser")
    List<EdSheeranVi> findAllWithToOneRelationships();

    @Query("select edSheeranVi from EdSheeranVi edSheeranVi left join fetch edSheeranVi.internalUser where edSheeranVi.id =:id")
    Optional<EdSheeranVi> findOneWithToOneRelationships(@Param("id") Long id);
}
