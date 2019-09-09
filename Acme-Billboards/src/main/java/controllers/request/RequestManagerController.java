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
import services.ManagerService;
import services.RequestService;

import controllers.AbstractController;
import domain.Contract;
import domain.Manager;
import domain.Request;

@Controller
@RequestMapping("/request/manager")
public class RequestManagerController extends AbstractController {

	@Autowired
	private RequestService requestService;

	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private ContractService contractService;

	// Show-------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int requestId) {

		ModelAndView result;

		Request request = requestService.findOne(requestId);

		result = new ModelAndView("request/show");
		result.addObject("request", request);
		result.addObject("requestURI", "request/manager/show.do");
		return result;
	}

	// List -------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		Manager manager = managerService.findByPrincipal();
		Collection<Request> requests = requestService.getRequestsByManager(manager.getId());

		result = new ModelAndView("request/list");
		result.addObject("requests", requests);

		return result;
	}

	// Edit-------------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int requestId) {
		ModelAndView result;

		Request request = requestService.findOne(requestId);
		Manager logged = managerService.findByPrincipal();

		if (logged.equals(request.getPakage().getManager()) && request.getStatus().equals("PENDING")) {
			result = createEditModelAndView(request);
		} else {
			result = new ModelAndView("error/access");
		}

		return result;
	}

	// Save -----------------------------------------------------------------

	@RequestMapping(value = "/edit", params = "save", method = RequestMethod.POST)
	public ModelAndView edit(final Request request, final BindingResult bindingResult) {
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
				this.requestService.save(res);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(request, "request.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(Request request) {
		ModelAndView result;
		result = createEditModelAndView(request, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(Request request, String messageCode) {
		
		ModelAndView result = new ModelAndView("request/edit");
		
		Boolean condition = false;
		
		if (request.getStatus().equals("REJECTED") && request.getCommentsManager() == null) {
			condition = true;
		}
		
		Contract contract = contractService.getContractByPackage(request.getPakage().getId());
		
		if (contract == null) {
				
			Collection<String> statuses = new HashSet<String>();
			statuses.add("APPROVED"); 
			statuses.add("REJECTED");
			result.addObject("statuses", statuses);
			
		} else {
			
			Collection<String> statuses = new HashSet<String>();
			statuses.add("REJECTED");
			result.addObject("statuses", statuses);
		}
		
		result.addObject("request", request);
		result.addObject("message", messageCode);
		result.addObject("condition", condition);
		return result;
	}
}
