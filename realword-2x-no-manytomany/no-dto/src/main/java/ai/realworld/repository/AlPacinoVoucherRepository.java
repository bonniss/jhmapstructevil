package ai.realworld.repository;

import ai.realworld.domain.AlPacinoVoucher;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AlPacinoVoucher entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlPacinoVoucherRepository extends JpaRepository<AlPacinoVoucher, UUID>, JpaSpecificationExecutor<AlPacinoVoucher> {}