package ua.edu.ukma.repositories;

import ua.edu.ukma.dto.category.CategoryCreationDto;
import ua.edu.ukma.dto.category.CategoryPriceDto;

import ua.edu.ukma.entities.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepository {
    private static final String SAVE_CATEGORY = "INSERT INTO categories (name, description) VALUES (?, ?)";
    private static final String UPDATE_CATEGORY = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM categories WHERE id = ?";
    private static final String FIND_ALL_CATEGORIES = "SELECT * FROM categories";
    private static final String FIND_CATEGORY_BY_ID = "SELECT * FROM categories WHERE id = ?";
    private static final String FIND_CATEGORY_BY_NAME = "SELECT * FROM categories WHERE name ILIKE ?";
    private static final String FIND_BY_NAME_STARTS_WITH = "SELECT * FROM categories WHERE name ILIKE ?";
    private static final String FIND_ALL_CATEGORIES_WITH_PRICE = """
    SELECT categories.id, categories.name, categories.description, SUM(products.amount * products.price) AS price
    FROM categories
    INNER JOIN products 
    ON categories.id = products.category_id
    GROUP BY categories.id, categories.name, categories.description
    """;
    private static final String FIND_CATEGORY_WITH_PRICE = """
    SELECT categories.id, categories.name, categories.description, SUM(COALESCE(products.amount, 0) * COALESCE(products.price, 0)) AS price
    FROM categories
    LEFT JOIN products
    ON categories.id = products.category_id
    WHERE categories.id = ?
    GROUP BY categories.id, categories.name, categories.description
    """;
    private final Connection connection;

    public CategoryRepository(Connection connection) {
        this.connection = connection;
    }

    public Category createCategory(CategoryCreationDto categoryDto) {
        try (PreparedStatement statement = connection.prepareStatement(SAVE_CATEGORY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, categoryDto.getName());
            statement.setString(2, categoryDto.getDescription());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt("id");
                    return new Category(id, categoryDto.getName(), categoryDto.getDescription());
                } else {
                    throw new SQLException("Error saving category");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving category", e);
        }
    }

    public void deleteCategory(int categoryId) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_CATEGORY)) {
            statement.setInt(1, categoryId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting category", e);
        }
    }

    public Category updateCategory(int categoryId, Category categoryDto) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_CATEGORY)) {
            statement.setString(1, categoryDto.getName());
            statement.setString(2, categoryDto.getDescription());
            statement.setInt(3, categoryId);
            statement.executeUpdate();
            return new Category(categoryId, categoryDto.getName(), categoryDto.getDescription());
        } catch (SQLException e) {
            throw new RuntimeException("Error updating category", e);
        }
    }

    public List<Category> findAllCategories() {
        try (Statement statement = connection.createStatement()) {
            return mapToCategoryList(statement.executeQuery(FIND_ALL_CATEGORIES));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all categories", e);
        }
    }

    public List<CategoryPriceDto> findAllCategoriesPrice() {
        try (Statement statement = connection.createStatement()) {
            return mapToCategoryListWithPrice(statement.executeQuery(FIND_ALL_CATEGORIES_WITH_PRICE));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all categories with price", e);
        }
    }

    public Optional<CategoryPriceDto> findCategoryWithPriceById(Integer categoryId) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_CATEGORY_WITH_PRICE)) {
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToCategoryWithPrice(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding category by id", e);
        }
    }

    public Optional<Category> findCategoryByName(String name) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_CATEGORY_BY_NAME)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToCategory(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding categories by name", e);
        }
    }

    public List<Category> findCategoriesWhereNameLike(String name) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME_STARTS_WITH)) {
            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();
            return mapToCategoryList(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding categories by name", e);
        }
    }

    public Optional<Category> findCategoryById(Integer id) {
        try (PreparedStatement statement = connection.prepareStatement(FIND_CATEGORY_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToCategory(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding category by id", e);
        }
    }

    private List<Category> mapToCategoryList(ResultSet resultSet) throws SQLException {
        List<Category> categories = new ArrayList<>();
        while (resultSet.next()) {
            categories.add(mapToCategory(resultSet));
        }
        return categories;
    }

    private List<CategoryPriceDto> mapToCategoryListWithPrice(ResultSet resultSet) throws SQLException {
        List<CategoryPriceDto> categories = new ArrayList<>();
        while (resultSet.next()) {
            categories.add(mapToCategoryWithPrice(resultSet));
        }
        return categories;
    }

    private Category mapToCategory(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        return new Category(id, name, description);
    }

    private CategoryPriceDto mapToCategoryWithPrice(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        double price = resultSet.getDouble("price");
        return new CategoryPriceDto(id, name, description, price);
    }
}
