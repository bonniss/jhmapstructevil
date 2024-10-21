package ai.realworld.repository;

import ai.realworld.domain.AntonioBanderas;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AntonioBanderas entity.
 */
@Repository
public interface AntonioBanderasRepository extends JpaRepository<AntonioBanderas, Long>, JpaSpecificationExecutor<AntonioBanderas> {
    default Optional<AntonioBanderas> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AntonioBanderas> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AntonioBanderas> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select antonioBanderas from AntonioBanderas antonioBanderas left join fetch antonioBanderas.parent",
        countQuery = "select count(antonioBanderas) from AntonioBanderas antonioBanderas"
    )
    Page<AntonioBanderas> findAllWithToOneRelationships(Pageable pageable);

    @Query("select antonioBanderas from AntonioBanderas antonioBanderas left join fetch antonioBanderas.parent")
    List<AntonioBanderas> findAllWithToOneRelationships();

    @Query(
        "select antonioBanderas from AntonioBanderas antonioBanderas left join fetch antonioBanderas.parent where antonioBanderas.id =:id"
    )
    Optional<AntonioBanderas> findOneWithToOneRelationships(@Param("id") Long id);
}
