package teams.dto;

import lombok.Builder;
import lombok.Data;
import teams.model.Team;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
public class TeamDTO {

    private String name;
    private Set<PlayerDTO> players;

    public static Optional<TeamDTO> byTeam(Team team) {
        return team == null ? Optional.empty() : Optional.of(
            TeamDTO.builder().name(team.getName()).players(
                team.getPlayers().stream().map(
                    player -> PlayerDTO.builder()
                        .name(player.getName())
                        .age(Period.between(player.getBirth(), LocalDate.now()).getYears())
                        .build()
                    )
                .collect(Collectors.toSet())
            ).build()
        );
    }
}
