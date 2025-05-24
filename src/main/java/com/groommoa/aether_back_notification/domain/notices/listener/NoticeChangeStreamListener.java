package com.groommoa.aether_back_notification.domain.notices.listener;

import com.groommoa.aether_back_notification.infrastructure.sse.service.SseService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeChangeStreamListener {

    private final MongoClient mongoClient;
    private final SseService sseService;

    private final String COLLECTION_NAME = "notices";

    @Value("${spring.data.mongodb.database}")
    private String DB_NAME;

    @PostConstruct
    public void listen() {
        Executors.newSingleThreadExecutor().submit(() -> {
            MongoCollection<Document> collection = mongoClient
                    .getDatabase(DB_NAME)
                    .getCollection(COLLECTION_NAME);

            try (MongoCursor<ChangeStreamDocument<Document>> cursor = collection.watch().iterator()) {
                log.info("Notice Change Stream 시작됨");

                while (cursor.hasNext()) {
                    ChangeStreamDocument<Document> change = cursor.next();

                    if ("insert".equals(change.getOperationType().getValue())){
                        Document fullDoc = change.getFullDocument();
                        if (fullDoc == null) continue;

                        String noticeId = fullDoc.getObjectId("_id").toHexString();

                        sseService.sendEventToAllEmitters("notice", noticeId);
                    }
                }
            } catch (Exception e) {
                log.error("Notice Change Stream 오류 발생", e);
            }
        });
    }
}
