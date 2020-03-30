package com.multibankfx.chatapp.data.collection;

import com.multibankfx.chatapp.data.enumarated.SessionStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@Document(collection = "user_session")
public class UserSession extends BaseCollection {

    private String userId;

    private String sid;

    private SessionStatus sessionStatus;

}
