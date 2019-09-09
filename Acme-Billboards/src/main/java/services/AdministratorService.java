package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Administrator;
import forms.AdministratorForm;

import repositories.AdministratorRepository;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class AdministratorService {

	@Autowired
	private AdministratorRepository administratorRepository;

	public Administrator create(UserAccount ua) {

		Administrator res = new Administrator();

		res.setUserAccount(ua);

		return res;
	}

	public Collection<Administrator> findAll() {
		return administratorRepository.findAll();
	}

	public Administrator findOne(int id) {
		return administratorRepository.findOne(id);
	}

	public Administrator save(Administrator administrator) {

		Administrator saved = administratorRepository.saveAndFlush(administrator);
		return saved;
	}

	public void delete(Administrator administrator) {
		administratorRepository.delete(administrator);
	}
	
	public Administrator findByPrincipal() {
		return administratorRepository.getAdministratorByUserAccountId((LoginService.getPrincipal().getId()));
	}
	
	public AdministratorForm rellenaForm(Administrator administrator , AdministratorForm administratorForm , List<String> parts) {
		
		if (parts.contains("personal") || parts.isEmpty()) {
			
			// Personal Data
			
			administratorForm.setName(administrator.getName());
			administratorForm.setMiddleName(administrator.getMiddleName());
			administratorForm.setSurname(administrator.getSurname());
			administratorForm.setAddress(administrator.getAddress());
			administratorForm.setEmail(administrator.getEmail());
			administratorForm.setPhone(administrator.getPhone());
			administratorForm.setPhoto(administrator.getPhoto());
		}
		
		if(parts.contains("account")|| parts.isEmpty()) {
			
			// Account
			
			administratorForm.setPassword("default");
			administratorForm.setPassword2("default");
			administratorForm.setUsername(administrator.getUserAccount().getUsername());
		}
		
		return administratorForm;
	}
}
