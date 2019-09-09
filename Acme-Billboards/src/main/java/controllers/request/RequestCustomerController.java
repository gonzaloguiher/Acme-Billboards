package controllers.request;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ContractService;
import services.CustomerService;
import services.PackageService;
import services.RequestService;

import controllers.AbstractController;
import domain.Contract;
import domain.Customer;
import domain.Request;

@Controller
@RequestMapping("/request/customer")
public class RequestCustomerController extends AbstractController {

	@Autowired
	private RequestService requestService;
	
	@Autowired
	private PackageService packageService;
	
	@Autowired
	private ContractService contractService;

	@Autowired
	private CustomerService customerService;

	// Show-------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int requestId) {

		ModelAndView result;

		Request request = requestService.findOne(requestId);

		result = new ModelAndView("request/show");
		result.addObject("request", request);
		result.addObject("requestURI", "request/customer/show.do");

		return result;
	}

	// List -------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		Customer customer = customerService.findByPrincipal();
		Collection<Request> requests = requestService.getRequestsByCustomer(customer.getId());
		
		result = new ModelAndView("request/list");
		result.addObject("requests", requests);

		return result;
	}

	// Create -----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;

		Request request = requestService.create();

		result = this.createEditModelAndView(request);
		return result;
	}

	// Edit----------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int requestId) {
		ModelAndView result;

		Request request = requestService.findOne(requestId);
		Customer logged = customerService.findByPrincipal();

		if (logged.equals(request.getCustomer()) && request.getStatus().equals("PENDING")) {
			result = createEditModelAndView(request);
		} else {
			result = new ModelAndView("error/access");
		}

		return result;
	}

	// Save -----------------------------------------------------------------

	@RequestMapping(value = "/edit", params = "save", method = RequestMethod.POST)
	public ModelAndView edit(final Request request,
			final BindingResult bindingResult) {
		ModelAndView result;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(request);
		else
			try {
				if (request.getId() != 0) {
					Request db = requestService.findOne(request.getId());
					if (db.getStatus().equals("APPROVED") || db.getStatus().equals("REJECTED")) {
						bindingResult.rejectValue("request", "request.commit.error");
					}
				}
				Request res = requestService.reconstruct(request, bindingResult);
				if (res.getPakage().equals(null)) {
					result = new ModelAndView("redirect:list.do");
				}
				this.requestService.save(res);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(request, "request.commit.error");
			}
		return result;
	}

	// Delete -----------------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int requestId) {
		ModelAndView result;
		Request request = requestService.findOne(requestId);

		if (request.getCustomer().equals(customerService.findByPrincipal()) && request.getStatus().equals("PENDING")) {
			try {
				requestService.delete(request);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(request, "request.commit.error");
			}
		} else
			result = new ModelAndView("error/access");

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Request request, final BindingResult binding) {
		ModelAndView result;

		if (request.getCustomer().equals(customerService.findByPrincipal()) && request.getStatus().equals("PENDING")) {
			try {
				requestService.delete(request);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(request, "request.commit.error");
			}
		} else {
			result = new ModelAndView("error/access");
		}

		return result;
	}

	// Helper methods---------------------------------------------------

	protected ModelAndView createEditModelAndView(Request request) {
		ModelAndView res;
		res = createEditModelAndView(request, null);
		return res;
	}

	protected ModelAndView createEditModelAndView(Request request, String messageCode) {
		ModelAndView res;
		
		Collection<Contract> contracts = contractService.findAll();
		Collection<domain.Package> pakages = packageService.findPackagesNoDraft();
		Collection<domain.Package> packages = new HashSet<domain.Package>();
		Collection<domain.Package> packages2 = new HashSet<domain.Package>();
		
		for(Contract contract: contracts) {
			for(domain.Package pakage: pakages) {
				if(contract.getPakage().equals(pakage)) {
					packages2.add(pakage);
				}
			}
		}
		
		pakages.removeAll(packages2);
		packages.addAll(pakages);
		
		res = new ModelAndView("request/edit");
		res.addObject("request", request);
		res.addObject("packages", packages);
		res.addObject("message", messageCode);

		return res;
	}
}
