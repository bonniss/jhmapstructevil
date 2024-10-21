package ai.realworld.repository;

import ai.realworld.domain.AlVueVueUsage;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlVueVueUsage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlVueVueUsageRepository extends JpaRepository<AlVueVueUsage, UUID>, JpaSpecificationExecutor<AlVueVueUsage> {}
