package ai.realworld.repository;

import ai.realworld.domain.AlGoogleMeetVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlGoogleMeetVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlGoogleMeetViRepository extends JpaRepository<AlGoogleMeetVi, UUID>, JpaSpecificationExecutor<AlGoogleMeetVi> {}
