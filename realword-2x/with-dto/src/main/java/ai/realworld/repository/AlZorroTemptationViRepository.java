package ai.realworld.repository;

import ai.realworld.domain.AlZorroTemptationVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlZorroTemptationVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlZorroTemptationViRepository
    extends JpaRepository<AlZorroTemptationVi, Long>, JpaSpecificationExecutor<AlZorroTemptationVi> {}
