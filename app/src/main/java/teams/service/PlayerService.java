package teams.service;

import org.springframework.data.domain.Page;
import teams.model.Player;

import java.util.Optional;

public interface PlayerService {

    Player save(Player player);

    Optional<Player> findById(Long citizenNumber);

    Optional<Player> getByCitizenNumber(Long id);

    Page<Player> getAllPlayers(int page, int size);
}
