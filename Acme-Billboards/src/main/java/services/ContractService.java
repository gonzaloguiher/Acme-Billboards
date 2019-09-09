package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Contract;
import domain.Customer;
import domain.File;
import domain.Manager;
import domain.Request;

import repositories.ContractRepository;
import security.LoginService;

@Service
@Transactional
public class ContractService {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private RequestService requestService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ContractRepository contractRepository;

	@Autowired
	private Validator validator;

	public Contract create() {
		
		Assert.isTrue(LoginService.hasRole("MANAGER"));
		
		Contract result = new Contract();
		
		Collection<File> files = new HashSet<File>();
		
		result.setStatus("DRAFT");
		result.setFiles(files);
		return result;
	}

	public Collection<Contract> findAll() {
		return contractRepository.findAll();
	}

	public Contract findOne(int Id) {
		return contractRepository.findOne(Id);
	}
	
	public Collection<Contract> getContractsByManager(int id) {
		Collection<Contract> contracts = contractRepository.getContractsByManager(id);
		return contracts;
	}
	
	public Contract getContractByPackage(int id) {
		Contract contract = contractRepository.getContractByPackage(id);
		return contract;
	}
	
	public Contract getContractByFile(int id) {
		Contract contract = contractRepository.findContractByFileId(id);
		return contract;
	}

	public Contract save(Contract contract) {
		
		Assert.isTrue(LoginService.hasRole("CUSTOMER") || LoginService.hasRole("MANAGER"));

		Contract saved = contractRepository.saveAndFlush(contract);

		return saved;
	}

	public void delete(Contract contract) {

		Assert.isTrue(LoginService.hasRole("MANAGER"));
		Assert.isTrue(contract.getStatus().equals("DRAFT"));
		
		Collection<File> files = contract.getFiles();
		
		for(File file: files) {
			fileService.delete(file);
		}
		
		contractRepository.delete(contract);
	}
	
	public void trueDelete(Contract contract) {
		
		contractRepository.delete(contract);
	}
	
	public void testDelete(Contract contract) {
		
		Manager manager = managerService.findByPrincipal();
		
		Collection<Request> requests = requestService.getRequestsByManager(manager.getId());
		
		for (Request request: requests) {
			request.setPakage(null);
			request.setContract(null);
			Request saved = requestService.trueSave(request);
			requestService.trueDelete(saved);
			System.out.println("deleted " + saved);
			
		}
		
		if (contract.getFiles().size() != 0) {
			File[] files = new File[contract.getFiles().size()];
			Integer cont = 0;
			for (File f : contract.getFiles()) {
				files[cont] = f;
				cont++;
			}
			
			for (int i = 0; i < files.length; i++) {
				fileService.delete(files[i]);
				System.out.println("deleted "+ files[i]);
			}
		}
		contractRepository.delete(contract);
	}
	
	public Contract reconstruct(Contract contract, BindingResult binding) {
		
		Contract result = new Contract();
		
		if (contract.getId() == 0) {
			result = contract;
			
		} else {
			
			Date momentCustomer = new Date(System.currentTimeMillis());
			Date momentManager  = new Date(System.currentTimeMillis());
			
			Manager manager = managerService.findByPrincipal();
			Customer customer = customerService.findByPrincipal();
			
			System.out.println("Manager: " + manager);
			System.out.println("Customer: " + customer);
			
			result = contractRepository.findOne(contract.getId());
			
			result.setText(contract.getText());
//			result.setHash(contract.getHash());
			
			if(contract.getSignCustomer().isEmpty() == false && customer != null) {
				
				result.setSignCustomer(contract.getSignCustomer());
				result.setMomentCustomer(momentCustomer);
				result.setStatus("FINAL");
			}
			
			if(contract.getSignManager().isEmpty() == false && manager != null) {
				
				Md5PasswordEncoder encoder = new Md5PasswordEncoder();
				String pass = encoder.encodePassword(result.getHash(), null);
				
				result.setHash(pass);
				result.setSignManager(contract.getSignManager());
				result.setMomentManager(momentManager);
			}
			
		}
		validator.validate(result, binding);
		if (binding.hasErrors()) {
			throw new ValidationException();
		}
		return result;
	}
}