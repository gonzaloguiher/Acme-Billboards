package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.ManagerRepository;
import domain.Manager;

@Component
@Transactional
public class StringToManagerConverter implements Converter<String, Manager> {

	@Autowired
	ManagerRepository repository;

	@Override
	public Manager convert(String s) {
		Manager res;
		int id;

		try {
			if (StringUtils.isEmpty(s))
				res = null;
			else {
				id = Integer.valueOf(s);
				res = repository.findOne(id);
			}
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return res;
	}

}
