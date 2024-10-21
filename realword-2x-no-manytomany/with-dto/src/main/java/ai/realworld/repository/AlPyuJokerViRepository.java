package ai.realworld.repository;

import ai.realworld.domain.AlPyuJokerVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPyuJokerVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPyuJokerViRepository extends JpaRepository<AlPyuJokerVi, UUID>, JpaSpecificationExecutor<AlPyuJokerVi> {}
