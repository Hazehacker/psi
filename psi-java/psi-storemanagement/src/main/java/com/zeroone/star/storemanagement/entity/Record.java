package com.zeroone.star.storemanagement.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Record {
    private String id;

    private String type;

    private String source;

    private LocalDateTime time;

    private String user;

    private String info;
}
