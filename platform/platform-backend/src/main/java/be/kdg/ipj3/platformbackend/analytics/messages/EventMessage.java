package be.kdg.ipj3.platformbackend.analytics.messages;

public interface EventMessage {
    String event_type();
    String timestamp();
}
