package be.vsko.jss.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import be.vsko.jss.annotation.Entity;
import be.vsko.jss.annotation.Field;
import be.vsko.jss.common.jqg.JQGridInfo;
import be.vsko.jss.exception.InvalidEntityException;
import be.vsko.jss.exception.MappingException;
import be.vsko.jss.exception.PersistenceException;
import be.vsko.jss.exception.ReflectionException;
import be.vsko.jss.exception.SearchException;


/**
 * @author lionel
 *
 * Base class for all the persistable entities. This class adds three properties: id, creationDate, modificationDate.
 * Offers methods to save, update and delete the entity.
 */
public abstract class AbstractEntity {

	private static Logger log = Logger.getLogger(AbstractEntity.class);
	
	private Integer id;
	private Date createdOn;
	private Date modifiedOn;
	private String createdBy;
	private String modifiedBy;
	
	private static final boolean LOG_SQL = true;
	
	/**
	 * Saves the entity
	 */
	public void save() throws InvalidEntityException {
		Connection conn = null;
		ValidationResult vr = validate();
		
		if(!vr.isValid())
			throw new InvalidEntityException(this, vr);
		
		try {
			log.info("saving object " + getClass());
			
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(createSaveQuery(), Statement.RETURN_GENERATED_KEYS);				
			
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()) {
				setId(rs.getInt(1));
			}
								
			log.info("inserted id: " + id);
		}
		catch(SQLException e) {
			throw new PersistenceException(e);
		}
		finally {
			if(conn != null)
				try { conn.close(); } catch (Exception ignore) {}
		}				
	}
	
	/**
	 * Updates the entity
	 */
	public void update() throws InvalidEntityException{
		Connection conn = null;
		ValidationResult vr = validate();
		
		if(!vr.isValid())
			throw new InvalidEntityException(this, vr);
		
		try {
			log.info("updating object " + getClass() + " with id " + getId());
			
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(createUpdateQuery());				
			
		}
		catch(SQLException e) {
			throw new PersistenceException(e);
		}
		finally {
			if(conn != null)
				try { conn.close(); } catch (Exception ignore) {}
		}				
	}
	
	public void delete() {
		Connection conn = null;
		
		beforeDelete();
		
		try {
			log.info("deleting object " + getClass() + " with id " + getId());
			
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();			
			
			String sql = "delete from " + getTableName(getClass()) + " where id = " + getId();
			if(LOG_SQL)
				log.info(sql);
			
			stmt.executeUpdate(sql);				
			
		}
		catch(SQLException e) {
			throw new PersistenceException(e);
		}
		finally {
			if(conn != null)
				try { conn.close(); } catch (Exception ignore) {}
		}
	}
	
	/**
	 * Method intended to be overriden. Performes extra operations
	 * before deleting the entity, for example, deletes the related
	 * entities (manual cascade).
	 */
	protected void beforeDelete() {
		
	}
	
	/**
	 * Gets an entity of type clazz by its id.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends AbstractEntity> T findById(Class<T> clazz, Integer id) {
		T result = null; 
		Connection conn = null;
		
		try {			
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();			
			String query = "select * from " + getTableName(clazz) + " where id = " + id;
			
			if(LOG_SQL)
				log.info(query);
			
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()) {
				result = (T)createAndLoadEntity(clazz, rs);								
			}	
		}
		catch(SQLException e) {
			throw new SearchException(e);
		}
		finally {
			if(conn != null)
				try { conn.close(); } catch (Exception ignore) {}
		}

		return result;
	}
	
	/**
	 * Gets a list of entities of type clazz by their ids.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends AbstractEntity> List<T> findByIds(Class<T> clazz, String ids) {
		List<T> result = new ArrayList<T>(); 
		Connection conn = null;
		
		try {			
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();			
			String query = "select * from " + getTableName(clazz) + " where id in (" + ids + ")";
			
			if(LOG_SQL)
				log.info(query);
			
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				T t = (T)createAndLoadEntity(clazz, rs);
				
				result.add(t);
			}	
		}
		catch(SQLException e) {
			throw new SearchException(e);
		}
		finally {
			if(conn != null)
				try { conn.close(); } catch (Exception ignore) {}
		}

		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T extends AbstractEntity> List<T> find(Class<T> clazz, String orderby, Boolean asc, Integer limit, Integer offset, String where) {
		List<T> result = new ArrayList<T>();
		Connection conn = null;
		
		try {			
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();			
			String query = "select * from " + getTableName(clazz) + " ";
			
			if(where != null && !where.isEmpty())
				query += where + " ";
			
			if(orderby != null && orderby.length() > 0) {
				query += " order by " + orderby;
				if(asc != null && !asc)
					query += " desc";
			}
			
			if(limit != null && limit > 0)
				query += " limit " + limit;
			
			if(offset != null && offset > 0)
				query += " offset " + offset;
			
			if(LOG_SQL)
				log.info(query);
			
			ResultSet rs = stmt.executeQuery(query);
									
			while(rs.next()) {
				T t = (T)createAndLoadEntity(clazz, rs);
				
				result.add(t);
			}
			
		}
		catch(SQLException e) {
			throw new SearchException(e);
		}
		finally {
			if(conn != null)
				try { conn.close(); } catch (Exception ignore) {}
		}
		
		return result;
	}
	
	
	public static <T extends AbstractEntity> List<T> find(Class<T> clazz, String where) {		
		return find(clazz, null, null, null, null, where);
	}
	
	public static <T extends AbstractEntity> List<T> getAll(Class<T> clazz, String orderby, Boolean asc, Integer limit, Integer offset) {	
		return find(clazz, orderby, asc, limit, offset, "");
	}
	
	public static <T extends AbstractEntity> List<T> getAll(Class<T> clazz) {		
		return getAll(clazz, null, null, null, null);
	}
	
	
		
	public static <T extends AbstractEntity> List<HashMap<String, Object>> findForSelector(Class<T> clazz, SelectorData selectorData, String sql) {
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		Connection conn = null;
		JQGridInfo jqinfo = selectorData.getGridInfo();
		try {			
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();			
						
			String query = sql;			
			
			if(jqinfo.getSortField() != null && jqinfo.getSortField().length() > 0) {
				query += " order by " + jqinfo.getSortField();
				if(!jqinfo.isAsc())
					query += " desc";
			}
			
			
			//if(limit != null && limit > 0)
				query += " limit " + jqinfo.getRows();
			
			//if(offset != null && offset > 0)
				query += " offset " + jqinfo.getIndexFrom();
			
			if(LOG_SQL)
				log.info(query);
			
			ResultSet rs = stmt.executeQuery(query);
									
			while(rs.next()) {
				//use a LinkedHashMap to keep the order
				HashMap<String, Object> map = new LinkedHashMap<String, Object>();
				
				for(int i=1; i<=rs.getMetaData().getColumnCount(); i++) {
					String columnName = rs.getMetaData().getColumnName(i);
					map.put(columnName, getValueForColumn(rs.getObject(i), columnName, clazz));
				}
				
				result.add(map);
			}
			
		}
		catch(SQLException e) {
			throw new SearchException(e);
		}
		finally {
			if(conn != null)
				try { conn.close(); } catch (Exception ignore) {}
		}
		
		return result;
	}
	
	/**
	 * Will find the value to show in the row for the given column.
	 * If the given column represents an enum, then the value will be translated otherwise it will keep the value from the db.  
	 * 
	 * @param object
	 * @param columnName
	 */	
	@SuppressWarnings("unchecked")
	private static Object getValueForColumn(Object object, String columnName, Class entityClass) {						
		Class returnType = getReturnTypeByDatabaseFieldName(entityClass, columnName);
		
		if(returnType != null && returnType.isEnum() && object != null) {
			// detected column of type Enum -> do the translation for the description		
			Class<? extends Enum> generalEnum = (Class<? extends Enum>)returnType; 			
			object =  Util.getTranslatedEnumDescription(generalEnum, Enum.valueOf(generalEnum, object.toString()));
		}				
		
		return object;
	}
	
	/**
	 * Executes a query in the database and returns the results as a list of hashmaps.
	 * @param sql The query to execute.
	 * @return A List of hashmaps where the key is the name of the field.
	 */
	public static List<HashMap<String, Object>> simpleQuery(String sql) {
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		Connection conn = null;
		
		try {			
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();			
						
			String query = sql;			
			
			if(LOG_SQL)
				log.info(query);
			
			ResultSet rs = stmt.executeQuery(query);
									
			while(rs.next()) {
				//use a LinkedHashMap to keep the order
				HashMap<String, Object> map = new LinkedHashMap<String, Object>();
				
				for(int i=1; i<=rs.getMetaData().getColumnCount(); i++) {
					String columnName = rs.getMetaData().getColumnName(i);
					map.put(columnName, rs.getObject(i));
				}
				
				result.add(map);
			}
			
		}
		catch(SQLException e) {
			throw new SearchException(e);
		}
		finally {
			if(conn != null)
				try { conn.close(); } catch (Exception ignore) {}
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T extends AbstractEntity> List<HashMap<String, Object>> findForSelector(Class<T> clazz, SelectorData selectorData) {
		T obj = (T)createObject(clazz);
		String query = obj.getSqlForSelector(selectorData);
		
		return findForSelector(clazz, selectorData, query);
	}

	@SuppressWarnings("unchecked")
	public static <T extends AbstractEntity> Integer getCountForSelector(Class<T> clazz, SelectorData selectorData) {
		T obj = (T)createObject(clazz);
		String query = obj.getSqlForSelector(selectorData);
		
		return getCountForSelector(clazz, selectorData, query);
	}
	
	public static <T extends AbstractEntity> Integer getCountForSelector(Class<T> clazz, SelectorData selectorData, String sql) {
		Integer result = null;
		Connection conn = null;		
		
		try {			
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();			
			
			String query = sql;
			
			//fix query to select only count(*)
			int idx = query.indexOf(" from ");
			query = "select count(*) as count " + query.substring(idx);
									
			if(LOG_SQL)
				log.info(query);
			
			ResultSet rs = stmt.executeQuery(query);
									
			if(rs.next()) {
				result = rs.getInt("count");
			}
			
		}
		catch(SQLException e) {
			throw new SearchException(e);
		}
		finally {
			if(conn != null)
				try { conn.close(); } catch (Exception ignore) {}
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends AbstractEntity> List<Option> getAsAutocompleteOptions(Class<T> clazz, String term) {
		List<Option> result = new ArrayList<Option>();
		
		Connection conn = null;		
		try {			
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();			
			
			T obj = (T)createObject(clazz);
			String query = obj.getSqlForAutocompleteOptions(Util.escape(term));			
			
			if(LOG_SQL)
				log.info(query);
			
			ResultSet rs = stmt.executeQuery(query);
									
			while(rs.next()) {												
				result.add(new Option(rs.getInt(1), rs.getString(2)));
			}
			
		}
		catch(SQLException e) {
			throw new SearchException(e);
		}
		finally {
			if(conn != null)
				try { conn.close(); } catch (Exception ignore) {}
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends AbstractEntity> String getAutocompleteOptionValue(Class<T> clazz, Integer id) {
		String result = "";
		
		Connection conn = null;		
		try {			
			conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();			
			
			T obj = (T)createObject(clazz);
			String query = obj.getSqlForSelectedAutocompleteOption(id);			
			
			if(LOG_SQL)
				log.info(query);
			
			ResultSet rs = stmt.executeQuery(query);
									
			if(rs.next()) {												
				result = rs.getString(2);
			}
			
		}
		catch(SQLException e) {
			throw new SearchException(e);
		}
		finally {
			if(conn != null)
				try { conn.close(); } catch (Exception ignore) {}
		}
		
		return result;
	}
	
	protected abstract String getSqlForSelector(SelectorData selectorData);
	
	protected String getSqlForAutocompleteOptions(String term) {
		return "";
	}
	
	protected String getSqlForOption(String where) {
		return "";
	}
	
	protected String getSqlForSelectedAutocompleteOption(Integer id) {
		return getSqlForOption("where id = " + id); 
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends AbstractEntity>  T createAndLoadEntity(Class clazz, ResultSet rs) {		
		T obj = null; 
		
		try {
			Class partypes[];        
			obj = (T)createObject(clazz);
			
			
			//loop through the methods of the class and look for the getters
			for(Method method : clazz.getMethods()) {
				//get the Field annotation
				Field field = method.getAnnotation(Field.class);
				
				//is it the getter field?
				if(field != null) {
					String setterName = "";
					if(method.getName().startsWith("is"))
						setterName = method.getName().replaceFirst("is", "set");
					else
						setterName = method.getName().replaceFirst("get", "set");
										
					partypes = new Class[1];
					partypes[0] = method.getReturnType();										
					Method setter = clazz.getMethod(setterName, partypes);
					
					//the return type of the getter is the same as the expected
					//parameter of the setter
					if(method.getReturnType().equals(String.class))
						setter.invoke(obj, rs.getString(field.columnName()));
					else if(method.getReturnType().isEnum()) {		
						//its an enum, instanciate it and get the value using the static valueOf method
						if(rs.getString(field.columnName()) != null) {
							partypes = new Class[1];
							partypes[0] = String.class;
							Method enumMethod = method.getReturnType().getMethod("valueOf", partypes);
							
							Object enumValue = enumMethod.invoke(method.getReturnType(), rs.getString(field.columnName()));
							
							setter.invoke(obj, enumValue);
						}
					}
					else if(method.getReturnType().equals(Date.class))						
						setter.invoke(obj, rs.getTimestamp(field.columnName()));
					else if(method.getReturnType().equals(Integer.class)) {											
						//setter.invoke(obj, rs.getInt(field.columnName()));
						setter.invoke(obj, rs.getObject(field.columnName()));
					}
					else if(method.getReturnType().equals(Long.class)) {
						setter.invoke(obj, rs.getObject(field.columnName()));
					}
					else if(method.getReturnType().equals(Double.class)) 						
						setter.invoke(obj, rs.getDouble(field.columnName()));
					else if(method.getReturnType().equals(Boolean.class)) 						
						setter.invoke(obj, rs.getBoolean(field.columnName()));
					else if(method.getReturnType().equals(boolean.class)) 						
						setter.invoke(obj, rs.getBoolean(field.columnName()));
					else if(method.getReturnType().getSuperclass().equals(AbstractEntity.class)) {
						//its a fk, create the object and load the id
						T fk = (T)createObject(method.getReturnType());
						Integer id = rs.getInt(field.columnName());
						fk.setId(id==0?null:id);
						
						setter.invoke(obj, fk);
					}
					else
						throw new MappingException("Cannot detect the return type " + method.getReturnType() + " if its a different class, make sure it inherits from AbstractClass");
				}
			}
		}
		catch(Exception e) {
			throw new MappingException(e);
		}
		
		return obj;
	}
	
	
	/**
	 * Creates an object using the empty constructor.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends AbstractEntity> T createObject(Class clazz) {
		T result = null;
		try {
			Class partypes[] = new Class[0];        
			Constructor constructor = clazz.getConstructor(partypes);
			Object arglist[] = new Object[0];            
			result = (T)constructor.newInstance(arglist);
		}
		catch(Exception e) {
			throw new ReflectionException(e);
		}
		
		return result;
	}
	
	private String createUpdateQuery() {
		Map<String, Object> columns = getColumns();
		String query = "update " + getTableName(getClass()) + " set ";
		
		//add the modification date
		columns.put("modified_on", new Date());
		columns.put("modified_by", modifiedBy);
		columns.remove("created_on");
		columns.remove("created_by");
		
		Iterator<String> it = columns.keySet().iterator();
		while(it.hasNext()) {
			String columnName = it.next();
			query += columnName + " = " + fixColumnForSql(columns.get(columnName));
					
			if(it.hasNext()) {
				query += ", ";		
			}
		}
		
		query += " where id = " + getId();
		
		if(LOG_SQL)
			log.info(query);
		
		return query;
	}
	
	/**
	 * Creates the query for saving the entity in the database.
	 */
	private String createSaveQuery() {
		Map<String, Object> columns = getColumns();		
		String query = "insert into " + getTableName(getClass()) + " (";
		String colQuery = "";
		
		//add the createdOn, modifiedOn, createdBy, modifiedBy
		columns.put("created_on", new Date());
		columns.put("modified_on", new Date());
		columns.put("created_by", createdBy);
		columns.put("modified_by", modifiedBy);
		
		columns.remove("id");
		
		Iterator<String> it = columns.keySet().iterator();
		while(it.hasNext()) {
			String columnName = it.next();
			query += columnName;
			colQuery += fixColumnForSql(columns.get(columnName));
			
			if(it.hasNext()) {
				query += ", ";
				colQuery += ", ";
			}
		}
		
		query += ") values (" + colQuery + ")";
		
		if(LOG_SQL)
			log.info(query);
		
		return query;
	}
	
	@SuppressWarnings("unchecked")
	private static String fixColumnForSql(Object obj) {
		String result = "";
		
		if(obj != null) {
			if(obj instanceof String)
				result = "'"+Util.escape((String)obj)+"'";
			else if(obj.getClass().isEnum()) 
				result = "'"+((Enum)obj).name()+"'";
			else if(obj instanceof Date)
				result = "'"+DateUtil.dateToSql((Date)obj)+"'";
			else
				result = obj.toString();
		}
		else
			result = "null";
		
		return result;
	}
	
		
	/**
	 * Gets the name of the table.
	 */
	private static String getTableName(Class<?> clazz) {
		Entity entity = clazz.getAnnotation(Entity.class);
		
		if(entity == null)
			throw new MappingException("The class " + AbstractEntity.class.getName() + " must declare the Entity annotation");
	
		if(entity.tableName() == null)
			throw new MappingException("The class " + AbstractEntity.class.getName() + " declares anEntity annotation, but not the tableName property");
		
		return entity.tableName();
	}
	
	
	/**
	 * Gets a map of all the columns of this entity (column name - value).
	 */
	private Map<String, Object> getColumns() {
		
		Map<String, Object> columns = new HashMap<String, Object>();
		
		for(Method method : getClass().getMethods()) {
			//get the Field annotation
			Field field = method.getAnnotation(Field.class);
			
			if(field != null) {
				//get the value of the column using the getter method
				Object value = getColumnValue(method); 
				
				if(field.isForeignKey()) {
					if(value instanceof AbstractEntity) {
						value = ((AbstractEntity)value).getId();
					}
					else if(value != null)
						throw new MappingException("The method " + method + " is declared as a foreign key, it must return an object of type AbstractEntity!");
				}
				
				columns.put(field.columnName(), value);				
				
			}
		}
		
		return columns;
	}
	
	/**
	 * Gets the value of a column using its getter method
	 */
	private Object getColumnValue(Method method) {
		Object result = null;
		
		try {
			result = method.invoke(this);
		}
		catch(InvocationTargetException e) {
			throw new MappingException("There was a problem accessing the getter " + method.getName()+". Make sure" +
					"it exists, it has the @Field annotation, it doesn't expect any parameter and it is not private.");
		}
		catch(IllegalAccessException e) {
			throw new MappingException("There was a problem accessing the getter " + method.getName()+". Make sure" +
					"it exists, it has the @Field annotation, it doesn't expect any parameter and it is not private.");
		}
		
		return result;
	}
		
	
	/**
	 * Based on the column name of the database, this method gets the data type as a java class, 
	 * for example, for the field date_from it will return Date.
	 */
	@SuppressWarnings("unchecked")
	public static Class getReturnTypeByDatabaseFieldName(Class clazz, String fieldName) {
		Class result = null;
		
		Field field = AbstractEntity.getFieldAnnotationByDatabaseFieldName(clazz, fieldName);
		if(field != null)
			result = getGetterByField(clazz, field).getReturnType();		
		
		return result;
	}
	
	public void setFieldByDatabaseName(String fieldName, Object value) {
		Method setter = getSetterByDatabaseFieldName(fieldName);
		
		try {
			//check if its a fk
			if(setter.getParameterTypes()[0].getGenericSuperclass().equals(AbstractEntity.class)) {
				Method getter = getGetterByDatabaseFieldName(fieldName);
				AbstractEntity obj = (AbstractEntity)getter.invoke(this);
				if(value.getClass().equals(String.class))
					obj.setId(Integer.parseInt(value.toString()));				
			}
			else {
				setter.invoke(this, value);
			}
		}
		catch(Exception e) {
			log.warn("Could not set property " + fieldName + " for class " + getClass());
		}
	}
	
	public Method getSetterByDatabaseFieldName(String fieldName) throws ReflectionException {
		Field field = getFieldAnnotationByDatabaseFieldName(getClass(), fieldName);
		return getSetterByField(getClass(), field);
	}
	
	public Method getGetterByDatabaseFieldName(String fieldName) throws ReflectionException {
		Field field = getFieldAnnotationByDatabaseFieldName(getClass(), fieldName);
		return getGetterByField(getClass(), field);
	}
	
	/**
	 * Gets the Setter Method for certain class based on the @Field annotation.
	 */
	@SuppressWarnings("unchecked")
	public static Method getSetterByField(Class clazz, Field field) {
		Method result = null;
		Method getter = getGetterByField(clazz, field);
		String aux = "";
		
		if(getter.getName().startsWith("get"))
			aux = getter.getName().replaceFirst("get", "");
		else if(getter.getName().startsWith("is"))
			aux = getter.getName().replaceFirst("is", "");
		
		for(Method method : clazz.getMethods()) {
			if(method.getName().startsWith("set")) {
				if(method.getName().replaceFirst("set", "").equals(aux)) {
					result = method;
				}
			}
		}
		
		
		
		return result;
	}
	
	/**
	 * Gets the Getter Method for certain class based on the @Field annotation.
	 */
	@SuppressWarnings("unchecked")
	public static Method getGetterByField(Class clazz, Field field) {
		Method result = null;
		
		if(field != null) {
			for(Method method : clazz.getMethods()) {
				//get the Field annotation
				Field f = method.getAnnotation(Field.class);
				
				if(f != null && field.columnName().equals(f.columnName()))
					result = method;
			}
		}
		
		
		return result;
	}
	
	/**
	 * Gets the @Field annotation based no the column name of the database.
	 */
	@SuppressWarnings("unchecked")
	public static Field getFieldAnnotationByDatabaseFieldName(Class clazz, String fieldName) {
		Field result = null;
		
		for(Method method : clazz.getMethods()) {
			//get the Field annotation
			Field field = method.getAnnotation(Field.class);
			
			if(field != null && field.columnName().equals(fieldName)) {		
				result = field;
			}
		}

		return result;
	}

	/**
	 * Method intended to be overriden by the children. Allows to
	 * set the properties of the object. 
	 */
	public void set(EditorData data) {
		
	}
	
	@Field(columnName="id")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Field(columnName="created_on")
	public Date getCreatedOn() {
		return createdOn;
	}
	
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	@Field(columnName="modified_on")
	public Date getModifiedOn() {
		return modifiedOn;
	}
	
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public ValidationResult validate() {
		
		ValidationResult vr = new ValidationResult();
		vr.setValid(true);
		
		return vr;
	}
	
	@Field(columnName="created_by")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Field(columnName="modified_by")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	protected String getTranslation(String key) {
		return JSSTranslationProperties.getInstance().getMessage(key);
	}
	
	protected void log(String message) {
		log.info(message);
	}
}
