package xyz.jhmapstruct.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import xyz.jhmapstruct.domain.CustomerTheta;

/**
 * Spring Data JPA repository for the CustomerTheta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerThetaRepository extends JpaRepository<CustomerTheta, Long>, JpaSpecificationExecutor<CustomerTheta> {}
