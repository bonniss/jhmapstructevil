package ai.realworld.repository;

import ai.realworld.domain.AlApple;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlApple entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlAppleRepository extends JpaRepository<AlApple, UUID>, JpaSpecificationExecutor<AlApple> {}
