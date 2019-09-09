package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Actor;
import domain.Administrator;
import domain.CreditCard;
import domain.Customer;
import domain.Manager;
import forms.ProfileForm;
import forms.RegisterAdministratorForm;
import forms.RegisterCustomerForm;
import forms.RegisterManagerForm;

@Service
@Transactional
public class ActorService {

	@Autowired
	private ActorRepository actorRepository;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private Validator validator;

	public Actor save(Actor actor) {

		Actor result;

		result = actorRepository.saveAndFlush(actor);
		return result;
	}

	public void delete(Actor actor) {

		actorRepository.delete(actor);
	}

	public Collection<Actor> findAll() {
		return actorRepository.findAll();
	}

	public Actor findOne(int Id) {
		return actorRepository.findOne(Id);
	}

	public Actor getByUserAccount(UserAccount userAccount) {
		return actorRepository.findByUserAccount(userAccount);
	}
	
	public Administrator registerAdministrator(Administrator administrator) {
		
		Authority administratorAuthority = new Authority();
		administratorAuthority.setAuthority(Authority.ADMIN);
		
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(administratorAuthority));

		UserAccount savedUserAccount =  userAccountService.save(administrator.getUserAccount());
		
		System.out.println("la contraseña de la useraccount persistida es :" + savedUserAccount.getPassword());
		
		administrator.setUserAccount(savedUserAccount);
		
		Administrator saved = administratorService.save(administrator);
		
