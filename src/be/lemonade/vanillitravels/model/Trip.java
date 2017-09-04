package be.lemonade.vanillitravels.model;

import be.vsko.jss.annotation.Entity;
import be.vsko.jss.annotation.Field;
import be.vsko.jss.common.AbstractEntity;
import be.vsko.jss.common.EditorData;
import be.vsko.jss.common.SelectorData;
import be.vsko.jss.common.ValidationResult;
import be.vsko.jss.common.jqg.JQFilter;
import java.util.Date;
import be.vsko.jss.common.jqg.AttributeSqlHandler;

/**
 * 
 * @author lionel
 *
 * Represents a trip a client can perform. A trip is done to a certain City between two dates 
 * and has a certain cost. 
 */
@Entity(tableName="trips")
public class Trip extends AbstractEntity {
	
	private Client client;
	private City city;
	private Date dateFrom;
	private Date dateTo;
	private Double hotelCostDaily;
	private Double travelCost;
	
	/**
	 31-08-2017 JALCARAZ (ISSUE 4)
	 */
	////////////////////////
	private Double sigCost;
	////////////////////////


	public Trip() {
		super();
		this.client = new Client();
		this.city = new City();
		this.dateFrom = new Date();
		this.dateTo = new Date();
		
	}
	
	protected String getSqlForSelector(SelectorData selectorData) {
		JQFilter filter = selectorData.getGridInfo().getFilter();
		String query = "select " +
						"	t.id, " +
						"	c.first_name || ' ' || c.last_name as client, " +
						"	ci.name as city, " +
						"	t.date_from, " +
						"	t.date_to, " +
						"	t.hotel_cost_daily " +
						"from " +
						"	trips t " +
						"	inner join clients c on c.id = t.client_id " +
						"	inner join cities ci on ci.id = t.city_id";
		
		if(filter != null)
			query += " where " + filter.getAsSql(Trip.class, new AttributeSqlHandler());
				
		return query;
	}
	
	@Override
	public ValidationResult validate() {
		ValidationResult vr = new ValidationResult();
		
		/**
		 JALCARAZ 31-08-2017
		6. Validate in the Trips CRUD that From date < To date 
		 */
		
		if ((dateTo.before(dateFrom)) || (dateTo.equals(dateFrom))){
			vr.addMessage(getTranslation("error.toDateBeforeFromDate"), "toDate");
		}
		
		return vr;
	}
	

	
	@Override
	public void set(EditorData data) {
		this.client.setId(data.getIntegerAttribute("clientId"));
		this.city.setId(data.getIntegerAttribute("cityId"));
		this.dateFrom = data.getDateAttribute("dateFrom");
		this.dateTo = data.getDateAttribute("dateTo");
		this.hotelCostDaily = data.getDoubleAttribute("hotelCostDaily");
		this.travelCost = data.getDoubleAttribute("travelCost");
		this.sigCost = data.getDoubleAttribute("sigCost");

	}
	
	public void setClient(Client client) {
		this.client = client;
	}

	@Field(columnName="client_id", isForeignKey=true)
	public Client getClient() { 
		return client;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@Field(columnName="city_id", isForeignKey=true)
	public City getCity() { 
		return city;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	@Field(columnName="date_from")
	public Date getDateFrom() { 
		return dateFrom;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	@Field(columnName="date_to")
	public Date getDateTo() { 
		return dateTo;
	}

	public void setHotelCostDaily(Double hotelCostDaily) {
		this.hotelCostDaily = hotelCostDaily;
	}

	@Field(columnName="hotel_cost_daily")
	public Double getHotelCostDaily() { 
		return hotelCostDaily;
	}

	public void setTravelCost(Double travelCost) {
		this.travelCost = travelCost;
	}

	@Field(columnName="travel_cost")
	public Double getTravelCost() { 
		return travelCost;
	}
	
	/**
	 31-08-2017 JALCARAZ (ISSUE 4)
	 
	1)ALTER TABLE trips ADD COLUMN sig_cost numeric;
	2)update trips set sig_cost = 0;
	 */
	//////////////////////////////////////////////
	public void setSigCost(Double sigCost) {
		this.sigCost = sigCost;
	}
	
	@Field(columnName="sig_cost")
	public Double getSigCost() {
		return sigCost;
	}
	//////////////////////////////////////////////


	


}


