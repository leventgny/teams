package teams.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import teams.App;
import teams.data.PlayerRepository;
import teams.data.TeamRepository;
import teams.model.Player;
import teams.model.Team;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
public class TeamControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private static final Team team = getSampleTeam();

    @After
    public void resetDb() {
        teamRepository.deleteAll();
        playerRepository.deleteAll();
    }

    @Before
    public void init() throws Exception {
        mvc.perform(post("/teams").contentType(MediaType.APPLICATION_JSON).content(
                mapper.writeValueAsString(team)
        )); // persist TeamA and its players
    }

    @Test
    public void testCreateTeam() throws Exception {

        // expect team valid
        Iterable<Team> foundTeams = teamRepository.findAll();
        assertThat(foundTeams).extracting(Team::getName).containsOnly("TeamA");

        // expect players are valid
        Iterable<Player> foundPlayers = playerRepository.findAll();
        assertThat(foundPlayers).extracting(Player::getName).containsOnly("Player 1", "Player 2");

        // redundant team name needs to fail
        mvc.perform(post("/teams").contentType(MediaType.APPLICATION_JSON).content(
            mapper.writeValueAsString(team)
        )).andExpect(status().isBadRequest());
    }

    @Test
    public void testEditTeam() throws Exception {

        Team edited = Team.builder().name("Team Edited").build();
        mvc.perform(put("/teams").contentType(MediaType.APPLICATION_JSON).content(
            mapper.writeValueAsString(edited)
        )).andExpect(status().isNotFound()); // edit not found

        edited.setId(teamRepository.findByName("TeamA").get().getId());
        mvc.perform(put("/teams").contentType(MediaType.APPLICATION_JSON).content(
            mapper.writeValueAsString(edited)
        )).andExpect(status().isOk()) // edited and expected changes
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Team Edited")));
    }

   @Test
   public void testGetTeam() throws Exception {

       mvc.perform(get("/teams"))
           .andExpect(status().isOk()) // get all teams fine
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)));

       // expect TeamA loads
       mvc.perform(get("/teams?name=TeamA"))
           .andExpect(status().isOk()) // get TeamA fine
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name", is("TeamA")))
                        .andExpect(jsonPath("$.players", hasSize(2)));

       mvc.perform(get("/teams?id=1234"))
           .andExpect(status().isNotFound());

       mvc.perform(get("/teams?name=NotExisting"))
           .andExpect(status().isNotFound());
   }

   @Test
   public void testDeleteTeam() throws Exception {

        mvc.perform(delete("/teams"))
            .andExpect(status().isBadRequest());

        mvc.perform(delete("/teams?id=1234"))
            .andExpect(status().isNotFound());

        Team team = teamRepository.findByName("TeamA").get();
        mvc.perform(delete(("/teams?id=" + team.getId())))
            .andExpect(status().isOk());

       assertThat(teamRepository.findAll()).isEmpty();
   }

    @Test
    public void testRemovePlayersFromTeam() throws Exception {
        Team team = teamRepository.findByName("TeamA").get();

        mvc.perform(delete("/teams/removePlayer"))
            .andExpect(status().isBadRequest());

        mvc.perform(delete("/teams/removePlayer?teamId=1234"))
            .andExpect(status().isBadRequest());

        mvc.perform(delete("/teams/removePlayer?citizenNumber=10101"))
            .andExpect(status().isBadRequest());

        mvc.perform(delete("/teams/removePlayer?citizenNumber=10101&teamId=" + team.getId()))
            .andExpect(status().isNotFound());

        mvc.perform(delete("/teams/removePlayer?citizenNumber=1234&teamId=10101"))
            .andExpect(status().isNotFound());

        mvc.perform(delete("/teams/removePlayer?citizenNumber=1234&teamId=" + team.getId()))
            .andExpect(status().isOk());

    }



    private static Team getSampleTeam() {
        Team team = Team.builder().name("TeamA").build();
        team.getPlayers().addAll(
                Arrays.asList(
                        Player.builder().name("Player 1").birth(LocalDate.of(
                                1990, Month.JANUARY, 10
                        )).citizenNumber(1234L).build(),
                        Player.builder().name("Player 2").birth(LocalDate.of(
                                1990, Month.JANUARY, 10
                        )).citizenNumber(12345L).build()
                )
        );

        return team;
    }
}
