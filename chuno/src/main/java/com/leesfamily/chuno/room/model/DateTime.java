package com.leesfamily.chuno.room.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DateTime {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
}
