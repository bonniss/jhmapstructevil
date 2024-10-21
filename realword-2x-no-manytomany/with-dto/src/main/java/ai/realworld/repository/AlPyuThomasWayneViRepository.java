package ai.realworld.repository;

import ai.realworld.domain.AlPyuThomasWayneVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPyuThomasWayneVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPyuThomasWayneViRepository
    extends JpaRepository<AlPyuThomasWayneVi, Long>, JpaSpecificationExecutor<AlPyuThomasWayneVi> {}
