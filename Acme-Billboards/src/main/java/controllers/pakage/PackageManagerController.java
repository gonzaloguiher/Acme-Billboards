package controllers.pakage;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ManagerService;
import services.PackageService;

import controllers.AbstractController;
import domain.Manager;
import forms.PackageFinderForm;

@Controller
@RequestMapping("/package/manager")
public class PackageManagerController extends AbstractController {

	@Autowired
	private PackageService packageService;

	@Autowired
	private ManagerService managerService;

	@RequestMapping(value="/list", method= RequestMethod.GET)
	public ModelAndView filter() {
		
		ModelAndView result;
		PackageFinderForm form = new PackageFinderForm();
		
		if(LoginService.hasRole("MANAGER")) {
			form.setManagerId(managerService.findByPrincipal().getId());
		}
		
		result = createEditModelAndView(form);
		return result;
	}

	@RequestMapping(value="/list", method= RequestMethod.POST, params = "list")
	public ModelAndView list(PackageFinderForm form, final BindingResult binding) {
		
		ModelAndView result;
		
		if(binding.hasErrors()) {
			result = createEditModelAndView(form);
		} else {
			try {
				form.setMoment(new Date());
				result = createEditModelAndView(form);
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = createEditModelAndView(form, "package.commit.error");
			}
		}
		return result;
	}
	
	@RequestMapping(value="/list", method= RequestMethod.POST, params = "cancel")
	public ModelAndView cancel(PackageFinderForm form, final BindingResult binding) {
		
		ModelAndView result;
			try {
				form.setMoment(null);
				form.setKeyword(null);
				result = createEditModelAndView(form);
			} catch (final Throwable oops) {
				oops.printStackTrace();
				result = createEditModelAndView(form, "package.commit.error");
			}
		return result;
	}
	
	// Show-------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int packageId) {

		ModelAndView result;

		domain.Package pakage = packageService.findOne(packageId);
		
		result = new ModelAndView("package/show");
		result.addObject("pakage", pakage);
		result.addObject("requestURI", "package/manager/show.do");

		return result;
	}

	// Create -----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;

		domain.Package pakage = packageService.create();

		result = this.createEditModelAndView(pakage);
		return result;
	}

	// Edit-----------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int packageId) {
		ModelAndView result;

		domain.Package pakage = packageService.findOne(packageId);
		Manager logged = managerService.findByPrincipal();

		if (logged.equals(pakage.getManager()) && pakage.getIsDraft().equals(true)) {
			result = createEditModelAndView(pakage);
		} else {
			result = new ModelAndView("error/access");
		}

		return result;
	}

	// Save -----------------------------------------------------------------

	@RequestMapping(value = "/edit", params = "save", method = RequestMethod.POST)
	public ModelAndView edit(@ModelAttribute("pakage") final domain.Package pakage, final BindingResult bindingResult) {
		ModelAndView result;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(pakage);
		else
			try {
				if (pakage.getId() != 0) {
					domain.Package db = packageService.findOne(pakage.getId());
					if (db.getIsDraft().equals(false)) {
						bindingResult.rejectValue("package", "package.commit.error");
					}
				}
				domain.Package res = this.packageService.reconstruct(pakage, bindingResult);
				this.packageService.save(res);
				result = new ModelAndView("redirect:list.do");
			} catch (final ValidationException oops) {
				System.out.println(oops.getStackTrace());
				oops.printStackTrace();
				result = this.createEditModelAndView(pakage, "package.commit.error");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(pakage, "package.commit.error");
			}
		return result;
	}

	// Delete -----------------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int packageId) {
		ModelAndView result;

		domain.Package pakage = packageService.findOne(packageId);

		if (pakage.getManager().equals(managerService.findByPrincipal()) && pakage.getIsDraft().equals(true)) {
			try {
				packageService.delete(pakage);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(pakage, "package.commit.error");
			}
		} else
			result = new ModelAndView("error/access");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final domain.Package pakage, final BindingResult binding) {
		
		ModelAndView result;

		if (pakage.getManager().equals(managerService.findByPrincipal()) && pakage.getIsDraft().equals(true)) {
			try {
				packageService.delete(pakage);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(pakage, "package.commit.error");
			}
		} else 
			result = new ModelAndView("error/access");

		return result;
	}

	protected ModelAndView createEditModelAndView(domain.Package pakage) {
		ModelAndView res;

		res = createEditModelAndView(pakage, null);
		return res;
	}

	protected ModelAndView createEditModelAndView(domain.Package pakage, String messageCode) {

		ModelAndView res;

		res = new ModelAndView("package/edit");
		res.addObject("pakage", pakage);
		res.addObject("message", messageCode);

		return res;
	}
	
	protected ModelAndView createEditModelAndView(PackageFinderForm packageFinderForm) {
		
		ModelAndView result = createEditModelAndView(packageFinderForm, null);
		return result;
	}
	
	protected ModelAndView createEditModelAndView(PackageFinderForm packageFinderForm, String messageCode) {
		
		Collection<domain.Package> packages = new HashSet<domain.Package>();
		
		ModelAndView result = new ModelAndView("package/list");
		
		if(packageFinderForm.getMoment() == null || packageFinderForm.getKeyword() == null || packageFinderForm.getKeyword().equals("")){

			if(packageFinderForm.getManagerId() == null){
				packages.addAll(packageService.findPackagesNoDraft());
			}
			else packages.addAll(packageService.getPackagesByManager(packageFinderForm.getManagerId()));
		} else {

			if(packageFinderForm.getManagerId() == null) packages.addAll(packageService.searchPackages(packageFinderForm.getKeyword()));
			else packages.addAll(packageService.searchPackagesWithManager(packageFinderForm.getKeyword(), packageFinderForm.getManagerId()));
		}				
		
		result.addObject("packages", packages);
		result.addObject("packageFinderForm", packageFinderForm);
		result.addObject("requestURI", "package/manager/list.do");
		result.addObject("message", messageCode);
		return result;
	}
	
}
