package ai.realworld.repository;

import ai.realworld.domain.AlBestToothVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlBestToothVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlBestToothViRepository extends JpaRepository<AlBestToothVi, Long>, JpaSpecificationExecutor<AlBestToothVi> {}
