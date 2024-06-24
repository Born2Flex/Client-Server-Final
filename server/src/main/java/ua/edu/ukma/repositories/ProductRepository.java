package ua.edu.ukma.repositories;

import ua.edu.ukma.dto.product.ProductCreationDto;
import ua.edu.ukma.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {
    private static final String SAVE_PRODUCT = "INSERT INTO products (name, description, producer, amount, price, category_id) VALUES (?,?,?,?,?,?)";
    private static final String FIND_ALL = "SELECT * FROM products";
    private static final String FIND_BY_ID = "SELECT * FROM products WHERE id = ?";
    private static final String FIND_BY_NAME_LIKE = "SELECT * FROM products WHERE name ILIKE ?";
    private static final String FIND_BY_PRICE_LT = "SELECT * FROM products WHERE price < ?";
    private static final String FIND_BY_AMOUNT_GT = "SELECT * FROM products WHERE amount > ?";
    private static final String UPDATE_PRODUCT = "UPDATE products SET name = ?, description = ?, producer = ?, price = ?, amount = ? WHERE id = ?";
    private static final String UPDATE_PRODUCT_PRICE = "UPDATE products SET price = ? WHERE id = ?";
    private static final String UPDATE_PRODUCT_AMOUNT = "UPDATE products SET amount = ? WHERE id = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM products WHERE id = ?";
    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    public Product saveProduct(ProductCreationDto product) {
        try (PreparedStatement statement = connection.prepareStatement(SAVE_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setString(3, product.getProducer());
            statement.setInt(4, product.getAmount());
            statement.setDouble(5, product.getPrice());
            statement.setInt(6, product.getCategoryId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt("id");
                    return new Product(generatedId, product.getName(), product.getDescription(), product.getProducer(), product.getPrice(), product.getAmount(), product.getCategoryId());
                } else {
                    throw new SQLException("Product creation failed.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving product", e);
        }
    }

    public List<Product> findAll() {
        try (Statement statement = connection.createStatement()) {
            return mapToProductsList(statement.executeQuery(FIND_ALL));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> findByNameStartsWith(String name) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME_LIKE)) {
            preparedStatement.setString(1, name + "%");
            return mapToProductsList(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Product> findByName(String name) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME_LIKE)) {
            preparedStatement.setString(1, name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToProduct(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> findByPriceLessThan(double price) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_PRICE_LT)) {
            preparedStatement.setDouble(1, price);
            return mapToProductsList(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> findByAmountGreaterThan(int amount) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_AMOUNT_GT)) {
            preparedStatement.setInt(1, amount);
            return mapToProductsList(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Product> findById(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToProduct(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProduct(Product product) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setString(3, product.getProducer());
            preparedStatement.setInt(4, product.getAmount());
            preparedStatement.setDouble(5, product.getPrice());
            preparedStatement.setInt(6, product.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProductPrice(int id, double newPrice) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_PRICE)) {
            preparedStatement.setDouble(1, newPrice);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProductAmount(int id, int newAmount) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_AMOUNT)) {
            preparedStatement.setInt(1, newAmount);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteProduct(int id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Product> mapToProductsList(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(mapToProduct(resultSet));
        }
        return products;
    }

    private Product mapToProduct(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String producer = resultSet.getString("producer");
        int amount = resultSet.getInt("amount");
        double price = resultSet.getDouble("price");
        int categoryId = resultSet.getInt("category_id");
        return new Product(id, name, description, producer, price, amount, categoryId);
    }
}
