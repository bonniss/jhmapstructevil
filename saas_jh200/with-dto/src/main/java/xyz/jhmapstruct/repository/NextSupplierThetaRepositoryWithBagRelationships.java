package xyz.jhmapstruct.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import xyz.jhmapstruct.domain.NextSupplierTheta;

public interface NextSupplierThetaRepositoryWithBagRelationships {
    Optional<NextSupplierTheta> fetchBagRelationships(Optional<NextSupplierTheta> nextSupplierTheta);

    List<NextSupplierTheta> fetchBagRelationships(List<NextSupplierTheta> nextSupplierThetas);

    Page<NextSupplierTheta> fetchBagRelationships(Page<NextSupplierTheta> nextSupplierThetas);
}
