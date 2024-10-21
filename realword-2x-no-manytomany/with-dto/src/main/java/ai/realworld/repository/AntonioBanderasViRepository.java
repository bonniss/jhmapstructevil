package ai.realworld.repository;

import ai.realworld.domain.AntonioBanderasVi;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AntonioBanderasVi entity.
 */
@Repository
public interface AntonioBanderasViRepository extends JpaRepository<AntonioBanderasVi, Long>, JpaSpecificationExecutor<AntonioBanderasVi> {
    default Optional<AntonioBanderasVi> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AntonioBanderasVi> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AntonioBanderasVi> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select antonioBanderasVi from AntonioBanderasVi antonioBanderasVi left join fetch antonioBanderasVi.parent",
        countQuery = "select count(antonioBanderasVi) from AntonioBanderasVi antonioBanderasVi"
    )
    Page<AntonioBanderasVi> findAllWithToOneRelationships(Pageable pageable);

    @Query("select antonioBanderasVi from AntonioBanderasVi antonioBanderasVi left join fetch antonioBanderasVi.parent")
    List<AntonioBanderasVi> findAllWithToOneRelationships();

    @Query(
        "select antonioBanderasVi from AntonioBanderasVi antonioBanderasVi left join fetch antonioBanderasVi.parent where antonioBanderasVi.id =:id"
    )
    Optional<AntonioBanderasVi> findOneWithToOneRelationships(@Param("id") Long id);
}
