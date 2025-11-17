package practical.task.userservice.dto.response;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String name,
        String surname,
        LocalDate birthDate,
        String email,
        boolean active
) {
}
