/* 
 * Copyright (C) 2017 Paul Max Avalos Aguilar at S.D.T. pauldromeasaurio@hotmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package MathClassses;

import com.sdt.Datos.Datos;
import com.sdt.Datos.Distribuciones;
import com.sdt.Datos.Frecuencias;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author Paul Max Avalos Aguilar at S.D.T. pauldromeasaurio@hotmail.com
 */
public class MathDisplay {

    private final TextArea datosGarea;
    private TableView tablafrec;
    private TableView tabladist;
    private TableColumn<Frecuencias, Integer> notablafrec;
    private TableColumn<Frecuencias, String> limitablafrec;
    private TableColumn<Frecuencias, String> limstablafrec;
    private TableColumn<Frecuencias, String> fabstablafrec;
    private TableColumn<Frecuencias, String> freltablafrec;
    private TableColumn<Distribuciones, Integer> notabladist;
    private TableColumn<Distribuciones, String> valortabladist;
    private TableColumn<Distribuciones, String> dacumabstabladist;
    private TableColumn<Distribuciones, String> dacumreltabladist;

    private final List<Datos> list;
    private DescriptiveStatistics stats;

    private final String nombre;
    private boolean ceil;

    //variables para charts
    private List<Double> valoresAc;
    private List<String> intervalos;
    private List<Double> valoresRel;

    private List<Double> distacAbs;
    private List<Double> distacRel;
    private List<String> intervalosdist;

    private boolean hayDatos;

    public MathDisplay(TextArea text, List<Datos> list,
            String nombre, TableView tablafrec, TableColumn<Frecuencias, Integer> notablafrec,
            TableColumn<Frecuencias, String> limitablafrec,
            TableColumn<Frecuencias, String> limstablafrec,
            TableColumn<Frecuencias, String> fabstablafrec,
            TableColumn<Frecuencias, String> freltablafrec, 
            TableView tabladist,
            TableColumn<Distribuciones, Integer> notabladist,
            TableColumn<Distribuciones, String> valortabladist,
            TableColumn<Distribuciones, String> dacumabstabladist,
            TableColumn<Distribuciones, String> dacumreltabladist) {
        this.datosGarea = text;
        this.list = list;
        this.nombre = nombre;
        this.tablafrec = tablafrec;
        this.notablafrec = notablafrec;
        this.limitablafrec = limitablafrec;
        this.limstablafrec = limstablafrec;
        this.fabstablafrec = fabstablafrec;
        this.freltablafrec = freltablafrec;
        this.notabladist = notabladist;
        this.valortabladist = valortabladist;
        this.dacumabstabladist = dacumabstabladist;
        this.dacumreltabladist = dacumreltabladist;
        this.tabladist = tabladist;
        calculate();

    }

