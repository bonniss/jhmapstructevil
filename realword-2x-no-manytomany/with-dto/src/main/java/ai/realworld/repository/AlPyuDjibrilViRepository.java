package ai.realworld.repository;

import ai.realworld.domain.AlPyuDjibrilVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPyuDjibrilVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPyuDjibrilViRepository extends JpaRepository<AlPyuDjibrilVi, Long>, JpaSpecificationExecutor<AlPyuDjibrilVi> {}
