package dao;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import connection.ConnectionFactory;

/**
 * Generic Data Access Object that provides the CRUD operations for any class
 * @param <T> The class managed
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;
    /**
     * Constructs a new AbstractDAO and determines the class type
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    private String getTableName() {
        return "\"" + type.getSimpleName().toLowerCase() + "\"";
    }
    /**
     * generates an SQL SELECT query to find a record by a specific field
     * @param field the field to search by. in this case, id
     * @return a sql String
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(getTableName());
        sb.append(" WHERE ").append(field).append(" = ?");
        return sb.toString();
    }
    /**
     * generates a sql query to retrieve all records
     * @return a sql String
     */
    private String createFindAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(getTableName());
        return sb.toString();
    }
    /**
     * finds all records of a given class
     * @return a list with all the entries for the class
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createFindAllQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
    /**
     * finds an object with a specific id
     * @param id the id to search for
     * @return the object with the given id
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            List<T> objects = createObjects(resultSet);
            if (objects != null && !objects.isEmpty()) {
                return objects.get(0);
            }
        } catch (SQLException | IndexOutOfBoundsException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }
    /**
     *Converts a database ResultSet into a list of objects of the given type T
     *
     * @param resultSet the sql result set containing rows from the database
     * @return a list containing the entity objects of type T
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException | InvocationTargetException | SQLException | IntrospectionException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:createObjects " + e.getMessage());
        }
        return list;
    }
    /**
     * creates the CREATE query
     * @return the sql statement
     */
    private String createInsertQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(getTableName());
        sb.append(" (");
        Field[] fields = type.getDeclaredFields();
        boolean first = true;
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("id")) continue;
            if (!first) sb.append(", ");
            sb.append(field.getName());
            first = false;
        }
        sb.append(") VALUES (");
        first = true;
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("id")) continue;
            if (!first) sb.append(", ");
            sb.append("?");
            first = false;
        }
        sb.append(")");
        return sb.toString();
    }
    /**
     * inserts a new entity in the database and updates the object's id with the one given by the database
     *
     * @param t the object containing the data to be inserted
     * @return the same object, but with the database id
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createInsertQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            Field[] fields = type.getDeclaredFields();
            int parameterIndex = 1;
            for (Field field : fields) {
                if (field.getName().equalsIgnoreCase("id")) continue;
                field.setAccessible(true);
                Object value = field.get(t);

                if (value instanceof java.util.Date) {
                    statement.setObject(parameterIndex, new java.sql.Timestamp(((java.util.Date) value).getTime()));
                } else {
                    statement.setObject(parameterIndex, value);
                }
                parameterIndex++;
            }
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int insertedId = rs.getInt(1);
                Field idField = type.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(t, insertedId);
            }

        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
    /**
     * build the UPDATE sql query
     * @return the sql query
     */
    private String createUpdateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(getTableName());
        sb.append(" SET ");
        Field[] fields = type.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            sb.append(fields[i].getName());
            sb.append(" = ?");
            if (i < fields.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(" WHERE id = ?");
        return sb.toString();
    }
    /**
     * update an existing database record to match the new field for that object
     * the record is identified using the id
     * @param t the object containing the updated data
     * @return the updated entity object
     */
    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createUpdateQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            Field[] fields = type.getDeclaredFields();
            int i;
            for (i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = fields[i].get(t);

                if (value instanceof java.util.Date) {
                    statement.setObject(i + 1, new java.sql.Timestamp(((java.util.Date) value).getTime()));
                } else {
                    statement.setObject(i + 1, value);
                }
            }
            Field idField = type.getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(t);
            statement.setObject(i + 1, idValue);
            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return t;
    }
    /**
     * builds the DELETE sql query
     * @return the sql query
     */
    private String createDeleteQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(getTableName());
        sb.append(" WHERE id = ?");
        return sb.toString();
    }
    /**
     * deletes a record from the database based on the id
     * @param id the identifier of the object to be deleted
     */
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}