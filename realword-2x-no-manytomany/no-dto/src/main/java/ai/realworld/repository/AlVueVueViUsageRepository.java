package ai.realworld.repository;

import ai.realworld.domain.AlVueVueViUsage;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlVueVueViUsage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlVueVueViUsageRepository extends JpaRepository<AlVueVueViUsage, UUID>, JpaSpecificationExecutor<AlVueVueViUsage> {}
