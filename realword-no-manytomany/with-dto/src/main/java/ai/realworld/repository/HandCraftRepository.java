package ai.realworld.repository;

import ai.realworld.domain.HandCraft;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HandCraft entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HandCraftRepository extends JpaRepository<HandCraft, Long>, JpaSpecificationExecutor<HandCraft> {}
