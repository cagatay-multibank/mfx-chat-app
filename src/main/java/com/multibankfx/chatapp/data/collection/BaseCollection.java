package com.multibankfx.chatapp.data.collection;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Data
public abstract class BaseCollection {
    @Id
    private String id;

    private Long createdTime;

    private Long modifiedTime;

}
