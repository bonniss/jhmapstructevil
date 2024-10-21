package ai.realworld.repository;

import ai.realworld.domain.AlLexFerg;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface AlLexFergRepositoryWithBagRelationships {
    Optional<AlLexFerg> fetchBagRelationships(Optional<AlLexFerg> alLexFerg);

    List<AlLexFerg> fetchBagRelationships(List<AlLexFerg> alLexFergs);

    Page<AlLexFerg> fetchBagRelationships(Page<AlLexFerg> alLexFergs);
}
