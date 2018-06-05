/**
 * 
 */
package ci.projects.rci.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import ci.projects.rci.dao.GroupDAO;
import ci.projects.rci.model.Group;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author hamedkaramoko
 *
 */
@Api(value = "GroupController", description = "REST APIs related to Group Entity!!!!")
@RestController
@RequestMapping(value="/group")
@Transactional
@PreAuthorize("hasRole('ADMIN')")
public class GroupController {

	@Autowired
	private GroupDAO groupDAO;
	
	@ApiOperation(value="Save one group", response=Group.class)
	@RequestMapping(method=RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Group> save(@RequestBody Group group) {
		Group savedGroup = groupDAO.save(group);
		return new ResponseEntity<Group>(savedGroup, HttpStatus.CREATED);
	}
	
	@ApiOperation(value="Delete one group")
	@RequestMapping(value="/{name}", method=RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable("name") String name) {
		groupDAO.delete(name);
		return new ResponseEntity<String>(HttpStatus.ACCEPTED);
	}
	
	@ApiOperation(value="Get one group by his name", response=Group.class)
	@RequestMapping(value="/name/{name}", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Group> get(@PathVariable("name") final String name) {
		Group foundGroup = groupDAO.getByName(name);
		HttpStatus httpStatus = (foundGroup != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<Group>(foundGroup, httpStatus);
	}

	@ApiOperation(value="Get all groupss", response=Group.class)
	@RequestMapping(value="/list", method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Group>> getAll() {
		List<Group> groups = groupDAO.getAll();
		HttpStatus httpStatus = (groups != null && !groups.isEmpty()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		return new ResponseEntity<List<Group>>(groups, httpStatus);
	}
}
