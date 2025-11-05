package com.app.trainerattendence.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String id;

    private String userId;
    private String name;
    private String email;
    private String password;
    private String department;
    private String role; // USER / ADMIN

    @Field("profilePhoto")
    private byte[] profilePhoto; // ✅ Store Profile Photo as Binary
}
