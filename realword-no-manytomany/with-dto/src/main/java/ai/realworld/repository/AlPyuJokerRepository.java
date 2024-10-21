package ai.realworld.repository;

import ai.realworld.domain.AlPyuJoker;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPyuJoker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPyuJokerRepository extends JpaRepository<AlPyuJoker, UUID>, JpaSpecificationExecutor<AlPyuJoker> {}
