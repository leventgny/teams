package teams.data;


import org.springframework.data.repository.PagingAndSortingRepository;
import teams.model.Player;

public interface PlayerRepository extends PagingAndSortingRepository<Player, Long> {

}
