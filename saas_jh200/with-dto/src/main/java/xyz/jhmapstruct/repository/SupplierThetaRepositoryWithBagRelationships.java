package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.SupplierTheta;

public interface SupplierThetaRepositoryWithBagRelationships {
    Optional<SupplierTheta> fetchBagRelationships(Optional<SupplierTheta> supplierTheta);

    List<SupplierTheta> fetchBagRelationships(List<SupplierTheta> supplierThetas);

    Page<SupplierTheta> fetchBagRelationships(Page<SupplierTheta> supplierThetas);
}
