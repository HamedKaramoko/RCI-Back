/**
 * 
 */
package ci.projects.rci.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * @author hamedkaramoko
 *
 */
@Entity
public class Service {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(length=50, unique=true)
	private String label;
	@Column(length=200)
	private String description;
	private float cost;
	
	@ManyToMany(mappedBy="services")
	private Set<Person> persons = new HashSet<>();
	/**
	 * 
	 */
	public Service() {
		super();
	}
	/**
	 * @param label
	 * @param cost
	 */
	public Service(String label, float cost) {
		super();
		this.label = label;
		this.cost = cost;
	}
	/**
	 * @param label
	 * @param description
	 * @param cost
	 */
	public Service(String label, String description, float cost) {
		super();
		this.label = label;
		this.description = description;
		this.cost = cost;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the cost
	 */
	public float getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(float cost) {
		this.cost = cost;
	}
	/**
	 * @return the persons
	 */
	public Set<Person> getPersons() {
		return persons;
	}
	/**
	 * @param persons the persons to set
	 */
	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
}
