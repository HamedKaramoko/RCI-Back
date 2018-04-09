/**
 * 
 */
package ci.projects.rci.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * @author hamedkaramoko
 *
 */
@Entity
public class Person {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Column(length=20, unique=true)
	private String login;
	@Column(length=20)
	private String password;
	@Column(length=50)
	private String surname;
	@Column(length=50)
	private String firstname;
	@Enumerated(EnumType.STRING)
	@Column(length=6)
	private Gender gender;
	@Column(length=40, unique=true)
	private String email;
	
	@ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(name="Person_Service")
	private Set<Service> services = new HashSet<>();
	/**
	 * 
	 */
	public Person() {
		super();
	}
	/**
	 * @param login
	 * @param password
	 */
	public Person(String login, String password) {
		super();
		this.login = login;
		this.password = password;
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
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the services
	 */
	public Set<Service> getServices() {
		return services;
	}
	/**
	 * @param services the services to set
	 */
	public void setServices(Set<Service> services) {
		this.services = services;
	}
}
