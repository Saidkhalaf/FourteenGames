package be.kdg.gobackend.analytics.infrastructure.messages;

public interface EventMessage {
    String event_type();
    String timestamp();
}
