package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.SupplierAlpha;

public interface SupplierAlphaRepositoryWithBagRelationships {
    Optional<SupplierAlpha> fetchBagRelationships(Optional<SupplierAlpha> supplierAlpha);

    List<SupplierAlpha> fetchBagRelationships(List<SupplierAlpha> supplierAlphas);

    Page<SupplierAlpha> fetchBagRelationships(Page<SupplierAlpha> supplierAlphas);
}
