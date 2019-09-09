package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Entity
@Access(AccessType.PROPERTY)
public class Customer extends Actor {

	// Attributes -----------------------------------------------------------

	private String vatNumber;

	// Getters and Setters ---------------------------------------------------

	@Pattern(regexp = "^[A-Z]{3}[0-9]{8}$")
	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	// Relationships ----------------------------------------------------------

	private CreditCard creditCard;
	private Collection<Request> requests;

	@Valid
	@OneToOne
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@Valid
	@OneToMany(mappedBy = "customer")
	public Collection<Request> getRequests() {
		return requests;
	}

	public void setRequests(Collection<Request> requests) {
		this.requests = requests;
	}
}
