package ai.realworld.repository;

import ai.realworld.domain.AlInquiry;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlInquiry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlInquiryRepository extends JpaRepository<AlInquiry, UUID>, JpaSpecificationExecutor<AlInquiry> {}
