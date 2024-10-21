package ai.realworld.repository;

import ai.realworld.domain.AlGoogleMeet;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlGoogleMeet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlGoogleMeetRepository extends JpaRepository<AlGoogleMeet, UUID>, JpaSpecificationExecutor<AlGoogleMeet> {}
