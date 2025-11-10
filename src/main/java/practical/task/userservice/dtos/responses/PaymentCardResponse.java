package practical.task.userservice.dtos.responses;

import java.time.LocalDate;

public record PaymentCardResponse(
        Long id,
        String holder,
        String number,
        LocalDate expirationDate,
        Long userId
) {
}
