package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

//import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class File extends DomainEntity {

	// Attributes -----------------------------------------------------------

	private String location;
	private String image;

	// Getters and Setters ---------------------------------------------------

//	@NotBlank
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@URL
//	@NotBlank
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	// Relationships ----------------------------------------------------------
	
	private Contract contract;
	
	@Valid
	@ManyToOne(optional = false)
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
}