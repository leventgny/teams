package teams.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teams.model.Player;
import teams.service.PlayerService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayersController {

    private final PlayerService playerService;

    @GetMapping
    public ResponseEntity getPlayer(
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false) Long citizenNumber
    ) {
        if (citizenNumber != null) {
            Optional<Player> player = playerService.getByCitizenNumber(citizenNumber);
            if (player.isPresent()) {
                return ResponseEntity.ok(player.get());
            } return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
            playerService.getAllPlayers(page, size).getContent()
        );
    }

    @PutMapping
    public ResponseEntity<Player> save(@RequestBody Player player) {
        if(player.getCitizenNumber() == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<Player> persisted = playerService.findById(player.getCitizenNumber());

        if (!persisted.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        if(player.getBirth() == null) {
            player.setBirth(persisted.get().getBirth());
        }

        if(player.getName() == null) {
            player.setName(persisted.get().getName());
        }

        player.setTeam(persisted.get().getTeam());

        return ResponseEntity.ok(
            playerService.save(player)
        );
    }
}
