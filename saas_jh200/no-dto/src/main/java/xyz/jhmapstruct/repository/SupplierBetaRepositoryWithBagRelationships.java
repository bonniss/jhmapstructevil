package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.SupplierBeta;

public interface SupplierBetaRepositoryWithBagRelationships {
    Optional<SupplierBeta> fetchBagRelationships(Optional<SupplierBeta> supplierBeta);

    List<SupplierBeta> fetchBagRelationships(List<SupplierBeta> supplierBetas);

    Page<SupplierBeta> fetchBagRelationships(Page<SupplierBeta> supplierBetas);
}
