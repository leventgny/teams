package teams.service;

import org.springframework.data.domain.Page;
import teams.model.Team;

import java.util.Optional;

public interface TeamService {
    Team createTeam(Team team);

    boolean deleteTeam(Long id);

    Optional<Team> getTeamByName(String name);

    Optional<Team> getTeamById(Long id);

    Team save(Team team);

    Page<Team> getAllTeams(int page, int size);
}
