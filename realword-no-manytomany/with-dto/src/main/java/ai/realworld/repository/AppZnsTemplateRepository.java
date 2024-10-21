package ai.realworld.repository;

import ai.realworld.domain.AppZnsTemplate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppZnsTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppZnsTemplateRepository extends JpaRepository<AppZnsTemplate, Long>, JpaSpecificationExecutor<AppZnsTemplate> {}
