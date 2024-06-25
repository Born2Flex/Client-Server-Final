package ua.edu.ukma.repositories;

import ua.edu.ukma.dto.product.ProductCreationDto;
import ua.edu.ukma.dto.product.ProductExtDto;
import ua.edu.ukma.dto.product.ProductPriceDto;
import ua.edu.ukma.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {
    private static final String SAVE_PRODUCT = "INSERT INTO products (name, description, producer, amount, price, category_id) VALUES (?,?,?,?,?,?)";
    private static final String FIND_ALL_PRODUCTS = "SELECT * FROM products";
    private static final String FIND_BY_ID = "SELECT * FROM products WHERE id = ?";
    private static final String FIND_BY_NAME_LIKE = "SELECT * FROM products WHERE name ILIKE ?";
    private static final String FIND_BY_NAME_STARTS_WITH = "SELECT * FROM products WHERE name ILIKE ?";
    private static final String UPDATE_PRODUCT = "UPDATE products SET name = ?, description = ?, producer = ?, amount = ?, price = ?, category_id = ? WHERE id = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM products WHERE id = ?";
    private static final String FIND_ALL_PRODUCTS_EXT = """
            SELECT products.id, products.name, products.description, products.producer, products.amount, products.price, categories.name AS category_name
            FROM products
            INNER JOIN categories
            ON products.category_id = categories.id
            """;
    private static final String FIND_ALL_PRODUCTS_WITH_PRICE = """
            SELECT products.id, products.name, products.description, products.producer, products.amount, products.price, categories.name AS category_name, products.amount * products.price AS total_price
            FROM products
            INNER JOIN categories
            ON products.category_id = categories.id
            GROUP BY products.id, products.name, products.description, products.producer, products.amount, products.price, categories.name
            """;
    private static final String FIND_PRODUCT_WITH_PRICE = """
            SELECT products.id, products.name, products.description, products.producer, products.amount, products.price, categories.name AS category_name, products.amount * products.price AS total_price
            FROM products
            INNER JOIN categories
            ON products.category_id = categories.id
            WHERE products.id = ?
            GROUP BY products.id, products.name, products.description, products.producer, products.amount, products.price, categories.name
            """;
    private static final String FIND_ALL_CATEGORY_PRODUCTS = """
            SELECT *
            FROM products
            INNER JOIN categories
            ON categories.id = products.category_id
            WHERE categories.id = ?
            """;

    private final Connection connection;

    public ProductRepository(Connection connection) {
        this.connection = connection;
    }

    public Product createProduct(ProductCreationDto product) {
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
                    throw new SQLException("Error saving product");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving product", e);
        }
    }

    public List<Product> findAllProducts() {
        try (Statement statement = connection.createStatement()) {
            return mapToProductsList(statement.executeQuery(FIND_ALL_PRODUCTS));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all products", e);
        }
    }

    public List<Product> findAllProductsByCategory(Integer categoryId) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_CATEGORY_PRODUCTS)) {
            statement.setInt(1, categoryId);
            return mapToProductsList(statement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all products ext", e);
        }
    }

    public List<ProductExtDto> findAllProductsExt() {
        try (Statement statement = connection.createStatement()) {
            return mapToProductsListExt(statement.executeQuery(FIND_ALL_PRODUCTS_EXT));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all products ext", e);
        }
    }

    public List<ProductPriceDto> findAllProductsWithPrice() {
        try (Statement statement = connection.createStatement()) {
            return mapToProductsPriceList(statement.executeQuery(FIND_ALL_PRODUCTS_WITH_PRICE));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all products with price", e);
        }
    }

    public Optional<Product> findProductByName(String name) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME_LIKE)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToProduct(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding products by name", e);
        }
    }

    public List<Product> findProductsWhereNameLike(String name) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME_STARTS_WITH)) {
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();
            return mapToProductsList(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding products where name", e);
        }
    }

    public Optional<Product> findProductById(Integer productId) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToProduct(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding product by id", e);
        }
    }

    public Optional<ProductPriceDto> findProductWithPriceById(Integer productId) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_PRODUCT_WITH_PRICE)) {
            statement.setInt(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToProductPrice(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding product by id", e);
        }
    }

    public void updateProduct(Product product) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT)) {
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setString(3, product.getProducer());
            statement.setInt(4, product.getAmount());
            statement.setDouble(5, product.getPrice());
            statement.setInt(6, product.getCategoryId());
            statement.setInt(7, product.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    public void deleteProduct(int id) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    private List<Product> mapToProductsList(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(mapToProduct(resultSet));
        }
        return products;
    }

    private List<ProductExtDto> mapToProductsListExt(ResultSet resultSet) throws SQLException {
        List<ProductExtDto> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(mapToProductExt(resultSet));
        }
        return products;
    }

    private List<ProductPriceDto> mapToProductsPriceList(ResultSet resultSet) throws SQLException {
        List<ProductPriceDto> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(mapToProductPrice(resultSet));
        }
        return products;
    }

    private ProductPriceDto mapToProductPrice(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String producer = resultSet.getString("producer");
        int amount = resultSet.getInt("amount");
        double price = resultSet.getDouble("price");
        String categoryName = resultSet.getString("category_name");
        double totalPrice = resultSet.getDouble("total_price");
        return new ProductPriceDto(id, name, description, producer, price, amount, categoryName, totalPrice);
    }

    private ProductExtDto mapToProductExt(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String producer = resultSet.getString("producer");
        int amount = resultSet.getInt("amount");
        double price = resultSet.getDouble("price");
        String categoryName = resultSet.getString("category_name");
        return new ProductExtDto(id, name, description, producer, price, amount, categoryName);
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
