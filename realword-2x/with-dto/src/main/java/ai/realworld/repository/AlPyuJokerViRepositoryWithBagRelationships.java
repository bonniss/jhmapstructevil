package ai.realworld.repository;

import ai.realworld.domain.AlPyuJokerVi;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AlPyuJokerViRepositoryWithBagRelationships {
    Optional<AlPyuJokerVi> fetchBagRelationships(Optional<AlPyuJokerVi> alPyuJokerVi);

    List<AlPyuJokerVi> fetchBagRelationships(List<AlPyuJokerVi> alPyuJokerVis);

    Page<AlPyuJokerVi> fetchBagRelationships(Page<AlPyuJokerVi> alPyuJokerVis);
}
