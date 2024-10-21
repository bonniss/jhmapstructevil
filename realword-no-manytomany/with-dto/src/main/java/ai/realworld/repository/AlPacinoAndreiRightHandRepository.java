package ai.realworld.repository;

import ai.realworld.domain.AlPacinoAndreiRightHand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPacinoAndreiRightHand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPacinoAndreiRightHandRepository
    extends JpaRepository<AlPacinoAndreiRightHand, Long>, JpaSpecificationExecutor<AlPacinoAndreiRightHand> {}
