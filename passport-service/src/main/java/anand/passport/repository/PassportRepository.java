package anand.passport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import anand.passport.entity.Passport;


@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {

	Passport findByPassportId(Long passportId);
}
