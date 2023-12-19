package repository;

import domain.Cake;
import domain.Orders;
import domain.Unique;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DBRepository implements Repository{
    private static final String JDBC_URL =
            "jdbc:sqlite:identifier.sqlite";

    private Connection conn = null;
    private void connect() {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(JDBC_URL);
        try {
            conn = ds.getConnection();
            System.out.println("Connection status: " + (conn != null ? "Connected" : "Not connected"));
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
    private void disconnect() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public DBRepository() {
        connect();
    }
    void innitTables()
    {
        final String [] orders=new String[]{
                //3 orders
               "1, true, false, 10, 400, 2032-12-12",
                "2, true, false, 20, 200, 2020-12-12",
                "3, true, false, 30, 300, 2020-12-12,"
        };
        try {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO orders (id, is_available, is_done, due_date, cake_id, price) VALUES (?, ?, ?, ?, ?, ?)")) {
                for (String order : orders) {
                    String[] data = order.trim().split(",");
                    for (int i = 0; i < data.length; i++) {
                        data[i] = data[i].trim();
                    }
                    String dateString = data[5];  // Assuming data[3] contains the date string
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = dateFormat.parse(dateString);
                    java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

                    statement.setDate(4, sqlDate);
                    statement.setInt(1, Integer.parseInt(data[0]));
                    statement.setBoolean(2, Boolean.parseBoolean(data[1]));
                    statement.setBoolean(3, Boolean.parseBoolean(data[2]));
                    statement.setDouble(6, Double.parseDouble(data[4]));
                    statement.setInt(5, Integer.parseInt(data[3]));

                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            final String [] cakes=new String[]{
                    //3 cakes
                    "1, chocolate, small",
                    "2, vanilla, medium",
                    "3, strawberry, large"
            };
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO cakes (id, flavor, size) VALUES (?, ?, ?)")) {
                for (String cake : cakes) {
                    String[] data = cake.trim().split(",");
                    statement.setInt(1, Integer.parseInt(data[0]));
                    statement.setString(2, data[1]);
                    statement.setString(3, data[2]);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            System.err.println("[ERROR] initTables : " + e.getMessage());
        }

    }
    @Override
    public void add(Unique item) {
        //check if order or cake
        if(item instanceof Orders)
        {
            addOrder((Orders) item);
        }
        else if(item instanceof Cake)
        {
            addCake((Cake) item);
        }
        else
      return;
    }
    public void addOrder(Orders item) {
        try {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO orders (id, is_available, is_done, due_date, cake_id, price) VALUES (?, ?, ?, ?, ?, ?)")) {
                String order = item.simpleString();
                System.out.println(order);
                String[] data = order.split(",");
                String dateString = data[3];  // Assuming data[3] contains the date string
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = dateFormat.parse(dateString);
                java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

                statement.setDate(4, sqlDate);
                statement.setInt(1, Integer.parseInt(data[4]));
                statement.setBoolean(2, Boolean.parseBoolean(data[0]));
                statement.setBoolean(3, Boolean.parseBoolean(data[2]));
                statement.setDouble(6, Double.parseDouble(data[1]));
                statement.setInt(5, Integer.parseInt(data[5]));

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] initTables : " + e.getMessage());
        }


    }
    public void addCake(Cake item) {
        try {
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO cakes (id, flavor, size) VALUES (?, ?, ?)")) {
                String order = item.SimpleString();
                String[] data = order.split(",");
                statement.setInt(1, Integer.parseInt(data[0]));
                statement.setString(2, data[1]);
                statement.setString(3, data[2]);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] initTables : " + e.getMessage());
        }
    }
    public void removeOrder(int item)
    {
        try {
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM orders WHERE id=?")) {
                statement.setInt(1, item);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] initTables : " + e.getMessage());
        }
    }
    public void removeCake(int item)
    {
        try {
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM cakes WHERE id=?")) {

                statement.setInt(1, item);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] initTables : " + e.getMessage());
        }
    }


    @Override
    public Unique get(Object id) {
    //check if order or cake
            if(getOrder((Number) id)!=null)
            {
                return getOrder((Number) id);
            }
            else if(getCake((Number) id) !=null)
            {
                return getCake((Number) id);
            }
            else
                return null;

    }

    @Override
    public void update(Object id, Unique item) {

        if (item instanceof Orders) {
            updateOrder(id, (Orders) item);
        } else if (item instanceof Cake) {
            updateCake(id, (Cake) item);
        } else
            return;

    }
    public void updateCake(Object id, Cake item)
    {
        try {
            try (PreparedStatement statement = conn.prepareStatement("UPDATE cakes SET flavor=?, size=? WHERE id=?")) {
                String order = item.SimpleString();
                String[] data = order.split(",");
                statement.setInt(3, Integer.parseInt(data[0]));
                statement.setString(1, data[1]);
                statement.setString(2, data[2]);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] initTables : " + e.getMessage());
        }
    }
    public void updateOrder(Object id, Orders item)
    {
        try {
            try (PreparedStatement statement = conn.prepareStatement("UPDATE orders SET is_available=?, is_done=?, due_date=?, cake_id=?, price=? WHERE id=?")) {
                String order = item.simpleString();
                String[] data = order.split(",");
                String dateString = data[3];
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = dateFormat.parse(dateString);
                java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

                statement.setDate(3, sqlDate);
                statement.setInt(6, Integer.parseInt(data[4]));
                statement.setBoolean(1, Boolean.parseBoolean(data[0]));
                statement.setBoolean(2, Boolean.parseBoolean(data[2]));
                statement.setDouble(5, Double.parseDouble(data[1]));
                statement.setInt(4, Integer.parseInt(data[5]));

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] initTables : " + e.getMessage());
        }
    }
    public Cake getCake(Number id) {
        Cake cake = null;

        try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM cakes WHERE id = ?")) {
                statement.setInt(1, id.intValue());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int cakeId = resultSet.getInt("id");
                    String flavor = resultSet.getString("flavor");
                    String size = resultSet.getString("size");

                    cake = new Cake(cakeId, flavor, size);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] getCake: " + e.getMessage());
        }

        return cake;
    }
    public Orders getOrder(Number id) {
        Orders order = null;

        try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM orders WHERE id = ?")) {
                statement.setInt(1, id.intValue());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    int orderId = resultSet.getInt("id");
                    boolean isAvailable = ((ResultSet) resultSet).getBoolean("is_available");
                    boolean isDone = resultSet.getBoolean("is_done");
                    Date dueDate = resultSet.getDate("due_date");
                    int cakeId = resultSet.getInt("cake_id");
                    double price = resultSet.getDouble("price");
                    Cake cake=getCake(cakeId);
                    Map<Number,Cake> cakes=new HashMap<>();
                    cakes.put(cakeId,cake);

                    order = new Orders(isAvailable, price, isDone, dueDate, Integer.parseInt(String.valueOf(price)), cakes);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] getOrder: " + e.getMessage());
        }

        return order;
    }

    @Override
    public void remove(Object id) {
        //check if order or cake
            id= Integer.parseInt(String.valueOf(id));

            removeOrder((Integer) id);


            removeCake((Integer) id);



    }
    public Map<Integer, Orders> getAllOrders() {
        Map<Integer, Orders> ordersMap = new HashMap<>();
        Map<Integer, Cake> cakes = new HashMap<>();

        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM orders")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                boolean isAvailable = resultSet.getBoolean("is_available");
                boolean isDone = resultSet.getBoolean("is_done");
                Date dueDate = resultSet.getDate("due_date");
                int cakeId = resultSet.getInt("cake_id");
                double price = resultSet.getDouble("price");

                Cake cake = getCake(cakeId);
                cakes.put(cakeId, cake);

                Orders order = new Orders(isAvailable, price, isDone, dueDate, id, new HashMap<>(cakes));
                ordersMap.put(id, order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println("[ERROR] getAllOrders: " + e.getMessage());
        }

        return ordersMap;
    }


    public Map<Integer, Cake> getAllCakes(){
        Map<Integer, Cake>cakes=new HashMap<>();
        try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM cakes")) {

                ResultSet resultSet=statement.executeQuery();
                while(resultSet.next())

                {
                    int id = resultSet.getInt("id");
                    String flavor = resultSet.getString("flavor");
                    String size = resultSet.getString("size");
                    Cake cake = new Cake(id, flavor, size);
                    cakes.put(id, cake);

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] initTables : " + e.getMessage());
        }
        return cakes;

    }

    @Override
    public Map getAll() {
        //check if order of cake
        Map<Integer, Orders> ordersMap= getAllOrders();
        System.out.println(ordersMap);
        Map<Integer, Cake> cakesMap= getAllCakes();
        Map<Integer, Unique> all=new HashMap<>();
        all.putAll(ordersMap);
        all.putAll(cakesMap);
        return all;

    }

    @Override
    public Iterable getAllForClient() {
        return null;
    }
    public void GetAllCakesOfSize(String size)
    {
        try {
            try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM cakes WHERE size=?")) {
                statement.setString(1, size);
                statement.executeQuery();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] initTables : " + e.getMessage());
        }
    }

}

