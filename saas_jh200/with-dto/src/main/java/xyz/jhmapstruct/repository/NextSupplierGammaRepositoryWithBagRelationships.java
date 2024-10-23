package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.NextSupplierGamma;

public interface NextSupplierGammaRepositoryWithBagRelationships {
    Optional<NextSupplierGamma> fetchBagRelationships(Optional<NextSupplierGamma> nextSupplierGamma);

    List<NextSupplierGamma> fetchBagRelationships(List<NextSupplierGamma> nextSupplierGammas);

    Page<NextSupplierGamma> fetchBagRelationships(Page<NextSupplierGamma> nextSupplierGammas);
}
