package mvc.spring.security.services;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mvc.spring.security.entities.Role;
import mvc.spring.security.entities.User;
import mvc.spring.security.repositories.RoleRepository;
import mvc.spring.security.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = findUserByName(name);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Пользователь '%s' не найден", name));
        }
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    @Override
    @Transactional
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() != null) {
            Set<Role> selectedRoles = user.getRoles().stream()
                    .map(role -> roleRepository.findById(role.getId()).orElseThrow())
                    .collect(Collectors.toSet());
            user.setRoles(selectedRoles);
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(int id, User userWithNewInfo) {
        User updatedUser = userRepository.findUserById(id);
        updatedUser.setName(userWithNewInfo.getName());
        updatedUser.setAge(userWithNewInfo.getAge());
        updatedUser.setEmail(userWithNewInfo.getEmail());
        updatedUser.setRoles(userWithNewInfo.getRoles());
        if (userWithNewInfo.getPassword() != null && !userWithNewInfo.getPassword().isEmpty()) {
            updatedUser.setPassword(passwordEncoder.encode(userWithNewInfo.getPassword()));
        }
        userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.deleteUserById(id);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }
}