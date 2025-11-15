package practical.task.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practical.task.userservice.dto.request.paymentCardDto.CreatePaymentCardDto;
import practical.task.userservice.dto.request.paymentCardDto.UpdatePaymentCardDto;
import practical.task.userservice.dto.response.PaymentCardResponse;
import practical.task.userservice.service.PaymentCardService;

@RestController
@RequestMapping("/api/card")
public class PaymentCardController {

    private final PaymentCardService paymentCardService;

    @Autowired
    public PaymentCardController(PaymentCardService paymentCardService) {
        this.paymentCardService = paymentCardService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PaymentCardResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(paymentCardService.getOneById(id));
    }

    @GetMapping("/get/all")
    public ResponseEntity<Page<PaymentCardResponse>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(paymentCardService.getAll(pageable));
    }

    @PostMapping("/registration")
    public ResponseEntity<PaymentCardResponse> createPaymentCard(@RequestBody CreatePaymentCardDto createPaymentCardDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentCardService.createPaymentCard(createPaymentCardDto));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<PaymentCardResponse> updateUser(@PathVariable Long id, @RequestBody UpdatePaymentCardDto updatePaymentCardDto) {
        return ResponseEntity.ok(paymentCardService.updatePaymentCardById(id, updatePaymentCardDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        paymentCardService.deletePaymentCardById(id);
        return ResponseEntity.noContent().build();
    }

}