		return saved;
	}
	
	public Customer registerCustomer(Customer customer) {

		UserAccount savedUserAccount =  userAccountService.save(customer.getUserAccount());
		CreditCard savedCredit = creditCardService.save(customer.getCreditCard());
		
		System.out.println("la contraseña de la useraccount persistida es :" + savedUserAccount.getPassword());
		
		customer.setUserAccount(savedUserAccount);
		customer.setCreditCard(savedCredit);
		Customer saved = customerService.save(customer);
		
		return saved;
	}
	
	public Manager registerManager(Manager manager) {
		
		Authority administratorAuthority = new Authority();
		administratorAuthority.setAuthority(Authority.ADMIN);
		
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(administratorAuthority));
		
		UserAccount savedUserAccount =  userAccountService.save(manager.getUserAccount());
		
		System.out.println("la contraseña de la useraccount persistida es :" + savedUserAccount.getPassword());
		
		manager.setUserAccount(savedUserAccount);
		
		Manager saved = managerService.save(manager);
		
		return saved;
	}
	
	public Administrator reconstructAdministrator(RegisterAdministratorForm form, BindingResult binding ){
		
		//Creamos la cuenta de usuario.
		
		UserAccount ua = userAccountService.create();
		
		ua.setPassword(form.getPassword());
		ua.setUsername(form.getUsername());
		
		// Le asignamos la authority cosrrespondiente.
		
		Authority authority = new Authority();
		authority.setAuthority("ADMIN");
		ua.addAuthority(authority);
		
		// Creamos el actor con la useraccount sin persistir.
		
		Administrator actor = administratorService.create(ua);
		
		actor.setAddress(form.getAddress());
		actor.setEmail(form.getEmail());
		actor.setName(form.getName());
		actor.setPhone(form.getPhone());
		actor.setPhoto(form.getPhoto());
		actor.setMiddleName(form.getMiddleName());
		actor.setSurname(form.getSurname());
		
		validator.validate(form, binding);		
		
		if (form.getPassword().equals(form.getPassword2()) == false) {
			ObjectError error = new ObjectError(form.getPassword(), "password does not match");
			binding.addError(error);
		}		
		return actor;
	
	}

	public Customer reconstructCustomer(RegisterCustomerForm form, BindingResult binding ){
		
		//Creamos la tarjeta de crédito:
		
		CreditCard credit = creditCardService.create();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(form.getExpirationYear(), form.getExpirationMonth(), 1);
		Date date = calendar.getTime();
		
		credit.setCVV(form.getCVV());
		credit.setExpirationDate(date);
		credit.setHolder(form.getHolder());
		credit.setMake(form.getMake());
		credit.setNumber(form.getNumber());
		
		//Creamos la cuenta de usuario.
		
		UserAccount ua = userAccountService.create();
		
		ua.setPassword(form.getPassword());
		ua.setUsername(form.getUsername());
		
		// Le asignamos la authority cosrrespondiente.
		
		Authority authority = new Authority();
		authority.setAuthority("CUSTOMER");
		ua.addAuthority(authority);
		
		// Creamos el actor con la useraccount sin persistir.
		
		Customer actor = customerService.create(ua);
		
		actor.setAddress(form.getAddress());
		actor.setCreditCard(credit);
		actor.setEmail(form.getEmail());
		actor.setName(form.getName());
		actor.setPhone(form.getPhone());
		actor.setPhoto(form.getPhoto());
		actor.setMiddleName(form.getMiddleName());
		actor.setSurname(form.getSurname());
		actor.setVatNumber(form.getVatNumber());
		
		validator.validate(form, binding);		
		
		if (form.getPassword().equals(form.getPassword2()) == false) {
			ObjectError error = new ObjectError(form.getPassword(), "password does not match");
			binding.addError(error);
		}		
		return actor;
	
	}
	
	public Manager reconstructManager(RegisterManagerForm form, BindingResult binding) {
		
		//Creamos la cuenta de usuario.
		
		UserAccount ua = userAccountService.create();
		
		ua.setPassword(form.getPassword());
		ua.setUsername(form.getUsername());
		
		// Le asignamos la authority cosrrespondiente.
		
		Authority authority = new Authority();
		authority.setAuthority("MANAGER");
		ua.addAuthority(authority);
		
		// Creamos el actor con la useraccount sin persistir.
		
		Manager actor = managerService.create(ua);
		
		actor.setAddress(form.getAddress());
		actor.setEmail(form.getEmail());
		actor.setName(form.getName());
		actor.setPhone(form.getPhone());
		actor.setPhoto(form.getPhoto());
		actor.setMiddleName(form.getMiddleName());
		actor.setSurname(form.getSurname());
		
		validator.validate(form, binding);		
		
		if (form.getPassword().equals(form.getPassword2()) == false) {
			ObjectError error = new ObjectError(form.getPassword(), "password does not match");
			binding.addError(error);
		}		
		return actor;
	
	}
	
	@SuppressWarnings("deprecation")
	public ProfileForm rellenaForm (Actor actor , ProfileForm profileForm , List<String> parts, BindingResult binding) {
		
		Authority authority = new Authority();
		authority.setAuthority(Authority.CUSTOMER);
		
		if (parts.contains("personal") || parts.isEmpty()) {
			
			// Personal Data
			
			if (actor.getUserAccount().getAuthorities().contains(authority)) {
				Customer customer = (Customer) actor;
				profileForm.setVatNumber(customer.getVatNumber());
				
			} else {
				profileForm.setVatNumber("default");
			}
			profileForm.setName(actor.getName());
			profileForm.setMiddleName(actor.getMiddleName());
			profileForm.setSurname(actor.getSurname());
			profileForm.setAddress(actor.getAddress());
			profileForm.setEmail(actor.getEmail());
			profileForm.setPhone(actor.getPhone());
			profileForm.setPhoto(actor.getPhoto());
		}
		
		if(parts.contains("credit") || parts.isEmpty()) {
			
			// Credit Card
			
			Customer customer = (Customer) actor;
			
			profileForm.setCVV(customer.getCreditCard().getCVV());
			profileForm.setExpirationMonth(customer.getCreditCard().getExpirationDate().getMonth());
			profileForm.setExpirationYear(customer.getCreditCard().getExpirationDate().getYear()+1900);
			profileForm.setHolder(customer.getCreditCard().getHolder());
			profileForm.setMake(customer.getCreditCard().getMake());
			profileForm.setNumber(customer.getCreditCard().getNumber());
		}
		
		if(parts.contains("account")|| parts.isEmpty()) {
			
			// Account
			
			profileForm.setPassword("default");
			profileForm.setPassword2("default");
			profileForm.setUsername(actor.getUserAccount().getUsername());
		}
		
		validator.validate(profileForm, binding);	
		if (binding.hasErrors()) {
			System.out.println("Prueba: ");
			System.out.println(binding.getFieldErrors());
			throw new ValidationException();
		}
		
		return profileForm;
	}
	
	public Actor findByUsername (String username){
		return actorRepository.findByUsername(username);
	}
	
	public Actor findActorLogged() {

		UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		Actor result = this.actorRepository.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}
	
	public StringBuilder exportData() {
		final Actor actorLogged = this.findActorLogged();

		final StringBuilder sb = new StringBuilder();
		sb.append(actorLogged.getName());
		sb.append(';');
		sb.append(actorLogged.getSurname());
		sb.append(';');
		sb.append(actorLogged.getMiddleName());
		sb.append(';');
		sb.append(actorLogged.getEmail());
		sb.append(';');
		sb.append(actorLogged.getPhone());
		sb.append(';');
		sb.append(actorLogged.getPhoto());
		sb.append(';');
		sb.append(actorLogged.getAddress());
		if (actorLogged instanceof Customer) {
			sb.append(';');
			sb.append(((Customer) actorLogged).getVatNumber());
			sb.append(';');
			sb.append(((Customer) actorLogged).getCreditCard().getHolder());
			sb.append(';');
			sb.append(((Customer) actorLogged).getCreditCard().getMake());
			sb.append(';');
			sb.append(((Customer) actorLogged).getCreditCard().getNumber());
			sb.append(';');
			sb.append(((Customer) actorLogged).getCreditCard().getExpirationDate());
			sb.append(';');
			sb.append(((Customer) actorLogged).getCreditCard().getCVV());
		}
		sb.append('\n');
		return sb;
	}	
	
}
