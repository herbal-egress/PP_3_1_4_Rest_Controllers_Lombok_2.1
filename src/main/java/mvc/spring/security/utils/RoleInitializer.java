package mvc.spring.security.utils;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mvc.spring.security.services.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RoleInitializer implements CommandLineRunner {
    RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        if (roleService.findRoleByName("ROLE_ADMIN") == null) {
            roleService.saveRole("ROLE_ADMIN");
        }
        if (roleService.findRoleByName("ROLE_USER") == null) {
            roleService.saveRole("ROLE_USER");
        }
    }
}