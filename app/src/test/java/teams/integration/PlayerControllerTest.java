package teams.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
@AutoConfigureMockMvc
public class PlayerControllerTest {

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
    public void testGetPlayer() throws Exception {

        mvc.perform(get("/players"))
            .andExpect(status().isOk()) // get all players fine
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)));

        mvc.perform(get("/players?citizenNumber=1234"))
            .andExpect(status().isOk()) // get Player 1 fine
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                   .andExpect(jsonPath("$.name", is("Player 1")));

        mvc.perform(get("/players?citizenNumber=10101"))
            .andExpect(status().isNotFound()); // random not found
    }

    @Test
    public void testEditPlayer() throws Exception {

        Player edited = Player.builder().name("Player Edited").build();
        mvc.perform(put("/players").contentType(MediaType.APPLICATION_JSON).content(
            mapper.writeValueAsString(edited)
        )).andExpect(status().isNotFound());

        edited.setCitizenNumber(101011L);
        mvc.perform(put("/players").contentType(MediaType.APPLICATION_JSON).content(
            mapper.writeValueAsString(edited)
        )).andExpect(status().isNotFound());

        edited.setCitizenNumber(1234L);
        mvc.perform(put("/players").contentType(MediaType.APPLICATION_JSON).content(
            mapper.writeValueAsString(edited)
        )).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name", is("Player Edited")));
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
