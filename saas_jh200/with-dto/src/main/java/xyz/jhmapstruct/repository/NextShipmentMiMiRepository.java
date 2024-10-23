package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextShipmentMiMi;

/**
 * Spring Data JPA repository for the NextShipmentMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextShipmentMiMiRepository extends JpaRepository<NextShipmentMiMi, Long>, JpaSpecificationExecutor<NextShipmentMiMi> {}
