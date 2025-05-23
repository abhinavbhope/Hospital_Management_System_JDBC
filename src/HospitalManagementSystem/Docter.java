package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Docter {
    private Connection connection;

    public Docter(Connection connection) {
        this.connection = connection;
    }
    public void viewDocters() {
        String query = "select * from docters";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctor:");
            System.out.println("+------------+-------------------+---------------------+");
            System.out.println("| Docters id | Name              | Specialzation       |");
            System.out.println("+------------+-------------------+---------------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("| %-10s | %-16s  | %-19s |\n", id, name, specialization);
                System.out.println("+------------+-------------------+---------------------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDocterById(int id) {
        String query = "SELECT * FROM DOCTERS WHERE ID = ?";
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
