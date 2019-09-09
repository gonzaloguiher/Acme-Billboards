package services;

import java.util.Collection;
import java.util.Random;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Manager;
import domain.Request;

import repositories.PackageRepository;
import repositories.RequestRepository;
import security.LoginService;

@Service
@Transactional
public class PackageService {

	@Autowired
	private ManagerService managerService;

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private PackageRepository packageRepository;
	
	@Autowired
	private Validator validator;

	public domain.Package create() {
		
		Assert.isTrue(LoginService.hasRole("MANAGER"));

		domain.Package result = new domain.Package();

		Manager manager = managerService.findByPrincipal();

		result.setIsDraft(true);
		result.setManager(manager);
		result.setTicker(this.generateTicker());

		return result;
	}

	public Collection<domain.Package> findAll() {

		Collection<domain.Package> result = this.packageRepository.findAll();
		return result;
	}
	
	public Collection<domain.Package> findPackagesIsDraft() {
		return packageRepository.getPackagesIsDraft();
	} 
	
	public Collection<domain.Package> findPackagesNoDraft() {
		return packageRepository.getPackagesNoDraft();
	} 
	
	public Collection<domain.Package> getPackagesByManager(int id) {
		return packageRepository.getPackagesByManager(id);
	}
	
	public Collection<domain.Package> getPackagesFinalByManager(int id) {
		return packageRepository.getPackagesFinalByManager(id);
	}
	
	public Collection<domain.Package> getPackagesDraftByManager(int id) {
		return packageRepository.getPackagesDraftByManager(id);
	}
	
	public Collection<domain.Package> searchPackages(String keyword){
		System.out.println("service search " + packageRepository.searchPackages(keyword).size());
		return packageRepository.searchPackages(keyword);
	}
	
	public Collection<domain.Package> searchPackagesWithManager(String keyword, Integer managerId){
		return packageRepository.searchPackagesWithManager(keyword, managerId);
	}

	public domain.Package findOne(Integer packageId) {
		domain.Package result;

		result = this.packageRepository.findOne(packageId);

		return result;
	}

	public domain.Package save(domain.Package pakage) { 
		
		Assert.isTrue(LoginService.hasRole("MANAGER"));
		
		return packageRepository.saveAndFlush(pakage);

	}

	public void delete(domain.Package pakage) {
		
		Assert.isTrue(LoginService.hasRole("MANAGER"));
		Assert.isTrue(pakage.getIsDraft() == true);
		
		Collection<Request> requests = requestRepository.getRequestsByPackage(pakage.getId());
		
		for (Request request : requests) {
			requestRepository.delete(request);
		}
		this.packageRepository.delete(pakage);
	}
	
	public void trueDelete(domain.Package pakage) {
		packageRepository.delete(pakage);
	}
	
	public domain.Package reconstruct(domain.Package pakage, BindingResult binding) {
		domain.Package result;
		
		if(pakage.getId() == 0) {
			
			result = this.create();
			result.setIsDraft(true);
			result.setId(pakage.getId());
			result.setManager(managerService.findByPrincipal());
			result.setTicker(this.generateTicker());
			result.setTitle(pakage.getTitle());
			result.setDescription(pakage.getDescription());
			result.setStartMoment(pakage.getStartMoment());
			result.setEndMoment(pakage.getEndMoment());
			result.setPhoto(pakage.getPhoto());
			result.setPrice(pakage.getPrice());
			
		} else {
			
			result = packageRepository.findOne(pakage.getId());
			result.setId(pakage.getId());
			result.setTitle(pakage.getTitle());
			result.setDescription(pakage.getDescription());
			result.setStartMoment(pakage.getStartMoment());
			result.setEndMoment(pakage.getEndMoment());
			result.setPhoto(pakage.getPhoto());
			result.setPrice(pakage.getPrice());
			result.setIsDraft(pakage.getIsDraft());
		}
		
		validator.validate(result, binding);
		if (binding.hasErrors()) {
			System.out.println(binding.getFieldErrors());
			throw new ValidationException();
		}
		
		return result;
		
	}

	private String generateTicker() {
		String ticker = "";
		ticker = randomNumber() + randomWord() + randomNumber() + "-"
				+ randomWord2();

		return ticker;
	}

	private String randomWord() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 3) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	private String randomWord2() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 5) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}

	private String randomNumber() {
		String SALTCHARS = "0123456789";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 2) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}
}
