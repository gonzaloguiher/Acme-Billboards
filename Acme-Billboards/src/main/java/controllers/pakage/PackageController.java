package controllers.pakage;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import forms.PackageFinderForm;

import services.PackageService;

@Controller
@RequestMapping("/package")
public class PackageController extends AbstractController {

	@Autowired
	private PackageService packageService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView filter(@RequestParam(required = false) Integer managerId) {

		PackageFinderForm form = new PackageFinderForm();

		if(managerId != null) {
			form.setManagerId(managerId);
		}
		
		ModelAndView result = createEditModelAndView(form);
		return result;
	}
	
//	@RequestMapping(value = "/lisr", method = RequestMethod.GET)
//	public ModelAndView filter() {
//
//		PackageFinderForm form = new PackageFinderForm();
//
//		ModelAndView result = createEditModelAndView(form);
//		return result;
//	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, params = "list")
	public ModelAndView list(PackageFinderForm form, final BindingResult binding) {
		
		ModelAndView result;
		
		if (binding.hasErrors()) {
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

	@RequestMapping(value = "/list", method = RequestMethod.POST, params = "cancel")
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
		result.addObject("requestURI", "package/show.do");

		return result;
	}

	// List -------------------------------------------------------------------

//	@RequestMapping(value = "/list", method = RequestMethod.GET)
//	public ModelAndView list() {
//		ModelAndView result;
//
//		Collection<domain.Package> packages = packageService.findPackagesNoDraft();
//
//		result = new ModelAndView("package/list");
//		result.addObject("packages", packages);
//		result.addObject("requestURI", "package/list.do");
//
//		return result;
//	}

	protected ModelAndView createEditModelAndView(PackageFinderForm packageFinderForm) {

		ModelAndView result = createEditModelAndView(packageFinderForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(PackageFinderForm packageFinderForm, String messageCode){

		Collection<domain.Package> packages = new HashSet<domain.Package>();
		
		ModelAndView result = new ModelAndView("package/list");
		
		if(packageFinderForm.getMoment() == null || packageFinderForm.getKeyword() == null || packageFinderForm.getKeyword().equals("")) {
			if(packageFinderForm.getManagerId() == null) packages.addAll(packageService.findPackagesNoDraft());
			else {
				packages.addAll(packageService.getPackagesFinalByManager(packageFinderForm.getManagerId()));
			}
		} else {
			if(packageFinderForm.getManagerId() == null) packages.addAll(packageService.searchPackages(packageFinderForm.getKeyword()));
			else {
				System.out.println("keyword " + packageFinderForm.getKeyword() + " manager " + packageFinderForm.getManagerId());
				packages.addAll(packageService.searchPackagesWithManager(packageFinderForm.getKeyword(), packageFinderForm.getManagerId()));
			}
		}
		
		result.addObject("packageFinderForm", packageFinderForm);
		result.addObject("packages", packages);
		result.addObject("requestURI", "package/list.do");
		result.addObject("message", messageCode);
		return result;
	}
}
