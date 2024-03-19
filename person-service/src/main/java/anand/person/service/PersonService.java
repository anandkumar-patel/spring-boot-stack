package anand.person.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import anand.person.entity.Person;
import anand.person.repository.PersonRepository;
import anand.person.vo.Passport;
import anand.person.vo.ResponseTemplateVO;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Person savePerson(Person person) {
        //log.info("Inside savePerson of PersonService");
        return personRepository.save(person);
    }

    public ResponseTemplateVO getPersonWithPassport(Long personId) {
        //log.info("Inside getPersonWithPassport of PersonService");
        ResponseTemplateVO vo = new ResponseTemplateVO();
        Person person = personRepository.findByPersonId(personId);

        Passport passport =
                restTemplate.getForObject("http://passport-service/passports/" + person.getPassportId()
                        ,Passport.class);

        vo.setPerson(person);
        vo.setPassport(passport);

        return  vo;
    }
}
