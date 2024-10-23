package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ShipmentViVi;

/**
 * Spring Data JPA repository for the ShipmentViVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentViViRepository extends JpaRepository<ShipmentViVi, Long>, JpaSpecificationExecutor<ShipmentViVi> {}
