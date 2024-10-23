package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextShipmentMi;

/**
 * Spring Data JPA repository for the NextShipmentMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextShipmentMiRepository extends JpaRepository<NextShipmentMi, Long>, JpaSpecificationExecutor<NextShipmentMi> {}
