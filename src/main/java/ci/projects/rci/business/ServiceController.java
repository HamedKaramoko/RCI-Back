/**
 * 
 */
package ci.projects.rci.business;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ci.projects.rci.dao.ServiceDAO;
import ci.projects.rci.model.Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author hamedkaramoko
 *
 */
@RestController
@Api(value = "ServiceController", description = "REST APIs related to Service Entity!!!!")
@RequestMapping("/service")
public class ServiceController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);
	
	@Autowired
	private ServiceDAO serviceDAO;

	@Transactional
	@ApiOperation(value="Save one service", response=Service.class)
	@RequestMapping(method=RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> save(@RequestBody Service serviceToSave) {
		serviceToSave.setId(null);
		Long id = serviceDAO.save(serviceToSave);
		return new ResponseEntity<>(id, HttpStatus.CREATED);
	}

	@Transactional
	@ApiOperation(value="Update one service", response=Service.class)
	@RequestMapping(method=RequestMethod.PUT, consumes={MediaType.APPLICATION_JSON_VALUE})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> update(@RequestBody Service serviceToUpdate) {
		serviceDAO.update(serviceToUpdate);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Transactional
	@ApiOperation(value="Delete one service")
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> delete(@PathVariable("id") long idServiceToDelete) {
		serviceDAO.delete(idServiceToDelete);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@Transactional
	@ApiOperation(value="Get one service", response=Service.class)
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Service> get(@PathVariable("id") long id) {
		Service serviceGetted = serviceDAO.get(id);
		HttpStatus httpStatus = serviceGetted != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<Service>(serviceGetted, httpStatus);
	}
	
	@Transactional
	@ApiOperation(value="Get service by its label", response=Service.class)
	@RequestMapping(value="/label/{label}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Service> getByLabel(@PathVariable("label") String label) {
		Service serviceGetted = null;
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		try {
			serviceGetted = serviceDAO.getByLabel(label);
			httpStatus = HttpStatus.OK;
		}catch (EmptyResultDataAccessException erdae) {
			LOGGER.info("Service with label '{}' not found", label);
		}
		return new ResponseEntity<Service>(serviceGetted, httpStatus);
	}

	@Transactional
	@ApiOperation(value="Get all services", response=Service.class)
	@RequestMapping(value="/list", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Service>> getAll() {
		List<Service> servicesGetted = serviceDAO.getAll();
		HttpStatus httpStatus = (servicesGetted != null && !servicesGetted.isEmpty()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<List<Service>>(servicesGetted, httpStatus);
	}

}
