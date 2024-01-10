package ud03Tarefa;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jose
 */
public class App {

    final static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        Connection conexion = creaConexion();
        if (conexion != null) {
            int opcion = menu();
            while (opcion != 0) {
                switch (opcion) {
                    case 1:
                        datosBD(conexion);
                        break;
                    case 2:
                        consultaTaboaPasaxeiros(conexion, true);
                        break;
                    case 3:
                        consultaTaboaPasaxeiros(conexion, false);
                        break;
                    case 4:
                        insertaVoo (conexion);
                        break;
                    case 5:
                        borraVoo(conexion);
                        break;
                    case 6:
                        modificaFumador(conexion);
                        break;
                    default:
                        break;
                }
                opcion = menu();
            }
        }
    }

    static Connection creaConexion() {
        final String conector = "jdbc:oracle:thin:@localhost:1539:XE";
        Connection conexion = null;

        try {
            System.out.println("Introduza nome de usuario para conectar coa base de datos");
            //String usuario = in.next();
            String usuario = "c##jose";
            System.out.println("Introduza contrasinal de usuario para conectar coa base de datos");
            //String password = in.next();
            String password = "chancludo";
            conexion = DriverManager.getConnection(conector, usuario, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conexion;
    }

    static int menu() {
        int opción;
        System.out.println("\n------------------------------\nOperación que desexas realizar:\n------------------------------");
        System.out.println(
                "Consultar os datos da Base de Datos\t\t Preme 1"
                + "\nConsultar os datos da táboa de PASAXEIROS\t Preme 2"
                + "\nConsultar datos pasaxeiro de voo\t\t Preme 3"
                + "\nInsertar datos dun novo voo\t\t\t Preme 4"
                + "\nBorrar o voo que se introduciu anteriormente\t Preme 5"
                + "\nModificar condición de fumadores en voo\t\t Preme 6"
                + "\n\nSaír, preme 0"
        );
        opción = in.nextInt();
        return opción;
    }

    static void datosBD(Connection con) {
        DatabaseMetaData datos;
        try {
            datos = con.getMetaData();
            System.out.println("Nome da BD: " + datos.getDatabaseProductName());
            System.out.println("Controlador: " + datos.getDriverName() + ", versión: " + datos.getDriverVersion());
            System.out.println("Nome do usuario: " + datos.getUserName());
            System.out.println("Dirección: " + datos.getURL());
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void consultaTaboaPasaxeiros(Connection conexion, boolean opcion) {
        String consulta = "";
        if (opcion) {
            consulta = "SELECT * FROM pasajeros";
        } else {
            System.out.println("Introduce número de voo");
            String voo = in.next();
            consulta = "SELECT * FROM pasajeros WHERE cod_vuelo=" + "\'" + voo + "\'";
        }

        try {
            Statement sentencia = conexion.createStatement();
            ResultSet resultado = sentencia.executeQuery(consulta);
            while (resultado.next()) {
                String codigoVoo = opcion ? "\tCódigo voo: " + resultado.getString(2) : "";
                System.out.println("Número pasaxeiro: " + resultado.getString(1)
                        + codigoVoo
                        + "\tTipo de plaza: " + resultado.getString(3)
                        + "\tFumador? " + resultado.getString(4));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    static void insertaVoo(Connection conexion) {
        System.out.println("Introduce datos do voo:");
        System.out.println("Código voo");
        String codVoo = in.next();
        System.out.println("Día eHora de saída");
        String horaSaida = in.next();
        System.out.println("Destino");
        String destino = in.next().toUpperCase();
        System.out.println("Procedencia");
        String procedencia = in.next().toUpperCase();
        System.out.println("Plazas de fumador");
        int pFumador = in.nextInt();
        System.out.println("Plazas de non fumador");
        int pNFumador = in.nextInt();
        System.out.println("Plazas de turista");
        int pTurista = in.nextInt();
        System.out.println("Plazas de primeira");
        int pPrimeira = in.nextInt();
        String inserta = "INSERT INTO vuelos "
                + "(COD_VUELO, HORA_SALIDA, DESTINO, PROCEDENCIA, PLAZAS_FUMADOR, PLAZAS_NO_FUMADOR, PLAZAS_TURISTA, PLAZAS_PRIMERA)"
                +" VALUES ("
                +"\'"+codVoo+"\',"
                +"\'"+horaSaida+"\',"
                +"\'"+destino+"\',"
                +"\'"+procedencia+"\',"
                +"\'"+pFumador+"\',"
                +"\'"+pNFumador+"\',"
                +"\'"+pTurista+"\',"
                +"\'"+pPrimeira+"\'"
                +")";
        try{
            Statement sentencia = conexion.createStatement();
            sentencia.execute (inserta);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }   
    
    static void borraVoo(Connection conexion){
         System.out.println("Introduce código do voo a borrar");
         String voo = in.next();
         String borra = "DELETE FROM vuelos WHERE cod_vuelo=" + "\'" + voo + "\'";
          try{
            Statement sentencia = conexion.createStatement();
            sentencia.execute (borra);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    static void modificaFumador(Connection con){
        
    }
}
