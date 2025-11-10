package practical.task.userservice.dtos.requests.paymentCardDtos;

import java.time.LocalDate;

public record UpdatePaymentCardDto(
        Long userId,

        String number,

        LocalDate expirationDate,

        Boolean active
) {

}
