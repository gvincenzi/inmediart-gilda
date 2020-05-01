package org.inmediart.telegram.bot.client;

import org.inmediart.telegram.bot.dto.UserCreditDTO;
import org.inmediart.telegram.bot.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "internal-credit", url = "${feign.url}/internal-credit")
public interface UserCreditResourceClient {
    @GetMapping("/{userId}")
    UserCreditDTO findById(@PathVariable("userId") Long userId);

    @PostMapping("/{additionalCredit}")
    UserCreditDTO newCredit(@RequestBody UserDTO userDTO, @PathVariable("additionalCredit") BigDecimal additionalCredit);

    @GetMapping("/totalUserCredit")
    BigDecimal totalUserCredit();

    @PutMapping("/{credit}")
    UserCreditDTO addCredit(@RequestBody UserDTO user, @PathVariable("credit") BigDecimal credit);

    @PutMapping(value = "/order/{orderId}/pay")
    String makePayment(@PathVariable("orderId") Long orderId);
}
