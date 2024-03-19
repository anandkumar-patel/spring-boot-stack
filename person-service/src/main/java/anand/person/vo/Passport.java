package anand.person.vo;

public class Passport {

    private Long passportId;
	private Long passportNumber;
    private String country;
    private String nationality;
    
    public Long getPassportId() {
		return passportId;
	}
	public void setPassportId(Long passportId) {
		this.passportId = passportId;
	}
	public Long getPassportNumber() {
		return passportNumber;
	}
	public void setPassportNumber(Long passportNumber) {
		this.passportNumber = passportNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
}
