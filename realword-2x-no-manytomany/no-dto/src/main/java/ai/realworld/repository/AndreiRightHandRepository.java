package ai.realworld.repository;

import ai.realworld.domain.AndreiRightHand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AndreiRightHand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AndreiRightHandRepository extends JpaRepository<AndreiRightHand, Long>, JpaSpecificationExecutor<AndreiRightHand> {}
