package org.inmediart.gui.client;

import org.inmediart.gui.dto.RechargeUserCreditLogDTO;
import org.inmediart.gui.dto.UserCreditDTO;
import org.inmediart.gui.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "internal-credit", url = "${feign.url}/internal-credit")
public interface UserCreditResourceClient {
    @GetMapping("/{userId}")
    UserCreditDTO findById(@PathVariable("userId") Long userId);

    @GetMapping("/totalUserCredit")
    BigDecimal totalUserCredit();

    @PutMapping("/{credit}")
    UserCreditDTO addCredit(@RequestBody UserDTO user, @PathVariable("credit") BigDecimal credit);

    @PutMapping(value = "/order/{orderId}/pay")
    String makePayment(@PathVariable("orderId") Long orderId);

    @GetMapping("/{userId}/log")
    List<RechargeUserCreditLogDTO> findRechargeUserCreditLogByUserId(@PathVariable("userId") Long userId);

}
