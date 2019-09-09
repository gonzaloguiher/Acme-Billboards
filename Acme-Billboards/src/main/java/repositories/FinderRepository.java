package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select p from Package p where " +
			"((:keyword is null or :keyword like '' ) or " +
				"(p.ticker like %:keyword% "  +
				"or p.title like %:keyword% " +
				"or p.description like %:keyword%)) ")
	Collection<domain.Package> filterPackages(@Param("keyword") String keyword);
	
	@Query("select f from Finder f where f.customer.id = :customerId")
	Finder findByCustomer(@Param("customerId") Integer customerId);
	
	@Query("select avg(f.packages.size) from Finder f")
	Double getAvgResultsPerFinder();

	@Query("select min(f.packages.size) from Finder f")
	Integer getMinResultsPerFinder();
	
	@Query("select max(f.packages.size) from Finder f")
	Integer getMaxResultsPerFinder();
	
	@Query("select stddev(f.packages.size) from Finder f")
	Double getStdevResultsPerFinder();
}
