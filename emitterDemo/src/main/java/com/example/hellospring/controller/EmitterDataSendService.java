package com.example.hellospring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EmitterDataSendService {

    @Autowired
    EmitterMap emitterMap;
    public void sendEmitterData(String requestId, String message) {
        Collection<FluxSink<List<ResponseEntity<?>>>> sinks = emitterMap.values(requestId);
        if (!sinks.isEmpty()) {
            ResponseEntity<?> response = getResponse(requestId, message);
            sinks.forEach(sink -> {
            sink.next(List.of(response));
            });
        }
    }

    public List<ResponseEntity<?>> getLatestEmitterData(String requestId) {
        List<ResponseEntity<?>> latestdata = emitterMap.latestMessageMap.get(requestId);
        return latestdata==null?List.of(getResponse(requestId,"NO MORE HISTORY")):latestdata;
    }

    public void updateLatestMessageMap(String requestId, String message) {
        var listResponse = emitterMap.latestMessageMap.get(requestId);
        if(listResponse==null) {
            listResponse = new CopyOnWriteArrayList<>();
        }
            listResponse.add(getResponse(requestId,message));
            emitterMap.latestMessageMap.put(requestId, listResponse);
    }

    private  ResponseEntity<?> getResponse(String requestId, String message) {
        return new ResponseEntity<>(" requestId "+requestId+" message: "+message,HttpStatus.ACCEPTED);
    }
}
