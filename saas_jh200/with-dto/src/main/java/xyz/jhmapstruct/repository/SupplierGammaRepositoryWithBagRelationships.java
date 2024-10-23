package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.SupplierGamma;

public interface SupplierGammaRepositoryWithBagRelationships {
    Optional<SupplierGamma> fetchBagRelationships(Optional<SupplierGamma> supplierGamma);

    List<SupplierGamma> fetchBagRelationships(List<SupplierGamma> supplierGammas);

    Page<SupplierGamma> fetchBagRelationships(Page<SupplierGamma> supplierGammas);
}
