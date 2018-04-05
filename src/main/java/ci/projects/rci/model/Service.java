/**
 * 
 */
package ci.projects.rci.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author hamedkaramoko
 *
 */
@Entity
public class Service {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Column(length=50, unique=true)
	private String label;
	@Column(length=200)
	private String description;
	private float cost;
	/**
	 * 
	 */
	public Service() {
		super();
	}
	/**
	 * @param id
	 * @param label
	 * @param cost
	 */
	public Service(String label, float cost) {
		super();
		this.label = label;
		this.cost = cost;
	}
	/**
	 * @param id
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
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
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
}
