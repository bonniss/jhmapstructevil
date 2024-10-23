package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ShipmentVi;

/**
 * Spring Data JPA repository for the ShipmentVi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentViRepository extends JpaRepository<ShipmentVi, Long>, JpaSpecificationExecutor<ShipmentVi> {}
