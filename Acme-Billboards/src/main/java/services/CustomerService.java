package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.CustomerRepository;
import repositories.RequestRepository;
import security.LoginService;
import security.UserAccount;

import domain.Customer;
import domain.Request;
import forms.CustomerForm;

@Service
@Transactional
public class CustomerService {

	@Autowired
	private RequestRepository requestRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public Customer create(UserAccount ua) {
		Customer result = new Customer();
		
		Collection<Request> requests = new HashSet<Request>();
		
		result.setUserAccount(ua);
		result.setRequests(requests);
		return result;
	}

	public Collection<Customer> findAll() {
		return customerRepository.findAll();
	}

	public Customer findOne(int id) {
		return customerRepository.findOne(id);
	}

	public Customer save(Customer customer) {

		Customer saved = customerRepository.saveAndFlush(customer);
		return saved;
	}

	public void delete(Customer customer) {
		customerRepository.delete(customer);
	}

	public Customer findByPrincipal() {
		return customerRepository.getCustomerByUserAccountId((LoginService.getPrincipal().getId()));
	}
	
	@SuppressWarnings("deprecation")
	public CustomerForm rellenaForm (Customer customer , CustomerForm customerform , List<String> parts){
		
		if (parts.contains("personal") || parts.isEmpty()) {
			
			// Personal Data
			
			customerform.setName(customer.getName());
			customerform.setMiddleName(customer.getMiddleName());
			customerform.setSurname(customer.getSurname());
			customerform.setAddress(customer.getAddress());
			customerform.setEmail(customer.getEmail());
			customerform.setPhone(customer.getPhone());
			customerform.setPhoto(customer.getPhoto());
			customerform.setVatNumber(customer.getVatNumber());
		}
		
		if(parts.contains("credit") || parts.isEmpty()) {
			
			// Credit Card
			
			customerform.setCVV(customer.getCreditCard().getCVV());
			customerform.setExpirationMonth(customer.getCreditCard().getExpirationDate().getMonth());
			customerform.setExpirationYear(customer.getCreditCard().getExpirationDate().getYear()+1900);
			customerform.setHolder(customer.getCreditCard().getHolder());
			customerform.setMake(customer.getCreditCard().getMake());
			customerform.setNumber(customer.getCreditCard().getNumber());
		}
		
		if(parts.contains("account")|| parts.isEmpty()) {
			
			// Account
			
			customerform.setPassword("default");
			customerform.setPassword2("default");
			customerform.setUsername(customer.getUserAccount().getUsername());
		}
		
		
		return customerform;
	}
	
	public void flush() {
		this.customerRepository.flush();
	}


	public Double getAvgRequestsPerCustomer() {
		Double res = customerRepository.getAvgRequestsPerCustomer();
		if (res == null)
			res = 0d;
		return res;
	}

	public Integer getMinRequestsPerCustomer() {
		Integer res = customerRepository.getMinRequestsPerCustomer();
		if (res == null)
			res = 0;
		return res;
	}

	public Integer getMaxRequestsPerCustomer() {
		Integer res = customerRepository.getMaxRequestsPerCustomer();
		if (res == null)
			res = 0;
		return res;
	}

	public Double getStdevRequestsPerCustomer() {
		Double res = customerRepository.getStdevRequestsPerCustomer();
		if (res == null)
			res = 0d;
		return res;
	}
	
	public Double getAvgRequestsPerCustomerStatusPending() {
		Double res = customerRepository.getAvgRequestsPerCustomerStatusPending();
		if (res == null)
			res = 0d;
		return res;
	}
	
	public Double getAvgRequestsPerCustomerStatusApproved() {
		Double res = customerRepository.getAvgRequestsPerCustomerStatusApproved();
		if (res == null)
			res = 0d;
		return res;
	}
	
	public Double getAvgRequestsPerCustomerStatusRejected() {
		Double res = customerRepository.getAvgRequestsPerCustomerStatusRejected();
		if (res == null)
			res = 0d;
		return res;
	}
	
	public Integer getMinRequestsPerCustomerStatusPending() {
		Integer res = customerRepository.getMinRequestsPerCustomerStatusPending();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Integer getMinRequestsPerCustomerStatusApproved() {
		Integer res = customerRepository.getMinRequestsPerCustomerStatusApproved();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Integer getMinRequestsPerCustomerStatusRejected() {
		Integer res = customerRepository.getMinRequestsPerCustomerStatusRejected();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Integer getMaxRequestsPerCustomerStatusPending() {
		Integer res = customerRepository.getMaxRequestsPerCustomerStatusPending();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Integer getMaxRequestsPerCustomerStatusApproved() {
		Integer res = customerRepository.getMaxRequestsPerCustomerStatusApproved();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Integer getMaxRequestsPerCustomerStatusRejected() {
		Integer res = customerRepository.getMaxRequestsPerCustomerStatusRejected();
		if (res == null)
			res = 0;
		return res;
	}
	
	public Collection<Customer> customersWith10PercentPending() {
		
		Collection<Customer> res = new ArrayList<>();
		Collection<Object>   req = new ArrayList<>();
		Map<Long, Customer> map = new HashMap<>();
		req = requestRepository.getRequestsPendingByCustomer();
		
		for(Object o: req) {
			Object[] n = (Object[]) o;
			map.put((Long) n[0], (Customer) n[1]);
		}

		for(int i = 0; i < map.keySet().size(); i++) {
			Long t = (Long) map.keySet().toArray()[i];
			Customer s = map.get(t);
				if(t > requestRepository.get10RequestsByCustomer(s.getId())) {
					res.add(map.get(map.keySet().toArray()[i]));
				}
		}
		return res;
	}
	
	public Collection<Customer> customersWith10PercentApproved() {
		
		Collection<Customer> res = new ArrayList<>();
		Collection<Object>   req = new ArrayList<>();
		Map<Long, Customer> map = new HashMap<>();
		req = requestRepository.getRequestsApprovedByCustomer();
		
		for(Object o: req) {
			Object[] n = (Object[]) o;
			map.put((Long) n[0], (Customer) n[1]);
		}

		for(int i = 0; i < map.keySet().size(); i++) {
			Long t = (Long) map.keySet().toArray()[i];
			Customer s = map.get(t);
				if(t > requestRepository.get10RequestsByCustomer(s.getId())) {
					res.add(map.get(map.keySet().toArray()[i]));
				}
		}
		return res;
	}
	
	public Collection<Customer> customersWith10PercentRejected() {
		
		Collection<Customer> res = new ArrayList<>();
		Collection<Object>   req = new ArrayList<>();
		Map<Long, Customer> map = new HashMap<>();
		req = requestRepository.getRequestsRejectedByCustomer();
		
		for(Object o: req) {
			Object[] n = (Object[]) o;
			map.put((Long) n[0], (Customer) n[1]);
		}

		for(int i = 0; i < map.keySet().size(); i++) {
			Long t = (Long) map.keySet().toArray()[i];
			Customer s = map.get(t);
				if(t > requestRepository.get10RequestsByCustomer(s.getId())) {
					res.add(map.get(map.keySet().toArray()[i]));
				}
		}
		return res;
	}
	
}
