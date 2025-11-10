package practical.task.userservice.dto.request.paymentCardDto;

import java.time.LocalDate;

public record UpdatePaymentCardDto(
        Long userId,

        String number,

        LocalDate expirationDate,

        Boolean active
) {

}
