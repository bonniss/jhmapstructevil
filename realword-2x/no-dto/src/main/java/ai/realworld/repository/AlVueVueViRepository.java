package ai.realworld.repository;

import ai.realworld.domain.AlVueVueVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlVueVueVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlVueVueViRepository extends JpaRepository<AlVueVueVi, UUID>, JpaSpecificationExecutor<AlVueVueVi> {}