    public void calculate() {
        if (!(list.isEmpty())) {

            this.hayDatos = true;

            String textAppend;
            List<Double> modeList = new ArrayList();
            stats = new DescriptiveStatistics();

            for (int counter = 0; counter < list.size(); counter++) {

                double d = list.get(counter).getNumero();
                stats.addValue(d);
                modeList.add(d);
            }

            textAppend = String.format("Datos calculados del workspace: %S\n", nombre);
            datosGarea.appendText(textAppend);

            double numero_valores = stats.getN();
            textAppend = String.format("El numero de valores es: %1.0f\n", numero_valores);
            datosGarea.appendText(textAppend);

            double minValue = stats.getMin();
            double maxValue = stats.getMax();
            textAppend = String.format("El valor mas grande es: %.2f\nEl valor mas pequeño es: %.2f\n",
                    maxValue, minValue);
            datosGarea.appendText(textAppend);

            double range = Math.abs(maxValue - minValue);
            textAppend = String.format("El valor del rango es: %.2f\n", range);
            datosGarea.appendText(textAppend);

            Double[] arr = modeList.toArray(new Double[modeList.size()]);
            List<Double> modes = getMode(arr);

            textAppend = String.format("\n\n");
            datosGarea.appendText(textAppend);
            for (Double number : modes) {
                textAppend = String.format("La moda es %.2f\n", number);
                datosGarea.appendText(textAppend);
            }
            textAppend = String.format("\n\n");
            datosGarea.appendText(textAppend);

            double mean = stats.getMean();
            textAppend = String.format("La mediana es: %.6f\n", mean);
            datosGarea.appendText(textAppend);

            double median = stats.getPercentile(50);
            textAppend = String.format("La media es: %.6f\n", median);
            datosGarea.appendText(textAppend);

            double standardDeviation = stats.getStandardDeviation();
            textAppend = String.format("La desviacion estandar es: %.6f\n", standardDeviation);
            datosGarea.appendText(textAppend);

            double desviacion_aprox = range / 4;
            textAppend = String.format("El desviacion aproximada es de %.2f\n", desviacion_aprox);
            datosGarea.appendText(textAppend);

            double variance = stats.getVariance();
            textAppend = String.format("La varianza es: %.6f\n", variance);
            datosGarea.appendText(textAppend);

            double sesgo = stats.getSkewness();
            textAppend = String.format("El sesgo es: %.6f\n", sesgo);
            datosGarea.appendText(textAppend);

            double reglaSesentayCincoBajo = mean - standardDeviation;
            textAppend = String.format("El limite inferior al 65%% es %.2f\n", reglaSesentayCincoBajo);
            datosGarea.appendText(textAppend);

            double reglaSesentayCincoAlto = mean + standardDeviation;
            textAppend = String.format("El limite superior al 65%% es %.2f\n", reglaSesentayCincoAlto);
            datosGarea.appendText(textAppend);

            double reglaNoventayCincoBajo = mean - (standardDeviation * 2);
            textAppend = String.format("El limite inferior al 95%% es %.2f\n", reglaNoventayCincoBajo);
            datosGarea.appendText(textAppend);

            double reglaNoventayCincoAlto = mean + (standardDeviation * 2);
            textAppend = String.format("El limite superior al 95%% es %.2f\n", reglaNoventayCincoAlto);
            datosGarea.appendText(textAppend);

            double coefic_var = standardDeviation / mean;
            textAppend = String.format("El coeficiente de variacion es %.2f\n", coefic_var);
            datosGarea.appendText(textAppend);

            interpreta(sesgo, mean, median, standardDeviation);
            /*
            
            
            
            
            
            
            
            
            
             */
            //SEGUNDA PARTE
            /*
            
            
            
            
            
            
            
             */
            textAppend = String.format("\n\nAnalisis de frecuencias\n\n");
            datosGarea.appendText(textAppend);
            int numero_clase;
            for (numero_clase = 0;; numero_clase++) {

                double square = Math.pow(2, numero_clase);
                textAppend = String.format("%d.- %.2f\n", numero_clase, square);
                datosGarea.appendText(textAppend);
                if (square > numero_valores) {
                    textAppend = String.format("%.2f es mayor que el numero de datos\n", square);
                    datosGarea.appendText(textAppend);
                    break;

                }

            }

            textAppend = String.format("\nEl numero de clases es %d\n", numero_clase);
            datosGarea.appendText(textAppend);

            /*
            
             */
            //Division decimal y gestion de redondeo
            BigDecimal rangedecimal = new BigDecimal(range);
            BigDecimal n_clasedecimal = new BigDecimal(numero_clase);

            BigDecimal largoClasedecimal = new BigDecimal(0);
            try {
                largoClasedecimal = rangedecimal.divide(n_clasedecimal);
            } catch (ArithmeticException ex) {
                largoClasedecimal = rangedecimal.divide(n_clasedecimal, 2, RoundingMode.CEILING);
                ceil = true;
            }

            double largoClase = largoClasedecimal.doubleValue();
            //Fin de division decimal y gestion de redondeo
            /*
            
            
             */
            textAppend = String.format("El largo de clase es %.2f\n", largoClase);
            datosGarea.appendText(textAppend);

            //Listas para charts
            List<Double> valoresAc = new ArrayList<>();
            List<Double> valoresRel = new ArrayList<>();
            List<String> intervalos = new ArrayList<>();
            setFrecuenciaAbs(valoresAc);
            setFrecuenciaRel(valoresRel);
            setIntervalos(intervalos);

            final ObservableList<Frecuencias> datosFrec = FXCollections.observableArrayList();

            double limiteInferior = minValue;
            for (int i = 0; i < numero_clase; i++) {

                double limiteSuperior;
                double siguienteValor;
                double frecuencia;
                double relativa;
                Frecuencias frecuencias = new Frecuencias();

                if (i == numero_clase - 1) {
                    if (ceil == true) {
                        limiteSuperior = maxValue;
                        siguienteValor = limiteSuperior;
                        frecuencia = 0;
                        for (Double d : modeList) {
                            if ((d >= limiteInferior) && (d <= siguienteValor)) {
                                ++frecuencia;
                            }
                        }
                    } else {
                        limiteSuperior = limiteInferior + largoClase;
                        siguienteValor = limiteSuperior;
                        frecuencia = 0;
                        for (Double d : modeList) {
                            if ((d >= limiteInferior) && (d <= siguienteValor)) {
                                ++frecuencia;
                            }
                        }
                    }
                } else {
                    limiteSuperior = limiteInferior + largoClase;
                    siguienteValor = limiteSuperior - .01;
                    frecuencia = 0;
                    for (Double d : modeList) {
                        if ((d >= limiteInferior) && (d <= siguienteValor)) {
                            ++frecuencia;
                        }
                    }
                }
                //Concatenacion para graficacion
                String a = String.format("%.1f-", limiteInferior);
                String b = String.format("%.1f", limiteSuperior);
                relativa = (frecuencia / numero_valores) * 100;

                /*
                Set of values for display
                
                 */
                DecimalFormat fE = new DecimalFormat("#.####");

                frecuencias.setNumero(i + 1);
                frecuencias.setFrecAbs(fE.format(frecuencia));
                frecuencias.setFrecRel(String.format("%.2f%%", relativa));
                frecuencias.setLimiteInferior(fE.format(limiteInferior));
                frecuencias.setSiguienteValor(fE.format(siguienteValor));
                valoresAc.add(frecuencia);
                valoresRel.add(relativa);
                datosFrec.add(frecuencias);
                StringBuilder sb = new StringBuilder();
                sb.append(a).append(b);
                intervalos.add(sb.toString());

                limiteInferior = limiteSuperior;
            }

            //asignacion de columnas
            notablafrec.setCellValueFactory(new PropertyValueFactory<Frecuencias, Integer>(
                    "numero"));
            limitablafrec.setCellValueFactory(new PropertyValueFactory<Frecuencias, String>(
                    "limiteInferior"));
            limstablafrec.setCellValueFactory(new PropertyValueFactory<Frecuencias, String>(
                    "siguienteValor"));
            fabstablafrec.setCellValueFactory(new PropertyValueFactory<Frecuencias, String>(
                    "frecAbs"));
            freltablafrec.setCellValueFactory(new PropertyValueFactory<Frecuencias, String>(
                    "frecRel"));
            tablafrec.setItems(datosFrec);

            /*
            
            
            
            
            
            
            
            
             */
            //TERCERA PARTE
            /*
            
            
            
            
            
            
            
             */
            //Listas para charts
            List<Double> distacAbs = new ArrayList<>();
            List<Double> distacRel = new ArrayList<>();
            List<String> intervalosdist = new ArrayList<>();
            setdistacAbs(distacAbs);
            setdistacRel(distacRel);
            setintervalosdist(intervalosdist);
            
            final ObservableList<Distribuciones> datosDistr = FXCollections.observableArrayList();
            
            double frecAc = 0;
            double frecRel = 0;
            limiteInferior = minValue;
            for (int i = 0; i < numero_clase + 1; i++) {
                double limiteSuperior = limiteInferior + largoClase;
                Distribuciones dist = new Distribuciones();

                if (i == 0) {
                    frecAc = 0;
                    frecRel = 0;
                    limiteInferior = minValue;

                } else if (i > 0) {
                    if (i == (numero_clase + 1) - 1) {
                        limiteInferior = maxValue;
                        frecAc += valoresAc.get(i - 1);
                        frecRel = (frecAc / numero_valores) * 100;
                    } else {
                        frecAc += valoresAc.get(i - 1);
                        frecRel = (frecAc / numero_valores) * 100;
                    }

                }

                DecimalFormat fE = new DecimalFormat("#.####");
                dist.setNumero(i + 1);
                dist.setValor(fE.format(limiteInferior));
                dist.setDistribucionacum(fE.format(frecAc));
                dist.setDistribucionrelativa(String.format("%.2f%%", frecRel));
                distacRel.add(frecRel);
                distacAbs.add(frecAc);
                datosDistr.add(dist);

                StringBuilder sb = new StringBuilder();
                sb.append("<").append(String.format("%.2f", limiteInferior));
                intervalosdist.add(sb.toString());

                limiteInferior = limiteSuperior;
            }
            limiteInferior = minValue;
            
            notabladist.setCellValueFactory(new PropertyValueFactory<Distribuciones, Integer>(
                    "numero"));
            valortabladist.setCellValueFactory(new PropertyValueFactory<Distribuciones, String>(
                    "valor"));
            dacumabstabladist.setCellValueFactory(new PropertyValueFactory<Distribuciones, String>(
                    "distribucionacum"));
            dacumreltabladist.setCellValueFactory(new PropertyValueFactory<Distribuciones, String>(
                    "distribucionrelativa"));
            tabladist.setItems(datosDistr);

        } else {
            datosGarea.appendText("No hay valores\n");
            this.hayDatos = false;
            tablafrec.getItems().clear();
            tabladist.getItems().clear();
        }

    }

