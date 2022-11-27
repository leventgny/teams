package teams.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import teams.data.PlayerRepository;
import teams.model.Player;
import teams.service.PlayerService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    @Override
    public Player save(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Optional<Player> findById(Long citizenNumber) {
        return playerRepository.findById(citizenNumber);
    }

    @Override
    public Optional<Player> getByCitizenNumber(Long id) {
        return playerRepository.findById(id);
    }

    @Override
    public Page<Player> getAllPlayers(int page, int size) {
        return playerRepository.findAll(Pageable.ofSize(size).withPage(page));
    }
}
