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
 * Represents a city a Client can travel to.
 */
@Entity(tableName="cities")
public class City extends AbstractEntity {
	
	private String name;

	
	public City() {
		super();
		
	}
	
	protected String getSqlForSelector(SelectorData selectorData) {
		JQFilter filter = selectorData.getGridInfo().getFilter();
		String query = "select id, name from cities ";
		
		if(filter != null)
			query += " where " + filter.getAsSql(City.class, new AttributeSqlHandler());
				
		return query;
	}
	
	/*
	 JALCARAZ 31-08-2017. ISSUE 3
	 */

	protected String getSqlForAutocompleteOptions(String term) {							
		return getSqlForOption("where lower(name) like '%" +term.toLowerCase()+ "%'" );
	}
		
	
		
	protected String getSqlForOption(String where) {
		String query = "select id, name from cities " + where;
		
		return query;
	}

	@Override
	public ValidationResult validate() {
		ValidationResult vr = new ValidationResult();

		if(this.name == null || this.name.isEmpty()) {
			vr.addMessage(getTranslation("error.missingCityName"), "name");
		}

		return vr;
	}
	
	@Override
	public void set(EditorData data) {
		this.name = data.getStringAttribute("name");

	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Field(columnName="name")
	public String getName() { 
		return name;
	}


}


