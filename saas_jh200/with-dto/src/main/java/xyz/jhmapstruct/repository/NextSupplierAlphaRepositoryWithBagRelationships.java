package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.NextSupplierAlpha;

public interface NextSupplierAlphaRepositoryWithBagRelationships {
    Optional<NextSupplierAlpha> fetchBagRelationships(Optional<NextSupplierAlpha> nextSupplierAlpha);

    List<NextSupplierAlpha> fetchBagRelationships(List<NextSupplierAlpha> nextSupplierAlphas);

    Page<NextSupplierAlpha> fetchBagRelationships(Page<NextSupplierAlpha> nextSupplierAlphas);
}