    private void interpreta(double sesgo, double mean, double median, double standardDeviation) {
        boolean simetrico;
        String textAppend;

        textAppend = String.format("\n\nInterpretacion de la grafica\n\n");
        datosGarea.appendText(textAppend);
        if ((sesgo > .5) || (sesgo < -.5)) {
            textAppend = String.format("1.-La grafica no es simetrica\n");
            simetrico = false;

            datosGarea.appendText(textAppend);
            if (sesgo > .5) {
                textAppend = String.format("La grafica es asimetrica hacia la izquierda\n");
                datosGarea.appendText(textAppend);
            } else if (sesgo < -.5) {
                textAppend = String.format("La grafica es asimetrica hacia la derecha\n");
                datosGarea.appendText(textAppend);
            }
        } else {
            textAppend = String.format("1.-La grafica es simetrica\n");
            datosGarea.appendText(textAppend);
            simetrico = true;
        }

        double rendimiento = (((mean - median) / median) * 100);
        textAppend = String.format("2.-El rendimiento media-mediana es de %.2f%%", rendimiento);
        datosGarea.appendText(textAppend);
        if ((rendimiento > 5) || (rendimiento < -5)) {
            textAppend = String.format(" por lo tanto la media no es representativa\nporque su valor"
                    + " no es semejante al de la mediana por lo tanto no tipitfica al conjunto de datos\n");
            datosGarea.appendText(textAppend);
        } else {
            textAppend = String.format(" por lo tanto la media es representativa\nporque su valor"
                    + " no es semejante al de la media por lo tanto tipifica al conjunto de datos\n");
            datosGarea.appendText(textAppend);
        }

        if (simetrico == true) {
            textAppend = String.format("3.-La regla empirica se cumple porque la grafica es simetrica\n");
            datosGarea.appendText(textAppend);
        } else {
            textAppend = String.format("3.-La regla empirica no se cumple porque la grafica no es simetrica\n");
            datosGarea.appendText(textAppend);
        }

        textAppend = String.format("4.-La dispercion de mis datos con respecto a la media es de %.2f\n", standardDeviation);
        datosGarea.appendText(textAppend);
        textAppend = String.format("Si yo tomo un dato cualquiera va a tener esa dispercion promedio\n");
        datosGarea.appendText(textAppend);
        textAppend = String.format("El valor real va a ser cualquier numero menos la media\n");
        datosGarea.appendText(textAppend);
        textAppend = String.format("5.-El coeficiente de variacion puede que sea simetrico pero con unos datos "
                + "muy separados esto va relacionado con el ancho de la grafica\n");
        datosGarea.appendText(textAppend);
        textAppend = String.format("6.- Si la desviacion estandar aproximada es muy distante de la  "
                + "desviacion estandar la desviacion estandar aproximada no es confiable\n");
        datosGarea.appendText(textAppend);
    }

