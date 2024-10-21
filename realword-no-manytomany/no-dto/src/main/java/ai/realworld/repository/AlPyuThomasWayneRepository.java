package ai.realworld.repository;

import ai.realworld.domain.AlPyuThomasWayne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPyuThomasWayne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPyuThomasWayneRepository extends JpaRepository<AlPyuThomasWayne, Long>, JpaSpecificationExecutor<AlPyuThomasWayne> {}
