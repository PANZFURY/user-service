package practical.task.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import practical.task.userservice.dto.response.PaymentCardResponse;
import practical.task.userservice.service.PaymentCardService;

@RestController("/api/card")
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
}
