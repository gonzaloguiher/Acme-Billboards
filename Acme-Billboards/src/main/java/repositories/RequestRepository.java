package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import domain.Customer;
import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r from Request r where r.customer.id =?")
	Collection<Request> getRequestsByCustomer(int id);

	@Query("select r from Request r join r.pakage p where p.id =?")
	Collection<Request> getRequestsByPackage(int id);

	@Query("select r from Request r join r.pakage p where p.manager.id =?")
	Collection<Request> getRequestsByManager(int id);
	
	@Query("select count(r), r.customer from Request r where r.status='PENDING' group by r.customer")
	Collection<Object> getRequestsPendingByCustomer();
	
	@Query("select count(r), r.customer from Request r where r.status='APPROVED' group by r.customer")
	Collection<Object> getRequestsApprovedByCustomer();
	
	@Query("select count(r), r.customer from Request r where r.status='REJECTED' group by r.customer")
	Collection<Object> getRequestsRejectedByCustomer();
	
	@Query("select count(r)*0.1 from Request r where r.customer.id = ?1")
	Double get10RequestsByCustomer(int customerId);
	
	@Query("select distinct r.customer from Request r where r.status='PENDING' order by r")
	Collection<Customer> top10CustomersRequestsStatusPending();
	
	@Query("select distinct r.customer from Request r where r.status='APPROVED' order by r")
	Collection<Customer> top10CustomersRequestsStatusApproved();
	
	@Query("select distinct r.customer from Request r where r.status='REJECTED' order by r")
	Collection<Customer> top10CustomersRequestsStatusRejected();
	
}