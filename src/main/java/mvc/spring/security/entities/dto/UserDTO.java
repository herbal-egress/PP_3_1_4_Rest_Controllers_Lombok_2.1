package mvc.spring.security.entities.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import mvc.spring.security.entities.Role;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserDTO {
    Integer id;
    String name;
    int age;
    String email;
    Set<Role> roles;
}