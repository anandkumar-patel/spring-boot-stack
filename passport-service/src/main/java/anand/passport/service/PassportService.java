package anand.passport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import anand.passport.entity.Passport;
import anand.passport.repository.PassportRepository;

@Service
public class PassportService {

    @Autowired
    private PassportRepository passportRepository;

    public Passport savePassport(Passport passport) {
        //log.info("Inside savePassport of PassportService");
        return passportRepository.save(passport);
    }

    public Passport findByPassportId(Long passportId) {
        //log.info("Inside findByPassportId of PassportService");
        return passportRepository.findByPassportId(passportId);
    }
}
