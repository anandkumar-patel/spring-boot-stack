package anand.passport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anand.passport.entity.Passport;
import anand.passport.service.PassportService;

@RestController
@RequestMapping("/passports")
public class PassportController {

    @Autowired
    private PassportService passportService;

    @PostMapping("/")
    public Passport savePassport(@RequestBody Passport passport) {
        //log.info("Inside savePassport method of PassportController");
        return  passportService.savePassport(passport);
    }

    @GetMapping("/{id}")
    public Passport findByPassportId(@PathVariable("id") Long passportId) {
        //log.info("Inside findByPassportId method of PassportController");
        return passportService.findByPassportId(passportId);
    }

}
