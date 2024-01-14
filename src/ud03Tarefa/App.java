package ud03Tarefa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jose
 */
public class App {

    final static Scanner in = new Scanner(System.in);
    static Connection conexion;
    static Statement sentencia;
    static ResultSet resultado;

    public static void main(String[] args) {
        conexion = creaConexion();
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
        pecharFluxos();
    }

    static Connection creaConexion() {
        System.out.println("Introduce porto no que está a Base de Datos á escoita. Por defecto é 1521, se non o cambiaches introduce ese.");
        int porto = in.nextInt();
        final String conector = "jdbc:oracle:thin:@localhost:"+porto+":XE";        

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
            sentencia = conexion.createStatement();
            resultado = sentencia.executeQuery(consulta);
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
        System.out.println("Día e Hora de saída");
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
            sentencia = conexion.createStatement();
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
            sentencia = conexion.createStatement();
            sentencia.execute (borra);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    static void modificaFumador(Connection conexion){
        System.out.println("Elixe opción:"
        +"\n[A]ctualizar un voo concreto: quitar tódalas plazas de fumador"
        +"\nA[c]tualizar un voo concreto: igualar as plazas de fumadores e non fumadores"
        + "\n[E]liminar tódalas plazas de fumadores de tódolos voos"
        + "\n[I]gualar as plazas de fumadores coas de non fumadores en tódolos voos"
        );
        char opcion = in.next().toLowerCase().charAt(0);
        
        switch (opcion){
            case 'a':                
                actualizaFumadorVoo(conexion, true);
                break;
            case 'c':
                actualizaFumadorVoo(conexion, false);
                break;
            case 'e':
                actualizaFumadorTodos(conexion, true);
                break;
            case 'i':
                actualizaFumadorTodos(conexion, false);
                break;
            default:
                break;
        }                
    }
    
    static int consultaPlazas(String voo){
        int [] plazas = new int [3];       
        try{
            String consulta = ("SELECT PLAZAS_FUMADOR FROM VUELOS WHERE COD_VUELO = '"+voo+"'");
            sentencia = conexion.createStatement();                    
            resultado = sentencia.executeQuery(consulta);
            resultado.next();
            int numFumadores= Integer.parseInt(resultado.getString(1));
            plazas[0]=numFumadores;
            System.out.println("Numero fumadores "+numFumadores);
            consulta = ("SELECT PLAZAS_NO_FUMADOR FROM VUELOS WHERE COD_VUELO = '"+voo+"'");
            resultado = sentencia.executeQuery(consulta);
            resultado.next();
            int numNoFumadores= Integer.parseInt(resultado.getString(1));
            plazas[1]=numNoFumadores;
            System.out.println("Numero no fumadores "+numNoFumadores);
            int plazasTotales = numFumadores + numNoFumadores;
            plazas[2]=plazasTotales;
            System.out.println("Plazas totales "+plazasTotales);
        }catch(Exception e){
            e.printStackTrace();
        }
        return plazas [2];
    }
    
    static void actualizaFumadorVoo(Connection conexion, boolean modo){
        System.out.println ("Indica voo a actualizar:");
        String voo = in.next();
        int plazasTotales = consultaPlazas(voo);  
        int plazasMetade = plazasTotales /2;
        int plazasFumador = modo?0:plazasMetade;
        int plazasNoFumador = modo?plazasTotales:plazasMetade;
        String actualizaFumador;
        String actualizaNoFumador;
        final String UPDATE = "UPDATE vuelos SET PLAZAS_FUMADOR = ";
        final String WHERE = "WHERE COD_VUELO = ";
        try {                   
            actualizaFumador = UPDATE+"'"+plazasFumador +"'"+ WHERE +"'"+voo+"'";            
            
            sentencia = conexion.createStatement();
            sentencia.execute(actualizaFumador);
            
            actualizaNoFumador = UPDATE + "'" + plazasNoFumador + "'" + WHERE +"'"+voo+"'";                
                             
            sentencia = conexion.createStatement();
            sentencia.execute(actualizaNoFumador);                    
        }catch (Exception e){
            e.printStackTrace();
        }
    }      
    
    static void actualizaFumadorTodos(Connection conexion, boolean modo){
        String consulta = "SELECT COD_VUELO, PLAZAS_FUMADOR, PLAZAS_NO_FUMADOR FROM VUELOS";        
        sentencia = conexion.createStatement();
        List <String> listaVuelos = new ArrayList<>();
        List <Integer>listaFumadores = new ArrayList<>();
        List <Integer> listaNoFumadores = new ArrayList<>();
        try {
            resultado = sentencia.executeQuery(consulta);
            while (resultado.next()){
                listaVuelos.add(resultado.getString(1));
                listaFumadores.add(Integer.parseInt(resultado.getString(2)));
                listaNoFumadores.add(Integer.parseInt(resultado.getString(3)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        for (int i = 0; i < listaVuelos.size(); i++){
            String actualizaFumador;
            String actualizaNoFumador;
            String voo = listaVuelos.get(i);
            int plazasTotalesVoo = listaFumadores.get(i) + listaNoFumadores.get(i);
            int plazasMetade = plazasTotalesVoo /2;
            int plazasFumador = modo? 0 : plazasMetade;
            int plazasNoFumador = modo? plazasTotalesVoo : plazasMetade;
           
            actualizaFumador = "UPDATE vuelos SET PLAZAS_FUMADOR = " + "'" + plazasFumador + "'" + " WHERE COD_VUELO = "+"'"+voo+"'"; 
            actualizaNoFumador = "UPDATE vuelos SET PLAZAS_NO_FUMADOR = " + "'" + plazasNoFumador + "'" + " WHERE COD_VUELO = "+"'"+voo+"'"; 
                       
            try{
                sentencia = conexion.createStatement();
                sentencia.execute(actualizaFumador);
                sentencia = conexion.createStatement();
                sentencia.execute(actualizaNoFumador);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }        
    }
    
    static void pecharFluxos(){
        try {
            conexion.close();
            sentencia.close();
            resultado.close();
            in.close();
        } catch (SQLException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}