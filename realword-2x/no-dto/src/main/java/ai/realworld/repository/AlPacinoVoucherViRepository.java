package ai.realworld.repository;

import ai.realworld.domain.AlPacinoVoucherVi;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPacinoVoucherVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPacinoVoucherViRepository extends JpaRepository<AlPacinoVoucherVi, UUID>, JpaSpecificationExecutor<AlPacinoVoucherVi> {}
