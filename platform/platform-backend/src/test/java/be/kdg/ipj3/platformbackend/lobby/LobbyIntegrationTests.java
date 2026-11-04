package be.kdg.ipj3.platformbackend.lobby;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LobbyIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private static final UUID GAME_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID GAME_SESSION_ID =
            UUID.fromString("99999999-9999-9999-9999-999999999999");

    private static final UUID USER_ID =
            UUID.fromString("11111111-1111-1111-aaaa-111111111111");

    private static final UUID LOBBY_ID =
            UUID.fromString("11111111-ffff-1111-1111-111111111111");

    private static final UUID FULL_LOBBY_ID =
            UUID.fromString("11111111-ffee-1111-1111-111111111111");

    private static final UUID NON_EXISTING_LOBBY_ID =
            UUID.fromString("11111111-ffff-1111-1111-999999999999");

    @Test
    void findAllLobbies_ShouldReturn200() throws Exception {
        //Arrange
        //Act
        //Assert
        mockMvc.perform(get("/api/lobby")
                        .with(jwt()
                                .jwt(jwt -> jwt
                                        .subject(USER_ID.toString())
                                        .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                                        .claim(StandardClaimNames.FAMILY_NAME, "user")
                                        .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                                )
                        )
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void findAllLobbies_ShouldReturn401_when_not_authenticated() throws Exception {
        //Arrange
        //Act
        //Assert
        mockMvc.perform(get("/api/lobby")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Nested
    class FindLobby {

        @Test
        void findLobby_ShouldReturn200_WhenAuthenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(get("/api/lobby/{id}", LOBBY_ID)
                            .with(jwt()
                                    .jwt(jwt -> jwt
                                            .subject(USER_ID.toString())
                                            .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                                            .claim(StandardClaimNames.FAMILY_NAME, "user")
                                            .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                                    )
                            )
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists());
        }

        @Test
        void findLobby_ShouldReturn404_WhenLobbyDoesNotExist() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(get("/api/lobby/{id}", NON_EXISTING_LOBBY_ID)
                            .with(jwt()
                                    .jwt(jwt -> jwt
                                            .subject(USER_ID.toString())
                                            .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                                            .claim(StandardClaimNames.FAMILY_NAME, "user")
                                            .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                                    )
                            )
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void findLobby_ShouldReturn401_WhenNotAuthenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(get("/api/lobby/{id}", LOBBY_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class CreateLobby {

        @Test
        void createLobby_ShouldReturn200_WhenAuthenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(post("/api/lobby")
                            .with(jwt()
                                    .jwt(jwt -> jwt
                                            .subject(USER_ID.toString())
                                            .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                                            .claim(StandardClaimNames.FAMILY_NAME, "user")
                                            .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                                    )
                            )
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "gameId": "%s"
                                    }
                                    """.formatted(GAME_ID))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void createLobby_ShouldReturn401_WhenNotAuthenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(post("/api/lobby")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "gameId": "%s"
                                    }
                                    """.formatted(GAME_ID))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class JoinLobby {

        @Test
        void joinLobby_ShouldReturn200_WhenAuthenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(patch("/api/lobby/{id}", LOBBY_ID)
                            .with(jwt()
                                    .jwt(jwt -> jwt
                                            .subject(USER_ID.toString())
                                            .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                                            .claim(StandardClaimNames.FAMILY_NAME, "user")
                                            .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                                    )
                            )
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void joinLobby_ShouldReturn409_WhenAlreadyInLobby() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(patch("/api/lobby/{id}", FULL_LOBBY_ID)
                            .with(jwt()
                                    .jwt(jwt -> jwt
                                            .subject(USER_ID.toString())
                                            .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                                            .claim(StandardClaimNames.FAMILY_NAME, "user")
                                            .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                                    )
                            )
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());
        }

        @Test
        void joinLobby_ShouldReturn404_WhenLobbyDoesNotExist() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(patch("/api/lobby/{id}", NON_EXISTING_LOBBY_ID)
                            .with(jwt()
                                    .jwt(jwt -> jwt
                                            .subject(USER_ID.toString())
                                            .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                                            .claim(StandardClaimNames.FAMILY_NAME, "user")
                                            .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                                    )
                            )
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void joinLobby_ShouldReturn401_WhenNotAuthenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(patch("/api/lobby/{id}", LOBBY_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class LeaveLobby {

        @Test
        void leaveLobby_ShouldReturn200_WhenAuthenticated() throws Exception {
            //Arrange
            mockMvc.perform(patch("/api/lobby/{id}", LOBBY_ID)
                            .with(jwt()
                                    .jwt(jwt -> jwt
                                            .subject(USER_ID.toString())
                                            .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                                            .claim(StandardClaimNames.FAMILY_NAME, "user")
                                            .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                                    )
                            )
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            //Act
            //Assert
            mockMvc.perform(delete("/api/lobby/{id}", LOBBY_ID)
                            .with(jwt()
                                    .jwt(jwt -> jwt
                                            .subject(USER_ID.toString())
                                            .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                                            .claim(StandardClaimNames.FAMILY_NAME, "user")
                                            .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                                    )
                            )
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void leaveLobby_ShouldReturn401_WhenNotAuthenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(delete("/api/lobby/{id}", LOBBY_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class FindLobbyByGameId {

        @Test
        void findLobbyByGameId_ShouldReturn200_WhenAuthenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(get("/api/lobby/byGame/{gameSessionId}", GAME_SESSION_ID)
                            .with(jwt()
                                    .jwt(jwt -> jwt
                                            .subject(USER_ID.toString())
                                            .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                                            .claim(StandardClaimNames.FAMILY_NAME, "user")
                                            .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                                    )
                            )
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.gameId").value(GAME_ID.toString()));
        }

        @Test
        void findLobbyByGameId_ShouldReturn404_WhenLobbyDoesNotExist() throws Exception {
            //Arrange
            UUID nonExistingGameId = UUID.randomUUID();
            //Act
            //Assert
            mockMvc.perform(get("/api/lobby/game/{gameId}", nonExistingGameId)
                            .with(jwt()
                                    .jwt(jwt -> jwt
                                            .subject(USER_ID.toString())
                                            .claim(StandardClaimNames.GIVEN_NAME, "test_user")
                                            .claim(StandardClaimNames.FAMILY_NAME, "user")
                                            .claim(StandardClaimNames.EMAIL, "test_user@test.be")
                                    )
                            )
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void findLobbyByGameId_ShouldReturn401_WhenNotAuthenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(get("/api/lobby/game/{gameId}", GAME_ID)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

}
