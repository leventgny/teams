package teams.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import teams.data.TeamRepository;
import teams.model.Team;
import teams.service.TeamService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public boolean deleteTeam(Long id) {
        try {
            teamRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Optional<Team> getTeamByName(String name) {
        return teamRepository.findByName(name);
    }

    @Override
    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    public Team save(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public Page<Team> getAllTeams(int page, int size) {
        return teamRepository.findAll(Pageable.ofSize(size).withPage(page));
    }
}
