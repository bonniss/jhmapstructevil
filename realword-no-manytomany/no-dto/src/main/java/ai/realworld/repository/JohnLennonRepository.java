package ai.realworld.repository;

import ai.realworld.domain.JohnLennon;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the JohnLennon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JohnLennonRepository extends JpaRepository<JohnLennon, UUID>, JpaSpecificationExecutor<JohnLennon> {}
