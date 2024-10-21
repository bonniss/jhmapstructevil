package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.MasterTenant;

/**
 * Spring Data JPA repository for the MasterTenant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MasterTenantRepository extends JpaRepository<MasterTenant, Long> {}
