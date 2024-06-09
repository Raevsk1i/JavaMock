package com.example.newMock.controller;

import com.example.newMock.model.RequestDTO;
import com.example.newMock.model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Random;

@RestController
@Slf4j
public class MainController {

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO) {
        try {
            ResponseDTO responseDTO = new ResponseDTO();
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLimit;

            switch (firstDigit) {
                case '8' -> {
                    maxLimit = new BigDecimal(2000);
                    responseDTO.setCurrency("USD");
                }
                case '9' -> {
                    maxLimit = new BigDecimal(1000);
                    responseDTO.setCurrency("EUR");
                }
                default -> {
                    maxLimit = BigDecimal.valueOf(10000);
                    responseDTO.setCurrency("RUB");
                }
            }

            responseDTO.setRqUID(requestDTO.getRqUID());
            responseDTO.setClientId(requestDTO.getClientId());
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setMaxLimit(maxLimit);
            responseDTO.setBalance(new BigDecimal(new Random().nextInt(maxLimit.intValue())));

            log.info("******* REQUEST *******{}", mapper.writeValueAsString(requestDTO));
            log.info("******* RESPONSE *******{}", mapper.writeValueAsString(responseDTO));
            return responseDTO;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
