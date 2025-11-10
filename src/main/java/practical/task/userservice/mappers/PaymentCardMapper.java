package practical.task.userservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import practical.task.userservice.dtos.requests.paymentCardDtos.CreatePaymentCardDto;
import practical.task.userservice.dtos.responses.PaymentCardResponse;
import practical.task.userservice.models.PaymentCard;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentCardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", expression = "java(true)")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PaymentCard fromPaymentCardCreateDto(CreatePaymentCardDto createPaymentCardDto);

    PaymentCardResponse toPaymentCardResponse(PaymentCard paymentCard);


}
