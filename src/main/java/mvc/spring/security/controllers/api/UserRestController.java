package mvc.spring.security.controllers.api;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mvc.spring.security.entities.Role;
import mvc.spring.security.entities.User;
import mvc.spring.security.entities.dto.UserDTO;
import mvc.spring.security.services.RoleService;
import mvc.spring.security.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserRestController {
    UserService userService;
    RoleService roleService;
    ModelMapper modelMapper;

    @GetMapping("/api/user")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = userService.findAllUser().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/api/user/current")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
        User user = userService.findUserByName(principal.getName());
        return ResponseEntity.ok(convertToDTO(user));
    }

    @GetMapping("/api/user/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAllRole());
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}