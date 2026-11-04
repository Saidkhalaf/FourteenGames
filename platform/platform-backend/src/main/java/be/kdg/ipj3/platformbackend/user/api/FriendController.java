package be.kdg.ipj3.platformbackend.user.api;

import be.kdg.ipj3.platformbackend.shared.domain.UserId;
import be.kdg.ipj3.platformbackend.user.api.dtos.FriendDto;
import be.kdg.ipj3.platformbackend.user.api.dtos.FriendListDto;
import be.kdg.ipj3.platformbackend.user.api.dtos.FriendRecommendationListDto;
import be.kdg.ipj3.platformbackend.user.api.dtos.FriendRequestListDto;
import be.kdg.ipj3.platformbackend.user.application.FriendService;
import be.kdg.ipj3.platformbackend.user.application.UserService;
import be.kdg.ipj3.platformbackend.user.domain.PlatformUser;
import be.kdg.ipj3.platformbackend.user.domain.PlatformFriendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/api/user/friends")
public class FriendController {

    private final FriendService friendService;
    private final UserService userService;

    public FriendController(FriendService friendService, UserService userService) {
        this.friendService = friendService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<FriendListDto> findAllFriends(@AuthenticationPrincipal Jwt token) {
        UserId userId = UserId.fromToken(token);
        PlatformUser user = userService.findUserById(userId);
        List<PlatformUser> fullFriends =  friendService.findAllFriendsOfUserWithId(userId);
        return ResponseEntity.ok(FriendListDto.from(user, fullFriends));
    }
    @PostMapping("/{friendUserName}")
    public ResponseEntity<FriendListDto> addFriendRequest(@PathVariable final String friendUserName, @AuthenticationPrincipal Jwt token) {
        UserId userId = UserId.fromToken(token);
        PlatformUser friend = userService.findUserByUserName(friendUserName);
        friendService.addFriendRequest(userId, new UserId(friend.getUserId().id()));

        PlatformUser user = userService.findUserById(userId);
        List<PlatformUser> fullFriends = friendService.findAllFriendsOfUserWithId(userId);

        return ResponseEntity.ok(FriendListDto.from(user, fullFriends));
    }
    @DeleteMapping("/{friendUserName}")
    public ResponseEntity<FriendListDto> removeFriend(@PathVariable final String friendUserName, @AuthenticationPrincipal Jwt token) {
        UserId userId = UserId.fromToken(token);
        PlatformUser friend = userService.findUserByUserName(friendUserName);
        friendService.removeFriendFromFriendList(userId, friend.getUserId());

        PlatformUser user = userService.findUserById(userId);
        List<PlatformUser> fullFriends = friendService.findAllFriendsOfUserWithId(userId);

        return ResponseEntity.ok(FriendListDto.from(user, fullFriends));
    }

    @PatchMapping("/{friendUserName}/accept")
    public ResponseEntity<FriendDto> acceptFriendRequest(@PathVariable final String friendUserName, @AuthenticationPrincipal Jwt token) {
        UserId userId = UserId.fromToken(token);
        PlatformUser friend = userService.findUserByUserName(friendUserName);
        PlatformFriendRequest newFriend = friendService.acceptFriendRequest(userId, friend.getUserId().id());

        UUID otherUserId = newFriend.getReceiver().id().equals(userId.id())
                ? newFriend.getSender().id()
                : newFriend.getReceiver().id();
        PlatformUser friendUser = userService.findUserById(new UserId(otherUserId));

        return ResponseEntity.ok(FriendDto.from(friendUser));
    }

    @PatchMapping("/{friendUserName}/deny")
    public ResponseEntity<String> denyFriendRequest(@PathVariable final String friendUserName, @AuthenticationPrincipal Jwt token) {
        UserId userId = UserId.fromToken(token);
        PlatformUser friend = userService.findUserByUserName(friendUserName);
        friendService.denyFriendRequest(userId, friend.getUserId().id());
        return ResponseEntity.ok("Friendrequest succesfully denied.");
    }

    @GetMapping("/requests")
    public ResponseEntity<FriendRequestListDto> findAllFriendRequests(@AuthenticationPrincipal Jwt token) {
        UserId userId = UserId.fromToken(token);
        List<PlatformFriendRequest> friendRequests = friendService.findFriendRequestsForUser(userId);
        List<UserId> requestingUserIds = friendRequests.stream()
                .map(request -> request.getRequestingUserId(userId))
                .toList();
        List<PlatformUser> requestingUsers = userService.findUserListByIdList(requestingUserIds);
        return ResponseEntity.ok(FriendRequestListDto.from(requestingUsers));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<FriendRecommendationListDto> getFriendRecommendations
            (@AuthenticationPrincipal Jwt token,
             @RequestParam(name = "nameQuery", required = false) String nameQuery
             ){
        UserId userId = UserId.fromToken(token);
        log.info("Getting friend recomendations for user with id {} ", userId.id());
        List<PlatformUser> friendRecommendations = friendService.getFriendRecommendations(userId.id(), nameQuery);
        return ResponseEntity.ok(FriendRecommendationListDto.from(friendRecommendations));
    }

}
