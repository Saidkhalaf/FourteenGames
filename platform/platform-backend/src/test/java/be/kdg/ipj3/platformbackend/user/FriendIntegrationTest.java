package be.kdg.ipj3.platformbackend.user;

import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.api.dtos.UpdateUserProfileRequestDto;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FriendIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PlatformUser user;
    private PlatformUser friend1;
    private PlatformUser friend2;

    @BeforeEach
    void setUp() {
        UUID userId = UUID.fromString("11111111-1111-1111-1234-111111111111");
        UUID friendId = UUID.fromString("11111111-1111-1111-aaaa-111111111111");
        UUID friendId2 = UUID.fromString("11111111-1111-1111-aabb-111111111111");
        user = new PlatformUser(new UserId(userId), "test-host","",new ArrayList<>(),"","" ,0.0,new ArrayList<>());
        friend1 = new PlatformUser(new UserId(friendId), "test-user-1","",new ArrayList<>(),"","",0.0,new ArrayList<>());
        friend2 = new PlatformUser(new UserId(friendId2), "test-user-2","",new ArrayList<>(), "","",0.0,new ArrayList<>());
    }

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor authJwt(UUID id, String givenName, String familyName, String email) {
        return jwt().jwt(jwt -> jwt
                .subject(id.toString())
                .claim(StandardClaimNames.GIVEN_NAME, givenName)
                .claim(StandardClaimNames.FAMILY_NAME, familyName)
                .claim(StandardClaimNames.EMAIL, email)
        );
    }

    @Nested
    class GetFriendListFlows {
        @Test
        void getFriendList_should_return_200_when_authenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(get("/api/user/friends")
                            .with(authJwt(user.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void getFriendList_should_return_401_when_not_authenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(get("/api/user/friends"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class GetFriendRequestListFlows {
        @Test
        void getFriendRequestList_should_return_200_when_authenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(get("/api/user/friends/requests")
                            .with(authJwt(user.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void getFriendRequestList_should_return_401_when_not_authenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(get("/api/user/friends/requests"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class AddFriendRequestFlows {
        @Test
        void addFriendRequest_should_return_200_when_authenticated_with_valid_ids() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(post("/api/user/friends/" + friend1.getUserName())
                            .with(authJwt(user.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void addFriendRequest_should_return_401_when_not_authenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(patch("/api/user/friends/" + friend1.getUserName()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void addFriendRequest_should_return_404_with_invalid_user_id() throws Exception {
            //Arrange
            UUID invalidUserId = UUID.randomUUID();
            //Act
            //Assert
            mockMvc.perform(post("/api/user/friends/" + friend1.getUserName())
                            .with(authJwt(invalidUserId, "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void addFriendRequest_should_return_404_with_invalid_friend_id() throws Exception {
            //Arrange
            String nonExistantUserName = "invalid";

            //Act
            //Assert
            mockMvc.perform(post("/api/user/friends/" + nonExistantUserName)
                            .with(authJwt(user.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void addFriendRequest_shouldReturn409_WhenAlreadyExists() throws Exception {
            //Arrange
            //Act
            //Assert
            //Check on conflict
            mockMvc.perform(post("/api/user/friends/" + friend2.getUserName())
                            .with(authJwt(user.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict());
        }

        @Test
        void addFriendRequest_from_users_in_both_directions_should_return_200() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(post("/api/user/friends/" + user.getUserName())
                            .with(authJwt(friend2.getUserId().id(), "test_user_2", "user2", "test_user_2@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class AcceptFriendRequestFlows {
        @Test
        void accept_friend_request_should_return_200_when_open_and_authenticated() throws Exception {
            //Arrange
            mockMvc.perform(post("/api/user/friends/" + friend2.getUserName())
                            .with(authJwt(user.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            //Act
            //Assert
            mockMvc.perform(patch("/api/user/friends/" + user.getUserName() + "/accept")
                            .with(authJwt(friend2.getUserId().id(), "friend_user", "friend", "friend_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void accept_friend_request_should_return_404_when_none_open_and_authenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(patch("/api/user/friends/" + friend1.getUserName() + "/accept")
                            .with(authJwt(user.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void accept_friend_request_should_return_401_when_not_authenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(patch("/api/user/friends/" + friend1.getUserName() + "/accept"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class DenyFriendRequestFlows {

        @Test
        void deny_friend_request_should_return_200_when_open_and_authenticated() throws Exception {
            //Arrange
            mockMvc.perform(post("/api/user/friends/" + friend1.getUserName())
                            .with(authJwt(user.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            //Act
            //Assert
            mockMvc.perform(patch("/api/user/friends/" + user.getUserName() + "/deny")
                            .with(authJwt(friend1.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void deny_friend_request_should_return_404_when_none_open_and_authenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(patch("/api/user/friends/" + friend1.getUserId().id() + "/deny")
                            .with(authJwt(user.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void deny_friend_request_should_return_401_when_not_authenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(patch("/api/user/friends/" + friend1.getUserId().id() + "/deny"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class RemoveFriendFlows {
        @Test
        void removeFriend_should_return_404_when_user_doesnt_exist() throws Exception {
            //Arrange
            UUID invalidUserId = UUID.randomUUID();
            //Act
            //Assert
            mockMvc.perform(delete("/api/user/friends/" + friend2.getUserId().id())
                            .with(authJwt(invalidUserId, "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void removeFriend_should_return_404_when_non_friend() throws Exception {
            //Arrange
            UUID nonFriendId = UUID.fromString("11111111-1111-1111-aaad-111111111111");
            //Act
            //Assert
            mockMvc.perform(delete("/api/user/friends/" + nonFriendId)
                            .with(authJwt(user.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void removeFriend_should_return_401_when_not_authenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(delete("/api/user/friends/" + friend1.getUserId().id()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class RetrieveBasicUserInfo {
        @Test
        void getUserData_should_return_200_when_fired_from_a_registered_user() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(get("/api/user")
                            .with(authJwt(user.getUserId().id(), "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void getUserData_should_return_404_when_not_found() throws Exception {
            //Arrange
            UUID invalidUserId = UUID.fromString("99999999-9999-9999-9999-99999999");
            //Act
            //Assert
            mockMvc.perform(get("/api/user")
                            .with(authJwt(invalidUserId, "test_user", "user", "test_user@test.be"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void getUserData_should_return_401_when_not_authenticated() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(get("/api/user"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class UpdateUserProfileDataFlows {
        @Test
        void updateUserData_shouldReturn200_whenValidBodyIsSent() throws Exception {
            //Arrange
            UpdateUserProfileRequestDto requestData = new UpdateUserProfileRequestDto(
                    "My bio",
                    "https://example.com/profile.jpg",
                    "https://example.com/banner.jpg"
            );
            //Act
            //Assert
            mockMvc.perform(patch("/api/user")
                            .with(authJwt(
                                    user.getUserId().id(),
                                    "test_user",
                                    "user",
                                    "test_user@test.be"
                            ))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestData)))
                    .andExpect(status().isOk());
        }

        @Test
        void updateUserData_shouldReturn400_whenBodyIsEmpty() throws Exception {
            //Arrange
            //Act
            //Assert
            mockMvc.perform(patch("/api/user")
                            .with(authJwt(
                                    user.getUserId().id(),
                                    "test_user",
                                    "user",
                                    "test_user@test.be"
                            ))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void updateUserData_shouldReturn401_whenNotAuthenticated() throws Exception {
            //Arrange
            UpdateUserProfileRequestDto requestData = new UpdateUserProfileRequestDto(
                    "My bio",
                    "https://example.com/profile.jpg",
                    "https://example.com/banner.jpg"
            );
            //Act
            //Assert
            mockMvc.perform(patch("/api/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestData))
                    )
                    .andExpect(status().isUnauthorized());
        }


    }
}




