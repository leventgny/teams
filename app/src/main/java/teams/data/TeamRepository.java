package teams.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import teams.model.Team;

import java.util.Optional;

public interface TeamRepository extends PagingAndSortingRepository<Team, Long> {

    Optional<Team> findByName(String name);

}
