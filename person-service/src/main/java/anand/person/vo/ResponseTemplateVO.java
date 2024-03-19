package anand.person.vo;

import anand.person.entity.Person;

public class ResponseTemplateVO {

    private Person person;
    private Passport passport;
	
    public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Passport getPassport() {
		return passport;
	}
	public void setPassport(Passport passport) {
		this.passport = passport;
	}
}
