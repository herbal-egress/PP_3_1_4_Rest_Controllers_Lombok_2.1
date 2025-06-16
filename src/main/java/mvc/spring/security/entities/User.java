package mvc.spring.security.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.*;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@FieldDefaults(level = PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "name", length = 50, unique = true)
    String name;
    @Column(name = "age", length = 3)
    int age;
    @Column(name = "email", length = 100, unique = true)
    String email;
    String password;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        if (id != 0 && user.id != 0) {
            return id == user.id;
        }
        return email != null && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        if (id != 0) {
            return id;
        }
        return email != null ? email.hashCode() : 0;
    }
}