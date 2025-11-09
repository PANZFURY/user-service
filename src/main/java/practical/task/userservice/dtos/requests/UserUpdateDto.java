package practical.task.userservice.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserUpdateDto(
        @NotBlank
        String name,
        @NotBlank
        String surname,
        @NotNull
        LocalDate birthDate,
        @Email
        String email
) {
}
