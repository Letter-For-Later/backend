package org.example.letter.domain.eliceAi.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequest {

    private String model;
    private String sess_id;
    private List<Message> messages;

}
