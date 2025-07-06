package com.ecommerce.user.dto;


import com.ecommerce.user.models.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Address address;
}
