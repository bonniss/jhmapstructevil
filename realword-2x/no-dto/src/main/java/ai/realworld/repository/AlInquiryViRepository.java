package ai.realworld.repository;

import ai.realworld.domain.AlInquiryVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlInquiryVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlInquiryViRepository extends JpaRepository<AlInquiryVi, UUID>, JpaSpecificationExecutor<AlInquiryVi> {}
