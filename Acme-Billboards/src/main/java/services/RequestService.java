package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ContractRepository;
import repositories.RequestRepository;
import security.LoginService;
import domain.Contract;
import domain.Customer;
import domain.File;
import domain.Manager;
import domain.Request;

@Service
@Transactional
public class RequestService {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private RequestRepository requestRepository;
	
	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private Validator validator;

	public Request create() {
		
		Assert.isTrue(LoginService.hasRole("CUSTOMER"));
		
		Request result = new Request();
		return result;
	}
	
	public Request createId(domain.Package pakage) {
		
		Assert.isTrue(LoginService.hasRole("CUSTOMER"));
		
		Request result = new Request();
		result.setPakage(pakage);
		return result;
	}

	public Collection<Request> findAll() {
		return requestRepository.findAll();
	}

	public Request findOne(int Id) {
		return requestRepository.findOne(Id);
	}

	public Request save(Request request) {

		Assert.isTrue(LoginService.hasRole("CUSTOMER") || LoginService.hasRole("MANAGER"));

		Request saved = requestRepository.saveAndFlush(request);
		return saved;
	}

	public void delete(Request request) {

		Assert.isTrue(LoginService.hasRole("CUSTOMER"));
		Assert.isTrue(request.getStatus().equals("PENDING"));
		
		if (request.getContract() != null) {
			contractRepository.delete(request.getContract());
		}
		
		requestRepository.delete(request);
	}
	
	public Request trueSave(Request request) {
		
		return requestRepository.save(request);
	}
	
	public void trueDelete(Request request) {
		
		requestRepository.delete(request);
	}
	
	public Collection<Request> getRequestsByCustomer(int id) {
		Collection<Request> requests = requestRepository.getRequestsByCustomer(id);
		return requests;
	}
	
	public Collection<Request> getRequestsByManager(int id) {
		Collection<Request> requests = requestRepository.getRequestsByManager(id);
		return requests;
	}
	
	public Collection<Request> getRequestsByPackage(int id) {
		Collection<Request> requests = requestRepository.getRequestsByPackage(id);
		return requests;
	}
	
	public Collection<Customer> top10CustomersRequestsStatusPending() {

		ArrayList<Customer> res = (ArrayList<Customer>) requestRepository.top10CustomersRequestsStatusPending();

		List<Customer> sub = res;
		if (res.size() > 10) {
			sub = res.subList(0, 9);
		}
		return sub;
	}
	
	public Collection<Customer> top10CustomersRequestsStatusApproved() {

		ArrayList<Customer> res = (ArrayList<Customer>) requestRepository.top10CustomersRequestsStatusApproved();

		List<Customer> sub = res;
		if (res.size() > 10) {
			sub = res.subList(0, 9);
		}
		return sub;
	}
	
	public Collection<Customer> top10CustomersRequestsStatusRejected() {

		ArrayList<Customer> res = (ArrayList<Customer>) requestRepository.top10CustomersRequestsStatusRejected();

		List<Customer> sub = res;
		if (res.size() > 10) {
			sub = res.subList(0, 9);
		}
		return sub;
	}
	
	public Request reconstruct(Request request, BindingResult binding) {
		
		Request result = new Request();
		
		if (request.getId() == 0) {
			
			result = request;
			result.setCustomer(customerService.findByPrincipal());
			result.setStatus("PENDING");
			result.setCommentsManager(request.getCommentsManager());
			result.setCommentsCustomer(request.getCommentsCustomer());
			result.setPakage(request.getPakage());
			
		} else {

			result = requestRepository.findOne(request.getId());
			
			Manager manager = managerService.findByPrincipal();
			Customer customer = customerService.findByPrincipal();
			
			result.setStatus(request.getStatus());
//			result.setCommentsManager(request.getCommentsManager());
//			result.setCommentsCustomer(request.getCommentsCustomer());
			
			if (result.getStatus().equals("0")) {
				result.setStatus("PENDING");
			}
			
			if (manager != null) {
				result.setCommentsCustomer(result.getCommentsCustomer());
				result.setCommentsManager(request.getCommentsManager());
			}
			
			if(customer != null) {
				result.setCommentsCustomer(request.getCommentsCustomer());
				result.setCommentsManager(result.getCommentsManager());
			}
			
			if (result.getStatus().equals("APPROVED")) {

				Contract contract = contractService.create();
				contract.setText(result.getPakage().getTicker() + " " + result.getPakage().getTitle() + " " + result.getPakage().getDescription());
				contract.setPakage(result.getPakage());
				
				Collection<File> files = new HashSet<File>();
				File file = fileService.create(contract);
				files.add(file);
				contract.setFiles(files);
				Contract saved = contractService.save(contract);
				result.setContract(saved);
			}
			
		}
		
		validator.validate(result, binding);
		if (binding.hasErrors()) {
			throw new ValidationException();
		}
		return result;
	}
	
}