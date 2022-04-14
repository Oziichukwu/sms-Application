package com.example.smsapplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    @Size(min = 6, max = 16)
    private String smsSender;

    @Size(min = 1, max = 120)
    private String smsBody;

    @Size(min = 6, max = 16)
    private String smsReceiver;

}
