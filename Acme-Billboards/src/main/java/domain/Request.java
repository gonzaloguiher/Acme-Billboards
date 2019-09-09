package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Request extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String status;
	private Collection<String> commentsCustomer;
	private Collection<String> commentsManager;

	// Getters and Setters ---------------------------------------------------

	@NotBlank
	@Pattern(regexp = "^PENDING|APPROVED|REJECTED$")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ElementCollection
	public Collection<String> getCommentsCustomer() {
		return commentsCustomer;
	}

	public void setCommentsCustomer(Collection<String> commentsCustomer) {
		this.commentsCustomer = commentsCustomer;
	}

	@ElementCollection
	public Collection<String> getCommentsManager() {
		return commentsManager;
	}

	public void setCommentsManager(Collection<String> commentsManager) {
		this.commentsManager = commentsManager;
	}

	// Relationships ----------------------------------------------------------

	private Customer customer;
	private Contract contract;
	private Package pakage;

	@Valid
	@ManyToOne(optional = true)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Valid
	@ManyToOne(optional = true)
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@Valid
	@ManyToOne(optional = true)
	public Package getPakage() {
		return pakage;
	}

	public void setPakage(Package pakage) {
		this.pakage = pakage;
	}

}
