package ai.realworld.repository;

import ai.realworld.domain.AlAlexType;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlAlexType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlAlexTypeRepository extends JpaRepository<AlAlexType, UUID>, JpaSpecificationExecutor<AlAlexType> {}
