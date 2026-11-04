package be.kdg.ipj3.platformbackend.games;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GameIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class GameListIntegrationFlows {
        @Test
        public void getGames_should_return_200() throws Exception {
            mockMvc.perform(get("/api/games")).andExpect(status().isOk());
        }

    }
    @Nested
    class GameByIdIntegrationFlows{
        @Test
        public void getGameById_should_return_200_when_fired_with_valid_id() throws Exception {
            UUID gameId = UUID.fromString("11111111-1111-1111-1111-111111111111");
            mockMvc.perform(get("/api/games/{id}", gameId))
                    .andExpect(status().isOk());
        }

        @Test
        public void getGameById_should_return_404_when_fired_with_invalid_id() throws Exception {
            UUID gameId = UUID.fromString("22222222-2222-2222-2222-222222222222");
            mockMvc.perform(get("/api/games/{id}", gameId))
                    .andExpect(status().isNotFound());
        }
    }
}
