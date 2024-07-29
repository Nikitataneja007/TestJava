import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public static void main(String[args])
{
public class UnsafeQueryServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;

        try {
            // Get username from parameters
            String username = request.getParameter("username");

            // Establish database connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Create a statement from database connection
            statement = connection.createStatement();

            // Create unsafe query by concatenating user defined data with query string
            String query = "SELECT secret FROM Users WHERE (username = '" + username + "' AND NOT role = 'admin')";

            // Insecurely format the query string using user defined data
            // String query = String.format("SELECT secret FROM Users WHERE (username = '%s' AND NOT role = 'admin')", username);

            // Execute query and return the results
            result = statement.executeQuery(query);

            // Process the results
            while (result.next()) {
                String secret = result.getString("secret");
                response.getWriter().println("Secret: " + secret);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (result != null) result.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
}
