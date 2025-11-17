package practical.task.userservice.dto.response;

import java.time.LocalDate;

public record PaymentCardResponse(
        Long id,
        String holder,
        String number,
        LocalDate expirationDate,
        Long userId
) {
}
