package ai.realworld.repository;

import ai.realworld.domain.AlPyuJoker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AlPyuJokerRepositoryWithBagRelationships {
    Optional<AlPyuJoker> fetchBagRelationships(Optional<AlPyuJoker> alPyuJoker);

    List<AlPyuJoker> fetchBagRelationships(List<AlPyuJoker> alPyuJokers);

    Page<AlPyuJoker> fetchBagRelationships(Page<AlPyuJoker> alPyuJokers);
}
