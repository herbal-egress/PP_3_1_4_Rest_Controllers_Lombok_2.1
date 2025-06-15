package mvc.spring.security.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.*;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@FieldDefaults(level = PRIVATE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name.replace("ROLE_", "");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Role role = (Role) obj;
        if (id != 0 && role.id != 0) {
            return id == role.id;
        }
        return name != null && name.equals(role.name);
    }

    @Override
    public int hashCode() {
        if (id != 0) {
            return id;
        }
        return name != null ? name.hashCode() : 0;
    }
}