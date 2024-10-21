package ai.realworld.repository;

import ai.realworld.domain.AllMassageThai;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AllMassageThai entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AllMassageThaiRepository extends JpaRepository<AllMassageThai, Long>, JpaSpecificationExecutor<AllMassageThai> {}
