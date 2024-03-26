package kg.zhav.miksbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRole {

    public static final UserRole admin = new UserRole(Name.ADMIN);
    public static final UserRole staff = new UserRole(Name.STAFF);
    public static final UserRole user = new UserRole(Name.USER);

    @Id
    private UserRole.Name rolename;

    @PrePersist
    void defaultRole() {
        if (rolename == null)
            rolename = Name.USER;
    }

    public enum Name {
        ADMIN,
        STAFF,
        USER,
    }
}
