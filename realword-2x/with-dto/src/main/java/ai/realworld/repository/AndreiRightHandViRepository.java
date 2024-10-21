package ai.realworld.repository;

import ai.realworld.domain.AndreiRightHandVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AndreiRightHandVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AndreiRightHandViRepository extends JpaRepository<AndreiRightHandVi, Long>, JpaSpecificationExecutor<AndreiRightHandVi> {}
