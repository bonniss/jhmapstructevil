package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.NextShipmentViVi;

/**
 * Spring Data JPA repository for the NextShipmentViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextShipmentViViRepository extends JpaRepository<NextShipmentViVi, Long>, JpaSpecificationExecutor<NextShipmentViVi> {}
