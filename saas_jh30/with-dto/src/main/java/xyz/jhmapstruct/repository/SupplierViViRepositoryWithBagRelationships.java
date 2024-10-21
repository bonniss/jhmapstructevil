package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.SupplierViVi;

public interface SupplierViViRepositoryWithBagRelationships {
    Optional<SupplierViVi> fetchBagRelationships(Optional<SupplierViVi> supplierViVi);

    List<SupplierViVi> fetchBagRelationships(List<SupplierViVi> supplierViVis);

    Page<SupplierViVi> fetchBagRelationships(Page<SupplierViVi> supplierViVis);
}
