package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Manager extends Actor {

	// Relationships ----------------------------------------------------------

	private Collection<Package> packages;

	@Valid
	@OneToMany(mappedBy = "manager")
	public Collection<Package> getPackages() {
		return packages;
	}

	public void setPackages(Collection<Package> packages) {
		this.packages = packages;
	}

}
