package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select m from Manager m where m.userAccount.id = ?1")
	Manager getManagerByUserAccountId(int id);
	
	@Query("select count(r)*1.0/(select count(m) from Manager m) from Request r join r.pakage p join p.manager m")
	Double getAvgRequestsPerManager();
	
	@Query("select count(r)*1.0/(select count(m) from Manager m) from Request r join r.pakage p join p.manager m where r.status = 'PENDING'")
	Double getAvgRequestsPerManagerStatusPending();
	
	@Query("select count(r)*1.0/(select count(m) from Manager m) from Request r join r.pakage p join p.manager m where r.status = 'APPROVED'")
	Double getAvgRequestsPerManagerStatusApproved();
	
	@Query("select count(r)*1.0/(select count(m) from Manager m) from Request r join r.pakage p join p.manager m where r.status = 'REJECTED'")
	Double getAvgRequestsPerManagerStatusRejected();

	@Query("select distinct count(r) from Request r join r.pakage p join p.manager m where (select count(r1) from Request r1 join r1.pakage p1 join p1.manager m1 where m1.id = m.id) <= all (select count(r2) from Request r2 join r2.pakage p2 join p2.manager m2 group by m2) group by p.manager")
	Integer getMinRequestsPerManager();
	
	@Query("select distinct count(r) from Request r join r.pakage p join p.manager m where r.status = 'PENDING' and (select count(r1) from Request r1 join r1.pakage p1 join p1.manager m1 where m1.id = m.id) <= all (select count(r2) from Request r2 join r2.pakage p2 join p2.manager m2 group by m2) group by p.manager")
	Integer getMinRequestsPerManagerStatusPending();
	
	@Query("select distinct count(r) from Request r join r.pakage p join p.manager m where r.status = 'APPROVED' and (select count(r1) from Request r1 join r1.pakage p1 join p1.manager m1 where m1.id = m.id) <= all (select count(r2) from Request r2 join r2.pakage p2 join p2.manager m2 group by m2) group by p.manager")
	Integer getMinRequestsPerManagerStatusApproved();
	
	@Query("select distinct count(r) from Request r join r.pakage p join p.manager m where r.status = 'REJECTED' and (select count(r1) from Request r1 join r1.pakage p1 join p1.manager m1 where m1.id = m.id) <= all (select count(r2) from Request r2 join r2.pakage p2 join p2.manager m2 group by m2) group by p.manager")
	Integer getMinRequestsPerManagerStatusRejected();
	
	@Query("select distinct count(r) from Request r join r.pakage p join p.manager m where (select count(r1) from Request r1 join r1.pakage p1 join p1.manager m1 where m1.id = m.id) >= all (select count(r2) from Request r2 join r2.pakage p2 join p2.manager m2 group by m2) group by p.manager")
	Integer getMaxRequestsPerManager();
	
	@Query("select distinct count(r) from Request r join r.pakage p join p.manager m where r.status = 'PENDING' and (select count(r1) from Request r1 join r1.pakage p1 join p1.manager m1 where m1.id = m.id) >= all (select count(r2) from Request r2 join r2.pakage p2 join p2.manager m2 group by m2) group by p.manager")
	Integer getMaxRequestsPerManagerStatusPending();
	
	@Query("select distinct count(r) from Request r join r.pakage p join p.manager m where r.status = 'APPROVED' and (select count(r1) from Request r1 join r1.pakage p1 join p1.manager m1 where m1.id = m.id) >= all (select count(r2) from Request r2 join r2.pakage p2 join p2.manager m2 group by m2) group by p.manager")
	Integer getMaxRequestsPerManagerStatusApproved();
	
	@Query("select distinct count(r) from Request r join r.pakage p join p.manager m where r.status = 'REJECTED' and (select count(r1) from Request r1 join r1.pakage p1 join p1.manager m1 where m1.id = m.id) >= all (select count(r2) from Request r2 join r2.pakage p2 join p2.manager m2 group by m2) group by p.manager")
	Integer getMaxRequestsPerManagerStatusRejected();
	
	@Query("select count(r)*0.1 from Request r join r.pakage p join p.manager m where m.id = ?1")
	Double get10RequestsByManager(int managerId);
	
	@Query("select distinct m from Request r join r.pakage p join p.manager m where r.status='PENDING' order by r")
	Collection<Manager> top10ManagersRequestsStatusPending();
	
	@Query("select distinct m from Request r join r.pakage p join p.manager m where r.status='APPROVED' order by r")
	Collection<Manager> top10ManagersRequestsStatusApproved();
	
	@Query("select distinct m from Request r join r.pakage p join p.manager m where r.status='REJECTED' order by r")
	Collection<Manager> top10ManagersRequestsStatusRejected();
	
}