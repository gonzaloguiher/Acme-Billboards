package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<domain.Package, Integer> {

	@Query("select p from Package p where p.manager.id =?")
	Collection<domain.Package> getPackagesByManager(int id);
	
	@Query("select p from Package p where p.manager.id =?1 and p.isDraft = false")
	Collection<domain.Package> getPackagesFinalByManager(int id);
	
	@Query("select p from Package p where p.manager.id =?1 and p.isDraft = true")
	Collection<domain.Package> getPackagesDraftByManager(int id);

	@Query("select p from Package p where p.isDraft = true")
	Collection<domain.Package> getPackagesIsDraft();

	@Query("select p from Package p where p.isDraft = false")
	Collection<domain.Package> getPackagesNoDraft();
	
	@Query("select p from Package p where p.isDraft = false and " +
			"((:keyword is null or :keyword like '' ) or " +
				"(p.ticker like %:keyword% " +
				"or p.title like %:keyword% " +
				"or p.description like %:keyword%))")
	Collection<domain.Package> searchPackages(@Param("keyword") String keyword);
	
	@Query("select p from Package p where p.isDraft = true and " +
			"((:keyword is null or :keyword like '' ) or " +
				"(p.ticker like %:keyword% " +
				"or p.title like %:keyword% " +
				"or p.description like %:keyword%)) " +
				"and (p.manager.id =:managerId)")
	Collection<domain.Package> searchPackagesWithManager(@Param("keyword") String keyword, @Param("managerId") Integer managerId);
}
