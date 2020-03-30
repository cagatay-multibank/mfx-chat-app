package com.multibankfx.chatapp.data.collection;

import com.multibankfx.chatapp.data.enumarated.UserStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Data
@Document(collection = "user")
public class User extends BaseCollection implements Serializable {

    private String username;

    private String name;

    private String surname;

    private UserStatus userStatus;
}
