package ai.realworld.repository;

import ai.realworld.domain.AlMenityVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlMenityVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlMenityViRepository extends JpaRepository<AlMenityVi, Long>, JpaSpecificationExecutor<AlMenityVi> {}
