package ai.realworld.repository;

import ai.realworld.domain.AlCatalinaVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlCatalinaVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlCatalinaViRepository extends JpaRepository<AlCatalinaVi, Long>, JpaSpecificationExecutor<AlCatalinaVi> {}
