package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Contract extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String text;
	private String hash;
	private Date momentCustomer;
	private Date momentManager;
	private String signCustomer;
	private String signManager;
	private String status;

	// Getters and Setters ---------------------------------------------------

	@NotBlank
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMomentCustomer() {
		return momentCustomer;
	}

	public void setMomentCustomer(Date momentCustomer) {
		this.momentCustomer = momentCustomer;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMomentManager() {
		return momentManager;
	}

	public void setMomentManager(Date momentManager) {
		this.momentManager = momentManager;
	}

	public String getSignCustomer() {
		return signCustomer;
	}

	public void setSignCustomer(String signCustomer) {
		this.signCustomer = signCustomer;
	}

	public String getSignManager() {
		return signManager;
	}

	public void setSignManager(String signManager) {
		this.signManager = signManager;
	}

	@NotBlank
	@Pattern(regexp = "^DRAFT|FINAL$")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	// Relationships ----------------------------------------------------------

	private Package pakage;
	private Collection<File> files;

	@Valid
	@ElementCollection
	@OneToMany(mappedBy = "contract", cascade = { CascadeType.ALL, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	public Collection<File> getFiles() {
		return files;
	}

	public void setFiles(Collection<File> files) {
		this.files = files;
	}

	@Valid
	@OneToOne(optional = false)
	public Package getPakage() {
		return pakage;
	}

	public void setPakage(Package pakage) {
		this.pakage = pakage;
	}

}