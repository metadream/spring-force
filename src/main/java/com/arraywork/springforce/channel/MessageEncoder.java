package com.arraywork.springforce.channel;

import jakarta.websocket.Encoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Web Socket Message Encoder
 *
 * @author AiChen
 * @copyright ArrayWork Inc.
 * @since 2024/07/06
 */
@Slf4j
public class MessageEncoder implements Encoder.Text<Object> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String encode(Object message) {
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}