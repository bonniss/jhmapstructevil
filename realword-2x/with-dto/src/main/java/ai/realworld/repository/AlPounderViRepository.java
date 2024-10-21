package ai.realworld.repository;

import ai.realworld.domain.AlPounderVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPounderVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPounderViRepository extends JpaRepository<AlPounderVi, Long>, JpaSpecificationExecutor<AlPounderVi> {}
