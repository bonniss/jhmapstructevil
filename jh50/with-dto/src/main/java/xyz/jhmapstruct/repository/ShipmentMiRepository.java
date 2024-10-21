package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.ShipmentMi;

/**
 * Spring Data JPA repository for the ShipmentMi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentMiRepository extends JpaRepository<ShipmentMi, Long> {}
