package ai.realworld.repository;

import ai.realworld.domain.AlProPro;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlProPro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlProProRepository extends JpaRepository<AlProPro, UUID>, JpaSpecificationExecutor<AlProPro> {}
