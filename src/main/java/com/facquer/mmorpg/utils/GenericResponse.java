package com.facquer.mmorpg.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Component
public class GenericResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 4189541161407214791L;
    private StateEnum state;
    private String message;
    private String messageError;
    private T object;

    public void saveMessage(List<String> messageList){
        if(messageList.size()>1){
            message= messageList.get(0);
            messageError= messageList.get(1);
        }else{
            message= messageList.getFirst();
        }
    }

}