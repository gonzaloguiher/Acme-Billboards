package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {

	@Query("select c from Contract c where c.pakage.id =?")
	Contract getContractByPackage(int id);
	
	@Query("select c from Contract c join c.pakage p where p.manager.id =?")
	Collection<Contract> getContractsByManager(int id);
	
	@Query("select c from Contract c join c.files f where f.id = ?1")
	Contract findContractByFileId(int id);
	
}
