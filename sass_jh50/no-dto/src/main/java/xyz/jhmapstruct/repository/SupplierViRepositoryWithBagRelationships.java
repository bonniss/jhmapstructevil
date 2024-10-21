package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.SupplierVi;

public interface SupplierViRepositoryWithBagRelationships {
    Optional<SupplierVi> fetchBagRelationships(Optional<SupplierVi> supplierVi);

    List<SupplierVi> fetchBagRelationships(List<SupplierVi> supplierVis);

    Page<SupplierVi> fetchBagRelationships(Page<SupplierVi> supplierVis);
}
