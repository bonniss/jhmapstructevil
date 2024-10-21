package ai.realworld.repository;

import ai.realworld.domain.AlPacinoAndreiRightHandVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPacinoAndreiRightHandVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPacinoAndreiRightHandViRepository
    extends JpaRepository<AlPacinoAndreiRightHandVi, Long>, JpaSpecificationExecutor<AlPacinoAndreiRightHandVi> {}
