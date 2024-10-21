package ai.realworld.repository;

import ai.realworld.domain.AlVueVue;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlVueVue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlVueVueRepository extends JpaRepository<AlVueVue, UUID>, JpaSpecificationExecutor<AlVueVue> {}
