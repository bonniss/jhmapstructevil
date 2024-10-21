package ai.realworld.repository;

import ai.realworld.domain.AlLadyGagaVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlLadyGagaVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlLadyGagaViRepository extends JpaRepository<AlLadyGagaVi, UUID>, JpaSpecificationExecutor<AlLadyGagaVi> {}
