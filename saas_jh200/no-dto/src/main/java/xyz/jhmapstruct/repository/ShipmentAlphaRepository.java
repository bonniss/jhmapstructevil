package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ShipmentAlpha;

/**
 * Spring Data JPA repository for the ShipmentAlpha entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentAlphaRepository extends JpaRepository<ShipmentAlpha, Long>, JpaSpecificationExecutor<ShipmentAlpha> {}
