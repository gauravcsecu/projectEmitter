package com.example.hellospring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
public class HomeController {
    @Autowired
    EmitterMap emitterMap;

    @Autowired
    EmitterDataSendService emitterDataSendService;
    @GetMapping(path = "/consumer/{requestId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<ResponseEntity<?>>> consumer(
            @PathVariable String requestId
    ) {
        List<ResponseEntity<?>> latestMessage = emitterDataSendService.getLatestEmitterData(requestId);

        return Flux.<List<ResponseEntity<?>>>create(emitter -> {
            var collection = emitterMap.values(requestId);
            collection.add(emitter);
            emitterMap.put(requestId, collection);

            // Emit the latest message to the new subscriber
            emitter.next(latestMessage);

            emitter.onDispose(() -> emitterMap.remove(requestId,emitter));
        }).timeout(Duration.ofMinutes(12L));
    }

    @GetMapping("/producer/{requestId}/{message}")
    public String produce(@PathVariable String requestId, @PathVariable String message) {
        emitterDataSendService.updateLatestMessageMap(requestId,message);
        emitterDataSendService.sendEmitterData(requestId,message);
        return "RequestId " + requestId + " message "+ message;
    }

    @GetMapping(path = "/consumer/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<ResponseEntity<?>>> consumerAll(
            @PathVariable String requestId
    ) {
        List<ResponseEntity<?>> latestMessage = emitterDataSendService.getLatestEmitterData(requestId);

        return Flux.<List<ResponseEntity<?>>>create(emitter -> {
            var collection = emitterMap.values(requestId);
            collection.add(emitter);
            emitterMap.put(requestId, collection);

            // Emit the latest message to the new subscriber
            emitter.next(latestMessage);

            emitter.onDispose(() -> emitterMap.remove(requestId,emitter));
        }).timeout(Duration.ofMinutes(12L));
    }

}
