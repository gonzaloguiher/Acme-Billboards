package services;

import java.util.Collection;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Contract;
import domain.File;

import repositories.FileRepository;
import security.LoginService;

@Service
@Transactional
public class FileService {

	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private Validator validator;

	public File create(Contract contract) {
		
		Assert.isTrue(LoginService.hasRole("MANAGER"));
		
		File result = new File();
		result.setContract(contract);
		return result;
	}
	
	public File save(File file) {
		
		Assert.isTrue(LoginService.hasRole("MANAGER"));
		
		return fileRepository.saveAndFlush(file);
	}

	public void delete(File file) {
		
		Assert.isTrue(LoginService.hasRole("MANAGER"));
		
		fileRepository.delete(file);
		fileRepository.flush();
	}
	
	public void trueDelete(File file) {
		fileRepository.delete(file);
	}
	
	public Collection<File> findAll() {
		return fileRepository.findAll();
	}

	public File findOne(int Id) {
		return fileRepository.findOne(Id);
	}
	
	public void flush() {
		this.fileRepository.flush();
	}

	public File reconstruct(File file, BindingResult binding) {
		
		File result;

		if (file.getId() == 0) {
			
			result = this.create(file.getContract());
			result.setLocation(file.getLocation());
			result.setImage(file.getImage());

		} else {
			
			result = this.findOne(file.getId());
			result.setLocation(file.getLocation());
			result.setImage(file.getImage());
		}

		validator.validate(file, binding);
		if (binding.hasErrors()) {
			throw new ValidationException();
		}

		return result;
	}
}
