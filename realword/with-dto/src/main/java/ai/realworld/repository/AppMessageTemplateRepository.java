package ai.realworld.repository;

import ai.realworld.domain.AppMessageTemplate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppMessageTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppMessageTemplateRepository
    extends JpaRepository<AppMessageTemplate, Long>, JpaSpecificationExecutor<AppMessageTemplate> {}
