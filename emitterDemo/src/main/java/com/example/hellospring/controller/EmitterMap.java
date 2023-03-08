package com.example.hellospring.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
    public class EmitterMap {

        final ConcurrentHashMap<String, Collection<FluxSink<List<ResponseEntity<?>>>>> sinkMap = new ConcurrentHashMap<>();
        final ConcurrentHashMap<String, List<ResponseEntity<?>>> latestMessageMap = new ConcurrentHashMap<>();
        public void put(String requestId, Collection<FluxSink<List<ResponseEntity<?>>>> collectionEmitter) {
            sinkMap.put(requestId,collectionEmitter);
        }

    public void remove(String requestId, FluxSink<List<ResponseEntity<?>>> emitter) {
        Collection<FluxSink<List<ResponseEntity<?>>>> emitters = sinkMap.get(requestId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                sinkMap.remove(requestId);
            }
        }
    }


    public Collection<FluxSink<List<ResponseEntity<?>>>> values(String requestId) {
            if(sinkMap.get(requestId)==null) {
                Collection<FluxSink<List<ResponseEntity<?>>>> collection = new CopyOnWriteArrayList<>();
                return  collection;
            } else {
                return sinkMap.get(requestId);
            }
    }
}








