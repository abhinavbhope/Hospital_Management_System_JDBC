package HospitalManagementSystem;
import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    public static final String url = "jdbc:mysql://localhost:3306/hospital";
    public static final String username = "root";
    public static final String password = "Abcd1234$";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patients patient = new Patients(connection, scanner);
            Docter docter = new Docter(connection);
            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1.ADD PATIENT");
                System.out.println("2.VIEW PATIENTS");
                System.out.println("3.VIEW DOCTORS");
                System.out.println("4.BOOK APPOINTMENT");
                System.out.println("5.EXIT");
                System.out.println("ENTER YOUR CHOICE: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        //add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //view patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        //view doctor
                        docter.viewDocters();
                        System.out.println();
                        break;
                    case 4:
                        //book appointment
                        bookAppointment(patient,scanner,docter,connection);
                        System.out.println();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("ENTER VALID CHOICE");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patients patients, Scanner scanner, Docter docter, Connection connection) {
        System.out.println("Enter Patient Id: ");
        int patientsId = scanner.nextInt();
        System.out.println("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.println("Enter Appointment Date (YYYY-MM-DD):");
        String appointmentData = scanner.next();
        if (patients.getPatientById(patientsId) && docter.getDocterById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentData, connection)) {
                String appointmentQuery = "INSERT INTO APPOINTMENTS(PATIENT_ID,DOCTER_ID,APPOINTMENT_DATE) VALUES(?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientsId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentData);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Appointment booked");
                    } else {
                        System.out.println("Failed to book an appointment");
                    }
                } catch (SQLException e) {

                }
            }

        } else {
            System.out.println("EITHER DOCTOR OR PATIENT DOESN'T EXIST");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM APPOINTMENTS WHERE DOCTOR_ID=? AND APPOINTMENT_DATE=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    return true;
                }
                else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
