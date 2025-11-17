package practical.task.userservice.dto.request.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UserCreateDto(

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Surname is required")
        String surname,

        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date should be in the past")
        LocalDate birthDate,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email
) {
}
