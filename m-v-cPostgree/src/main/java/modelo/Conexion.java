package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.VariablesConexionPostgreSQL;

public class Conexion {

	static VariablesConexionPostgreSQL vcpsql = new VariablesConexionPostgreSQL();
	static final String HOST = vcpsql.getUser();
	static final String PORT = vcpsql.getPort();
	static final String DB = vcpsql.getDb();
	static final String USER = vcpsql.getUser();
	static final String PASS = vcpsql.getPass();
	
	public Connection generaConexion(final String host, final String port, final String db, final String user, final String pass) {

		System.out.println("[INFORMACIÓN-conexionPostgresql-generaConexion] Entra en generaConexion");
		
        /*(Definición local) Definimos connection a null y url a vacío para 
         * asegurarnos de que ambas variables están limpias.
         */
        Connection conexion = null;
        String url = "";            
        url = "jdbc:postgresql://" + host + ":" + port + "/" + db;
		
        try {
        	
        	/*Class.forName obtiene una instancia de la clase de java especificada.
			*En este caso registra la clase como driver JDBC
			*/
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException cnfe) {
                System.out.println("[ERROR-conexionPostgresql-generaConexion] Error en registro driver PostgreSQL: " + cnfe);
            }
      
            //Conexión a la base de datos en PostgreSQL y validación de esta
            conexion = DriverManager.getConnection(url, user, pass);           
            boolean esValida = conexion.isValid(50000);
            if(esValida == false) {
            	conexion = null;
            }
            System.out.println(esValida ? "[INFORMACIÓN-conexionPostgresql-generaConexion] Conexión a PostgreSQL válida" : "[ERROR-conexionPostgresql-generaConexion] Conexión a PostgreSQL no válida");
            return conexion;
            
        } catch (java.sql.SQLException jsqle) {
        	
            System.out.println("[ERROR-conexionPostgresql-generaConexion] Error en conexión a PostgreSQL (" + url + "): " + jsqle);
            return conexion;
            
        }
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Statement declaracionSQL = null;
		ResultSet resultadoConsulta = null;
		
		/*Se crea una instancia de la clase en la que estamos para poder generar la conexión a PostgreSQL
		*utilizando el método generaConexion
		*/
		Conexion Conexion = new Conexion();
		Connection conexionGenerada = Conexion.generaConexion(HOST,PORT,DB,USER,PASS);
		
		System.out.println("[INFORMACIÓN-conexionPostgresql-main] Realiza consulta a PostgreSQL");
		if(conexionGenerada != null) {
			
			try {
				
				//Se abre una declaración
				declaracionSQL = conexionGenerada.createStatement();
				//Se define la consulta de la declaración y se ejecuta
				resultadoConsulta = declaracionSQL.executeQuery("SELECT * FROM \"proyectoEclipse\".\"alumnos\"");
			    
				//Leemos el resultado de la consulta hasta que no queden filas
				while ( resultadoConsulta.next() ) {

			          int idAlumno = resultadoConsulta.getInt("id_alumno");
			          String  nombre = resultadoConsulta.getString("nombre");
			          String apellidos  = resultadoConsulta.getString("apellidos");
			          String email  = resultadoConsulta.getString("email");
			          System.out.printf( "Identificador alumno: %s , nombre: %s, apellidos: %s , e-mail: %s",
			        		  idAlumno, nombre, apellidos, email);
			          System.out.println();

			    }

				System.out.println("[INFORMACIÓN-conexionPostgresql-main] Cierre conexión, declaración y resultado");				
			    resultadoConsulta.close();
			    declaracionSQL.close();
			    conexionGenerada.close();
				
			} catch (SQLException e) {
				
				System.out.println("[ERROR-conexionPostgresql-main] Error generando la declaracionSQL: " + e);
				
			}
		}
	}

}
