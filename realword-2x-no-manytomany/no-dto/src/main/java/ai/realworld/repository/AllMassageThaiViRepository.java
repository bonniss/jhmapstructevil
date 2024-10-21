package ai.realworld.repository;

import ai.realworld.domain.AllMassageThaiVi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AllMassageThaiVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AllMassageThaiViRepository extends JpaRepository<AllMassageThaiVi, Long>, JpaSpecificationExecutor<AllMassageThaiVi> {}
