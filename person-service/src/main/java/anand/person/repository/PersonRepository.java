package anand.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anand.person.entity.Person;

@Repository
public interface PersonRepository  extends JpaRepository<Person,Long> {
    Person findByPersonId(Long personId);
}
