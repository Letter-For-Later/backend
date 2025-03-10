package org.example.letter.domain.eliceAi.dto;

import java.awt.Choice;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponse {

    private String id;
    private String object;
    private long created;
    private Choice[] choices;

    public static class Choice {
        private Message message;
        // 추가 필드가 필요하면 여기서 정의
        public Message getMessage() {
            return message;
        }
        public void setMessage(Message message) {
            this.message = message;
        }
    }


}
