package mvc.spring.security.controllers.api;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mvc.spring.security.entities.User;
import mvc.spring.security.entities.dto.UserRequestDTO;
import mvc.spring.security.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AdminRestController {
    UserService userService;
    ModelMapper modelMapper;

    @PostMapping("api/admin")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO userDTO) {
        User user = convertToEntity(userDTO);
        userService.register(user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("api/admin/id")
    public ResponseEntity<?> updateUser(@RequestParam int id, @RequestBody UserRequestDTO userDTO) {
        User user = convertToEntity(userDTO);
        userService.update(id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("api/admin/id")
    public ResponseEntity<?> deleteUser(@RequestParam int id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    private User convertToEntity(UserRequestDTO userDTO) {
        return modelMapper.map(userDTO, User.class); // Мапер сравнивает классы и выдаёт объект, заполненный только
        // одинаковыми полями
    }
}