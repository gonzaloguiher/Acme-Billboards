package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query("select c from Customer c where c.userAccount.id = ?1")
	Customer getCustomerByUserAccountId(int id);
	
	@Query("select avg(c.requests.size) from Customer c")
	Double getAvgRequestsPerCustomer();
	
	@Query("select min(c.requests.size) from Customer c")
	Integer getMinRequestsPerCustomer();
	
	@Query("select max(c.requests.size) from Customer c")
	Integer getMaxRequestsPerCustomer();
	
//	@Query("select stddev(c.requests.size) from Customer c")
//	Double getStdevRequestsPerCustomer();
	
	@Query("select count(r)*1.0/(select count(c) from Customer c) from Request r where r.status = 'PENDING'")
	Double getAvgRequestsPerCustomerStatusPending();
	
	@Query("select count(r)*1.0/(select count(c) from Customer c) from Request r where r.status = 'APPROVED'")
	Double getAvgRequestsPerCustomerStatusApproved();
	
	@Query("select count(r)*1.0/(select count(c) from Customer c) from Request r where r.status = 'REJECTED'")
	Double getAvgRequestsPerCustomerStatusRejected();
	
	@Query("select distinct count(r) from Request r join r.customer c where r.status = 'PENDING' and (select count(r1) from Request r1 where r1.customer.id = c.id) <= all (select count(r2) from Request r2 group by r2.customer) group by r.customer")
	Integer getMinRequestsPerCustomerStatusPending();
	
	@Query("select distinct count(r) from Request r join r.customer c where r.status = 'APPROVED' and (select count(r1) from Request r1 where r1.customer.id = c.id) <= all (select count(r2) from Request r2 group by r2.customer) group by r.customer")
	Integer getMinRequestsPerCustomerStatusApproved();
	
	@Query("select distinct count(r) from Request r join r.customer c where r.status = 'REJECTED' and (select count(r1) from Request r1 where r1.customer.id = c.id) <= all (select count(r2) from Request r2 group by r2.customer) group by r.customer")
	Integer getMinRequestsPerCustomerStatusRejected();
	
	@Query("select distinct count(r) from Request r join r.customer c where r.status = 'PENDING' and (select count(r1) from Request r1 where r1.customer.id = c.id) >= all (select count(r2) from Request r2 group by r2.customer) group by r.customer")
	Integer getMaxRequestsPerCustomerStatusPending();
	
	@Query("select distinct count(r) from Request r join r.customer c where r.status = 'APPROVED' and (select count(r1) from Request r1 where r1.customer.id = c.id) >= all (select count(r2) from Request r2 group by r2.customer) group by r.customer")
	Integer getMaxRequestsPerCustomerStatusApproved();
	
	@Query("select distinct count(r) from Request r join r.customer c where r.status = 'REJECTED' and (select count(r1) from Request r1 where r1.customer.id = c.id) >= all (select count(r2) from Request r2 group by r2.customer) group by r.customer")
	Integer getMaxRequestsPerCustomerStatusRejected();
	
	@Query("select sqrt(sum(c.requests.size*c.requests.size)) /(count(c)-(avg(c.requests.size)*avg(c.requests.size))) from Customer c")
	Double getStdevRequestsPerCustomer();
	
}
