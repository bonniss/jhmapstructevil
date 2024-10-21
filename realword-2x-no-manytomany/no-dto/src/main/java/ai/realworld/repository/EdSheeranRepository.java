package ai.realworld.repository;

import ai.realworld.domain.EdSheeran;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EdSheeran entity.
 */
@Repository
public interface EdSheeranRepository extends JpaRepository<EdSheeran, Long>, JpaSpecificationExecutor<EdSheeran> {
    @Query("select edSheeran from EdSheeran edSheeran where edSheeran.internalUser.login = ?#{authentication.name}")
    List<EdSheeran> findByInternalUserIsCurrentUser();

    default Optional<EdSheeran> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EdSheeran> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EdSheeran> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select edSheeran from EdSheeran edSheeran left join fetch edSheeran.internalUser",
        countQuery = "select count(edSheeran) from EdSheeran edSheeran"
    )
    Page<EdSheeran> findAllWithToOneRelationships(Pageable pageable);

    @Query("select edSheeran from EdSheeran edSheeran left join fetch edSheeran.internalUser")
    List<EdSheeran> findAllWithToOneRelationships();

    @Query("select edSheeran from EdSheeran edSheeran left join fetch edSheeran.internalUser where edSheeran.id =:id")
    Optional<EdSheeran> findOneWithToOneRelationships(@Param("id") Long id);
}