    public List getMode(Double[] m) {
        HashMap<Double, Double> freqs = new HashMap<Double, Double>();
        for (double d : m) {
            Double freq = freqs.get(d);
            freqs.put(d, (freq == null ? 1 : freq + 1));
        }
        List<Double> mode = new ArrayList<Double>();
        List<Double> frequency = new ArrayList<Double>();
        List<Double> values = new ArrayList<Double>();

        for (Map.Entry<Double, Double> entry : freqs.entrySet()) {
            frequency.add(entry.getValue());
            values.add(entry.getKey());
        }

        if (!(frequency.isEmpty())) {
            double max = Collections.max(frequency);

            for (int i = 0; i < frequency.size(); i++) {
                double val = frequency.get(i);
                if (max == val) {
                    mode.add(values.get(i));
                }
            }
        }

        return mode;
    }

    private void setFrecuenciaAbs(List<Double> valoresAc) {
        this.valoresAc = valoresAc;
    }

    public List<Double> getFrecuenciaAbs() {
        return this.valoresAc;
    }

    private void setIntervalos(List<String> intervalos) {
        this.intervalos = intervalos;
    }

    public List<String> getIntervalos() {
        return intervalos;
    }

    private void setFrecuenciaRel(List<Double> valoresRel) {
        this.valoresRel = valoresRel;
    }

    public List<Double> getFrecuenciaRel() {
        return valoresRel;
    }

    private void setdistacAbs(List<Double> distacAbs) {
        this.distacAbs = distacAbs;
    }

    public List<Double> getdistacAbs() {
        return this.distacAbs;
    }

    private void setdistacRel(List<Double> distacRel) {
        this.distacRel = distacRel;
    }

    public List<Double> getdistacRel() {
        return this.distacRel;
    }

    private void setintervalosdist(List<String> intervalosdist) {
        this.intervalosdist = intervalosdist;
    }

    public List<String> getintervalosdist() {
        return this.intervalosdist;
    }

    public boolean hayDatos() {
        return this.hayDatos;
    }
}
