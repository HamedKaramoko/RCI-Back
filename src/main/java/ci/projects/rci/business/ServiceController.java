/**
 * 
 */
package ci.projects.rci.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ci.projects.rci.dao.ServiceDAO;
import ci.projects.rci.model.Service;

/**
 * @author hamedkaramoko
 *
 */
@RestController
@RequestMapping("/service")
public class ServiceController {
	
	@Autowired
	private ServiceDAO serviceDAO;

	@Transactional
	@RequestMapping(method=RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Service> saveService(Service serviceToSave) {
		serviceToSave.setId(0);
		Service serviceSaved = serviceDAO.save(serviceToSave);
		return new ResponseEntity<Service>(serviceSaved, HttpStatus.CREATED);
	}

	@Transactional
	@RequestMapping(method=RequestMethod.PUT, consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Service> updateService(Service serviceToUpdate) {
		Service serviceUpdated = serviceDAO.update(serviceToUpdate);
		return new ResponseEntity<Service>(serviceUpdated, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<String> deleteService(@PathVariable("id") long idServiceToDelete) {
		Service serviceDeleted = serviceDAO.delete(idServiceToDelete);
		return new ResponseEntity<String>(serviceDeleted.getLabel(), HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Service> getService(@PathVariable("id") long id) {
		Service serviceGetted = serviceDAO.get(id);
		HttpStatus httpStatus = serviceGetted != null ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
		return new ResponseEntity<Service>(serviceGetted, httpStatus);
	}

	@Transactional
	@RequestMapping(method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Service>> getAllService() {
		List<Service> servicesGetted = serviceDAO.getAll();
		HttpStatus httpStatus = (servicesGetted != null && !servicesGetted.isEmpty()) ? HttpStatus.FOUND : HttpStatus.NOT_FOUND;
		return new ResponseEntity<List<Service>>(servicesGetted, httpStatus);
	}

}
