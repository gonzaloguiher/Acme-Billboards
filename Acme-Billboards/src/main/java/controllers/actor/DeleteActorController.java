package controllers.actor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import services.AdministratorService;
import services.ContractService;
import services.CreditCardService;
import services.CustomerService;
import services.FinderService;
import services.ManagerService;
import services.PackageService;
import services.RequestService;
import controllers.AbstractController;
import domain.Administrator;
import domain.Contract;
import domain.CreditCard;
import domain.Customer;
import domain.Finder;
import domain.Manager;
import domain.Request;

@Controller
@RequestMapping("actor/")
public class DeleteActorController extends AbstractController {

	// Services ----------------------------------------------------------------

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private ContractService contractService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private PackageService packageService;
	
	@Autowired
	private RequestService requestService;
	
	@Autowired
	private FinderService finderService;
	
	@Autowired
	private CreditCardService creditCardService;
	

	// DELETE ALL DATA --------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView deleteActor() {

		ModelAndView res;

		Collection<Authority> auths = LoginService.getPrincipal().getAuthorities();
		
		Authority adminAuth    = new Authority();
		Authority customerAuth = new Authority();
		Authority managerAuth  = new Authority();

		adminAuth.setAuthority(Authority.ADMIN);
		customerAuth.setAuthority(Authority.CUSTOMER);
		managerAuth.setAuthority(Authority.MANAGER);

		if (auths.contains(adminAuth)) {
			res = this.deleteAdministrator();
		} else if (auths.contains(customerAuth)) {
			res = this.deleteCustomer();
		} else if (auths.contains(managerAuth)) {
			res = this.deleteManager();
		} else {
			res = new ModelAndView("error/access");
		}

		return res;
	}

	// Customer ---------------------------------------------------------------

	@RequestMapping(value = "/deleteCustomerData", method = RequestMethod.GET)
	public ModelAndView deleteCustomer() {

		ModelAndView result = new ModelAndView("redirect:/j_spring_security_logout");

		Customer customer = customerService.findByPrincipal();
		Collection<Request> requests = requestService.getRequestsByCustomer(customer.getId());

		for (Request request : requests) {
			request.setPakage(null);
			request.setContract(null);
			Request saved = requestService.trueSave(request);
			requestService.trueDelete(saved);
			System.out.println("deleted " + saved);
		}

		CreditCard creditCard = customer.getCreditCard();
		System.out.println(creditCard);

		customer.setCreditCard(null);
		customerService.save(customer);

		if (creditCard != null) {
			creditCardService.delete(creditCard);
			System.out.println("deleted " + creditCard);
		}

		UserAccount ua = customer.getUserAccount();

		Finder finder = finderService.findByCustomer(customer);
		if (finder != null) {
			finderService.delete(finder);
		}
		System.out.println("deleted " + finder);
		
		customerService.delete(customer);
		userAccountService.delete(ua);

		return result;
	}

	// Manager ----------------------------------------------------------------

	@RequestMapping(value = "/deleteManagerData", method = RequestMethod.GET)
	public ModelAndView deleteManager() {

		ModelAndView result = new ModelAndView("redirect:/j_spring_security_logout");

		Manager manager = managerService.findByPrincipal();

		Collection<domain.Package> packages = manager.getPackages();

		Collection<Request> requests = requestService.getRequestsByManager(manager.getId());
		
		for (Request request: requests) {
			request.setPakage(null);
			request.setContract(null);
			Request saved = requestService.trueSave(request);
			requestService.trueDelete(saved);
			System.out.println("deleted " + saved);
			
		}
		
		Collection<Contract> contracts = contractService.getContractsByManager(manager.getId());
		
		for (Contract contract: contracts) {
			
			contractService.trueDelete(contract);
			System.out.println("deleted " + contract);
		}
		
		for (domain.Package pakage: packages) {
			
			packageService.trueDelete(pakage);
			System.out.println("deleted " + pakage);
		}

		UserAccount ua = manager.getUserAccount();

		managerService.delete(manager);
		userAccountService.delete(ua);

		return result;
	}

	// Administrator ----------------------------------------------------------
	
	@RequestMapping(value = "/deleteAdministratorData", method = RequestMethod.GET)
	public ModelAndView deleteAdministrator() {

		ModelAndView res = new ModelAndView("redirect:/j_spring_security_logout");

		Administrator administrator = administratorService.findByPrincipal();

		UserAccount ua = administrator.getUserAccount();

		administratorService.delete(administrator);
		userAccountService.delete(ua);

		return res;
	}

}