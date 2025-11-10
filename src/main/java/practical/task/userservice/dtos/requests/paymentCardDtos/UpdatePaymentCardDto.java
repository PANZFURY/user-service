package practical.task.userservice.dtos.requests.paymentCardDtos;

import java.time.LocalDate;

public class UpdatePaymentCardDto {

    private Long userId;

    private String number;

    private LocalDate expirationDate;

}
