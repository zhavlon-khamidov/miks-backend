package kg.zhav.miksbackend.repos;

import kg.zhav.miksbackend.entities.User;
import kg.zhav.miksbackend.entities.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username("user")
                .password("password")
                .email("user@example.com")
                .roles(Set.of(UserRole.user))
                .build();

        User staff = User.builder()
                .username("staff")
                .password("password")
                .email("staff@example.com")
                .roles(Set.of(UserRole.staff))
                .build();

        User admin = User.builder()
                .username("admin")
                .password("password")
                .email("admin@example.com")
                .roles(Set.of(UserRole.admin, UserRole.staff))
                .build();

        userRepository.saveAll(List.of(user, staff, admin));
    }


    @Test
    void listUsers() {
        List<User> users = userRepository.findAll();

        assertEquals(3, users.size());
    }

    @Test
    void addUser() {
        User newUser = User.builder()
                .username("newUser")
                .password("password")
                .email("newuser@example.com")
                .phoneNumber("+996555123456")
                .roles(Set.of(UserRole.user))
                .build();

        newUser = userRepository.save(newUser);

        assertNotNull(newUser);
        assertNotNull(newUser.getId());
        assertEquals("newUser",newUser.getUsername());
        assertEquals("password", newUser.getPassword());
        assertEquals("newuser@example.com", newUser.getEmail());
        assertEquals("+996555123456", newUser.getPhoneNumber());
        assertNotNull(newUser.getRoles());
        Optional<UserRole> optionalUserRole = newUser.getRoles().stream().findFirst();
        assertTrue(optionalUserRole.isPresent());
        assertEquals(UserRole.user.getRolename(), optionalUserRole.get().getRolename());

        System.out.println(roleRepository.findAll());

        assertEquals(3, roleRepository.findAll().size());
    }

    @Test
    void findByUsername() {
        Optional<User> user = userRepository.findByUsername("user");

        assertTrue(user.isPresent());
        assertEquals("user",user.get().getUsername());
        assertEquals("user@example.com", user.get().getEmail());
        assertEquals("password", user.get().getPassword());
    }

    @Test
    void findByEmail() {
        Optional<User> user = userRepository.findByEmail("user@example.com");

        assertTrue(user.isPresent());
        assertEquals("user",user.get().getUsername());
        assertEquals("user@example.com", user.get().getEmail());
        assertEquals("password", user.get().getPassword());
    }

    @Test
    void usernameUniqueConstraint() {
        User newUser = User.builder()
                .username("user")
                .build();

        assertThrows(DataIntegrityViolationException.class,
                () -> userRepository.saveAndFlush(newUser));
    }
}