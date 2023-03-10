package shelter.database.repository.impl;

import shelter.database.exception.RepositoryException;
import shelter.database.models.Animal;
import shelter.database.models.Category;
import shelter.database.models.Place;
import shelter.database.repository.AnimalRepository;
import shelter.database.util.DatabaseConnection;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnimalRepositoryImpl implements AnimalRepository {
    final private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.ENGLISH);

    @Override
    public void save(Animal animal) {
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `Animal` (`CategoryId`, `PlaceId`, `name`, `description`, `vaccinated`, `timeStart`, `timeEnd`, `timeCreate`) VALUES (?, ?, ?, ?, ?, ?, ?, datetime('now'))", Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, animal.getCategoryId());
            statement.setInt(2, animal.getPlaceId());
            statement.setString(3, animal.getName());
            statement.setString(4, animal.getDescription());
            statement.setBoolean(5, Boolean.valueOf(animal.getVaccinated()));
            if (animal.getTimeStart() != null) {
                statement.setString(6, formatter.format(animal.getTimeStart()));
            } else {
                statement.setString(6, "");
            }
            if (animal.getTimeEnd() != null) {
                statement.setString(7, formatter.format(animal.getTimeEnd()));
            } else {
                statement.setString(7, "");
            }

            if (statement.executeUpdate() != 1) {
                throw new RepositoryException("Can't save Animal.");
            } else {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    animal.setId(generatedKeys.getInt(1));
//                    category.setTimeCreate(find(user.getId()).getCreationTime());
                } else {
                    throw new RepositoryException("Can't save Animal [no autogenerated fields].");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Animal> finAll() {
        List<Animal> animals = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Animal")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                Animal animal;
                while ((animal = toAnimal(statement.getMetaData(), resultSet)) != null) {
                    animals.add(animal);
                }
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException("Can't find Animals.", e);
        }

        return animals;
    }

    @Override
    public Animal find(Integer id) {
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Animal WHERE id=?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return toAnimal(statement.getMetaData(), resultSet);
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException("Can't find Category.", e);
        }
    }

    @Override
    public void delete(Integer id) {
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM main.Animal where id=?"))
        {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Can't delete Animal.", e);
        }
    }

    @Override
    public void setVaccinate(Integer id, Boolean value) {
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("UPDATE main.Animal SET vaccinated=? where id=?"))
        {
            statement.setBoolean(1, value);
            statement.setInt(2, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Can't change Animal.", e);
        }
    }

    private Animal toAnimal(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException, ParseException {
        if (!resultSet.next()) {
            return null;
        }
        Animal animal = new Animal();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id" -> animal.setId(resultSet.getInt(i));
                case "timeCreate" -> animal.setTimeCreate(formatter.parse(resultSet.getString(i)));

                case "CategoryId" -> animal.setCategoryId(resultSet.getInt(i));
                case "PlaceId" -> animal.setPlaceId(resultSet.getInt(i));
                case "name" -> animal.setName(resultSet.getString(i));
                case "description" -> animal.setDescription(resultSet.getString(i));
                case "vaccinated" -> animal.setVaccinated(resultSet.getBoolean(i));
                case "timeStart" -> {
                    if (resultSet.getString(i) != null && !resultSet.getString(i).equals("")) {
                        animal.setTimeStart(formatter.parse(resultSet.getString(i)));
                    }
                }
                case "timeEnd" -> {
                    if (resultSet.getString(i) != null && !resultSet.getString(i).equals("")) {
                        animal.setTimeEnd(formatter.parse(resultSet.getString(i)));
                    }
                }

                default -> {
                }
            }
        }

        return animal;
    }
}
