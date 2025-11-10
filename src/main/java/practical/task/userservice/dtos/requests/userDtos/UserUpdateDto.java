package practical.task.userservice.dtos.requests.userDtos;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;

public record UserUpdateDto(

        String name,

        String surname,

        LocalDate birthDate,

        @Email
        String email
) {
}
