package org.example.storage.integration.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.events.EventTopics;
import org.example.storage.integration.outbox.OutboxEventJpaEntity;
import org.example.storage.integration.outbox.OutboxEventRepository;
import org.example.storage.integration.outbox.OutboxStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxKafkaPublisher {
    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelayString = "${app.outbox.publish-delay-ms:5000}")
    @Transactional
    public void publishNewEvents() {
        outboxEventRepository.findTop20ByStatusOrderByCreatedAtAsc(OutboxStatus.NEW)
                .forEach(this::publish);
    }

    private void publish(OutboxEventJpaEntity event) {
        try {
            kafkaTemplate.send(EventTopics.STORAGE_EVENTS, event.getAggregateId().toString(), event.getPayload()).get();
            event.setStatus(OutboxStatus.PUBLISHED);
            event.setPublishedAt(Instant.now());
            event.setErrorMessage(null);
            log.info("Published storage event type={} aggregateId={} traceId={}",
                    event.getEventType(), event.getAggregateId(), event.getTraceId());
        } catch (Exception exception) {
            event.setAttempts(event.getAttempts() + 1);
            event.setStatus(event.getAttempts() >= 5 ? OutboxStatus.FAILED : OutboxStatus.NEW);
            event.setErrorMessage(exception.getMessage());
            log.warn("Failed to publish storage event type={} aggregateId={} traceId={} attempt={}",
                    event.getEventType(), event.getAggregateId(), event.getTraceId(), event.getAttempts(), exception);
        }
    }
}
