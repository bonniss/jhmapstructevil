package ai.realworld.repository;

import ai.realworld.domain.AlPyuDjibril;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPyuDjibril entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPyuDjibrilRepository extends JpaRepository<AlPyuDjibril, Long>, JpaSpecificationExecutor<AlPyuDjibril> {}
