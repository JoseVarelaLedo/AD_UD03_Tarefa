
package probas;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jose
 */
public class AppProbas {
    public static void main (String [] args){
        formateaData ("10/01/2024");
    }
    static String formateaData(String s) {
        String data = "";
        DateFormat formateador = new SimpleDateFormat("dd/M/yy");
        Date fecha;
        try {
            fecha = formateador.parse(s);            
            Calendar calendario = new GregorianCalendar();
            calendario.setTime(fecha);
            calendario.add(Calendar.DATE, 0);
           data =formateador.format(calendario.getTime());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
