package teams.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teams.dto.TeamDTO;
import teams.model.Player;
import teams.model.Team;
import teams.service.PlayerService;
import teams.service.TeamService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamsController {

    private final TeamService teamService;

    private final PlayerService playerService;

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        if(teamService.getTeamByName(team.getName()).isPresent()) {
            return ResponseEntity.badRequest().build();
        } return ResponseEntity.ok(teamService.createTeam(team));
    }

    @PutMapping
    public ResponseEntity<Team> save(@RequestBody Team team) {
        Optional<Team> persisted;
        if(team.getId() != null) {
            persisted = teamService.getTeamById(team.getId());
            if (persisted.isPresent()) {
                team.getPlayers().addAll(persisted.get().getPlayers());
                return ResponseEntity.ok(
                        teamService.save(team)
                );
            }
        } return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity getTeam(
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Long id
    ) {

        if(id != null) {
            Optional<Team> team = teamService.getTeamById(id);
            if (team.isPresent()) {
                return ResponseEntity.ok(
                    TeamDTO.byTeam(team.get())
                );
            } return ResponseEntity.notFound().build();
        }

        if(name != null) {
            Optional<Team> team = teamService.getTeamByName(name);
            if(team.isPresent()) {
                return ResponseEntity.ok(
                    TeamDTO.byTeam(team.get())
                );
            } return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(teamService.getAllTeams(page, size).getContent());
    }

    @DeleteMapping
    public ResponseEntity deleteTeam(@RequestParam Long id) {
        if(teamService.deleteTeam(id)) {
            return ResponseEntity.ok().build();
        } return ResponseEntity.notFound().build();
    }

    @PutMapping("/addPlayers")
    public ResponseEntity<Team> addPlayersToTeam(
            @RequestBody List<Player> players,
            @RequestParam Long teamId
    ) {
        Optional<Team> team = teamService.getTeamById(teamId);

        if (team.isPresent()) {
            Team toPersist = team.get();

            // find existing bad request
            for (Player player : players) {
                for (Player existing : toPersist.getPlayers()) {
                    if (player.getCitizenNumber() == null ||
                            player.getCitizenNumber().longValue() == existing.getCitizenNumber().longValue()) {
                        return ResponseEntity.badRequest().build();
                    }
                }
            }

            toPersist.getPlayers().addAll(players);

            teamService.save(toPersist);

            return ResponseEntity.ok(
                teamService.getTeamById(teamId).get()
            );
        } return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/removePlayer")
    public ResponseEntity<Team> removePlayerFromTeam(
        @RequestParam Long teamId, @RequestParam Long citizenNumber
    ) {
        Optional<Team> team = teamService.getTeamById(teamId);
        Optional<Player> player = playerService.findById(citizenNumber);

        if (team.isPresent() && player.isPresent()) {
            Team toPersist = team.get();

            // remove player by citizenNumber
            toPersist.getPlayers().removeIf(
                p -> p.getCitizenNumber().longValue() == citizenNumber.longValue()
            );

            teamService.save(toPersist);

            return ResponseEntity.ok(toPersist);

        } return ResponseEntity.notFound().build();

    }

}
