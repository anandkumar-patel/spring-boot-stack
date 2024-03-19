package anand.person.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import anand.person.entity.Person;
import anand.person.service.PersonService;
import anand.person.vo.ResponseTemplateVO;


@RestController
@RequestMapping("/users")
public class PersonController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PersonController.class);

    @Autowired
    private PersonService personService;

    @PostMapping("/")
    public Person savePerson(@RequestBody Person person) {
        log.info("Inside savePerson of PersonController");

        return personService.savePerson(person);
    }

    @GetMapping("/{id}")
    public ResponseTemplateVO getPersonWithPassport(@PathVariable("id") Long personId) {
        log.info("Inside getPersonWithPassport of PersonController");
        return personService.getPersonWithPassport(personId);
    }


}
