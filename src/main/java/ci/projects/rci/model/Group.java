/**
 * 
 */
package ci.projects.rci.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author hamedkaramoko
 *
 */
@Entity
@Table(name="ROLE")
public class Group {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(length=20, unique=true)
	private String name;
	
	public Group() {
		super();
	}

	/**
	 * @param name
	 */
	public Group(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
