package anand.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

    @GetMapping("/personServiceFallBack")
    public String personServiceFallBackMethod() {
        return "Person Service is taking longer than Expected." +
                " Please try again later";
    }

    @GetMapping("/passportServiceFallBack")
    public String passportServiceFallBackMethod() {
        return "Passport Service is taking longer than Expected." +
                " Please try again later";
    }
}
