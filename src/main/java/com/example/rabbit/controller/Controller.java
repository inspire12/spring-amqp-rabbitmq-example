package com.example.rabbit.controller;


import com.example.rabbit.config.RabbitConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ObjectMapper mapper;


    @PostMapping("/")
    public ResponseEntity<ObjectNode> produceMq (@RequestBody ObjectNode requestBody) {

        rabbitTemplate.convertAndSend(RabbitConfig.fanoutExchangeName, "", requestBody);
        return ResponseEntity.ok().body(requestBody);
    }
}
