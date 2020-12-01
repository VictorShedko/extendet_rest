package com.epam.esm.gift_extended.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Order {

    private Integer certId;
    private Date orderDate;
    private Float cost;
}
