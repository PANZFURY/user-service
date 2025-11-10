package practical.task.userservice.dtos.requests.paymentCardDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreatePaymentCardDto(

        Long userId,

        @NotBlank(message = "Number is required")
        String number,

        @NotNull(message = "Expiration date is required")
        LocalDate expirationDate
) {
}
