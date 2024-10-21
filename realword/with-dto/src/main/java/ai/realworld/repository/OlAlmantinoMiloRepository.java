package ai.realworld.repository;

import ai.realworld.domain.OlAlmantinoMilo;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OlAlmantinoMilo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OlAlmantinoMiloRepository extends JpaRepository<OlAlmantinoMilo, UUID>, JpaSpecificationExecutor<OlAlmantinoMilo> {}
