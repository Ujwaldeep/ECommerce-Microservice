package com.ecommerce.user.models;


import lombok.Data;

@Data
public class Address {
    private Long id;

    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;

}
