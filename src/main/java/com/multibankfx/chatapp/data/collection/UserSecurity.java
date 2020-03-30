package com.multibankfx.chatapp.data.collection;

import com.multibankfx.chatapp.data.enumarated.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Data
@Document(collection = "user_security")
public class UserSecurity extends BaseCollection implements Serializable {

    private String userId;

    private String password;

    private Status status;
}
