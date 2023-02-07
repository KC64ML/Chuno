package com.leesfamily.chuno.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="items")
@DynamicInsert
@DynamicUpdate
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String imgPath;

    @Column(nullable = false)
    private int forRunner;


}
