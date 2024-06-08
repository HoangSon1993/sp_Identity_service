package com.sondev.identityservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

//@Getter
//@Setter
@Data //@Getter //@Setter
@NoArgsConstructor // Create Constructor 0 tham số
@AllArgsConstructor  // Create Cóntructor với tất cả tham số
@Builder  // Tương tự như Object initalize của C#
@FieldDefaults(level = AccessLevel.PRIVATE) // Tất cả các Field đều là Private
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "INVALID_PASSWORD")
    String password;

    String firstName;
    String lastName;

    private LocalDate dob;
}
