package practical.task.userservice.dto.request.paymentCardDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreatePaymentCardDto(

        @NotNull(message = "User id is required")
        Long userId,

        @NotBlank(message = "Number is required")
        String number,

        @NotNull(message = "Expiration date is required")
        LocalDate expirationDate
) {
}
