package be.kdg.gobackend.game.api.dto;

import java.util.UUID;

//boardSize here must match the configurableSettings.json's since its send in from UI.
public record NewMatchDto(UUID player1Id, UUID player2Id, GameSettingsDto settings) {}
