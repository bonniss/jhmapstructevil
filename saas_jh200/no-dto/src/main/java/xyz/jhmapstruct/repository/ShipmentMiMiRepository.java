package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ShipmentMiMi;

/**
 * Spring Data JPA repository for the ShipmentMiMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentMiMiRepository extends JpaRepository<ShipmentMiMi, Long>, JpaSpecificationExecutor<ShipmentMiMi> {}
