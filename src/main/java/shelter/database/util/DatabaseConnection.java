package shelter.database.util;

import java.sql.*;


public class DatabaseConnection {
    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    private static String url;

    public static Connection getConnection() {
        if (conn != null) {
            return conn;
        }
        url = "jdbc:sqlite:src/main/resources/animal.db";
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            createSchema();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static void createNewDatabase() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void connection(String path) throws ClassNotFoundException, SQLException {
        conn = null;
        url = "jdbc:sqlite:" + path;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection(url);
    }

    public static void createSchema() throws SQLException {
        statmt = conn.createStatement();

        resSet = statmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Category';");
        if (!resSet.next()) {
            statmt.execute("""
            create table main.Category
                (
                    id         INTEGER primary key AUTOINCREMENT,
                    timeCreate Date        not null,
                    name       Varchar(64) not null unique
                );
                """);
        }

        resSet = statmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Place';");
        if (!resSet.next()) {
            statmt.execute("""
            create table main.Place
                (
                    id          INTEGER primary key AUTOINCREMENT,
                    timeCreate  Date        not null,
                    number      INTEGER not null unique,
                    description VARCHAR(64) not null,
                    maxAnimal   INTEGER default 1
                );
                """);
        }

        resSet = statmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='Animal';");
        if (!resSet.next()) {
            statmt.execute("""
                    create table main.Animal
                        (
                            id          INTEGER primary key AUTOINCREMENT,
                            timeCreate  Date        not null,
                            CategoryId  INTEGER
                                        constraint table_name_Category_id_fk
                                        references Category,
                            PlaceId     INTEGER
                                        constraint table_name_Place_id_fk
                                        references Place,
                            name        Varchar(64) not null,
                            description Varchar(128),
                            vaccinated  boolean default false,
                            timeStart   Date,
                            timeEnd     Date
                        );
                        """);
        }

    }

//
//    // --------Создание таблицы--------
//    public static void CreateDB() throws ClassNotFoundException, SQLException
//    {
//        statmt = conn.createStatement();
//        statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'phone' INT);");
//
//        System.out.println("Таблица создана или уже существует.");
//    }
//
//    // --------Заполнение таблицы--------
//    public static void WriteDB() throws SQLException
//    {
//        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Petya', 125453); ");
//        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Vasya', 321789); ");
//        statmt.execute("INSERT INTO 'users' ('name', 'phone') VALUES ('Masha', 456123); ");
//
//        System.out.println("Таблица заполнена");
//    }
//
//    // -------- Вывод таблицы--------
//    public static void ReadDB() throws ClassNotFoundException, SQLException
//    {
//        resSet = statmt.executeQuery("SELECT * FROM users");
//
//        while(resSet.next())
//        {
//            int id = resSet.getInt("id");
//            String  name = resSet.getString("name");
//            String  phone = resSet.getString("phone");
//            System.out.println( "ID = " + id );
//            System.out.println( "name = " + name );
//            System.out.println( "phone = " + phone );
//            System.out.println();
//        }
//
//        System.out.println("Таблица выведена");
//    }
//
//    // --------Закрытие--------
//    public static void CloseDB() throws ClassNotFoundException, SQLException
//    {
//        conn.close();
//        statmt.close();
//        resSet.close();
//
//        System.out.println("Соединения закрыты");
//    }

}