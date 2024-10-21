package ai.realworld.repository;

import ai.realworld.domain.AlSherMaleVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlSherMaleVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlSherMaleViRepository extends JpaRepository<AlSherMaleVi, Long>, JpaSpecificationExecutor<AlSherMaleVi> {}
