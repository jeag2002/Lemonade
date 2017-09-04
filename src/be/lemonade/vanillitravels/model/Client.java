package be.lemonade.vanillitravels.model;

import be.vsko.jss.annotation.Entity;
import be.vsko.jss.annotation.Field;
import be.vsko.jss.common.AbstractEntity;
import be.vsko.jss.common.EditorData;
import be.vsko.jss.common.SelectorData;
import be.vsko.jss.common.ValidationResult;
import be.vsko.jss.common.jqg.JQFilter;

import be.vsko.jss.common.jqg.AttributeSqlHandler;

/**
 * 
 * @author lionel
 *
 * Represents a client of the Vanilli travel agency.
 */
@Entity(tableName="clients")
public class Client extends AbstractEntity {
	
	private String firstName;
	private String lastName;

	
	public Client() {
		super();
		
	}
	
	protected String getSqlForSelector(SelectorData selectorData) {
		JQFilter filter = selectorData.getGridInfo().getFilter();
		String query = "select id, first_name, last_name from clients ";
		
		if(filter != null)
			query += " where " + filter.getAsSql(Client.class, new AttributeSqlHandler());
				
		return query;
	}
	
	/**
	 * JALCARAZ 31-08-2017
	 2. When creating a trip, it is only possible to search for clients by the first name in the autocomplete field. Make it possible
	 to search by first name and/or last name.
	 */

	protected String getSqlForAutocompleteOptions(String term) {							
		return getSqlForOption("where lower(first_name) like '%"+term.toLowerCase()+"%' or lower(last_name) like '%" +term.toLowerCase()+ "%'");
	}
	
		
	protected String getSqlForOption(String where) {
		String query = "select id, first_name from clients " + where;
		
		return query;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult vr = new ValidationResult();

		if(this.firstName == null || this.firstName.isEmpty()) {
			vr.addMessage(getTranslation("error.missingFirstName"), "firstName");
		}
		
		/**
		 JALCARAZ 31-08-2017
		 1. Add Last name validation for the Clients CRUD. The last name shouldn't be empty. 
		 */
		
		if (this.lastName == null || this.lastName.isEmpty()){
			vr.addMessage(getTranslation("error.missingLastName"), "lastName");
		}

		return vr;
	}
	
	
	@Override
	public void set(EditorData data) {
		this.firstName = data.getStringAttribute("firstName");
		this.lastName = data.getStringAttribute("lastName");

	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Field(columnName="first_name")
	public String getFirstName() { 
		return firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Field(columnName="last_name")
	public String getLastName() { 
		return lastName;
	}


}


