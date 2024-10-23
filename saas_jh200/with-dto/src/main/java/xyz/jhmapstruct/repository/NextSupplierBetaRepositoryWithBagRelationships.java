package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.NextSupplierBeta;

public interface NextSupplierBetaRepositoryWithBagRelationships {
    Optional<NextSupplierBeta> fetchBagRelationships(Optional<NextSupplierBeta> nextSupplierBeta);

    List<NextSupplierBeta> fetchBagRelationships(List<NextSupplierBeta> nextSupplierBetas);

    Page<NextSupplierBeta> fetchBagRelationships(Page<NextSupplierBeta> nextSupplierBetas);
}
