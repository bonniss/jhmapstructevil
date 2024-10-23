package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.SupplierSigma;

public interface SupplierSigmaRepositoryWithBagRelationships {
    Optional<SupplierSigma> fetchBagRelationships(Optional<SupplierSigma> supplierSigma);

    List<SupplierSigma> fetchBagRelationships(List<SupplierSigma> supplierSigmas);

    Page<SupplierSigma> fetchBagRelationships(Page<SupplierSigma> supplierSigmas);
}
