package ai.realworld.repository;

import ai.realworld.domain.AlPacino;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPacino entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPacinoRepository extends JpaRepository<AlPacino, UUID>, JpaSpecificationExecutor<AlPacino> {}
