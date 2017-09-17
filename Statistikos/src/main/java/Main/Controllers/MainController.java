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
package Main.Controllers;

import FXControllers.calculatorController;
import com.jfoenix.controls.JFXTextArea;
import com.sdt.Datos.Controllers.exceptions.NonexistentEntityException;
import com.sdt.Datos.Datos;
import com.sdt.Datos.dao.DatosDao;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author Paul Max Avalos Aguilar at S.D.T. pauldromeasaurio@hotmail.com
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane mainAnchor;

    private calculatorController con;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            URL location = getClass().getResource("/calculator.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

            Parent pn = (Parent) fxmlLoader.load(location.openStream());
            con = fxmlLoader.getController();
            con.doUpdate(0);
            mainAnchor.getChildren().addAll(pn);

        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addDataAction(ActionEvent event) {
        Dialog<String> dialog = new Dialog();
        dialog.setTitle("Add Data Dialog");
        dialog.setHeaderText("Enter your data:");
        ButtonType addButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        BorderPane pane = new BorderPane();
        JFXTextArea area = new JFXTextArea();
        pane.setCenter(area);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        area.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(pane);

        Platform.runLater(() -> area.requestFocus());

        dialog.setResultConverter((param) -> {
            if (param == addButtonType) {
                return "";
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        if ((result.isPresent())) {
            String sa = area.getText().replaceAll("(?m)^[ \t]*\r?\n", "");
            String[] s = sa.split("\\r?\\n");
            ArrayList<String> arrList = new ArrayList<>(Arrays.asList(s));

            DatosDao ddao = DatosDao.getInstance();
            Datos dato = new Datos();

            Task<Boolean> task = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    boolean err = false;
                    for (int i = 0; i < arrList.size(); i++) {

                        try {
                            double numero = Double.valueOf(arrList.get(i).replaceAll(",", "."));
                            dato.setNumero(numero);
                            ddao.updateRegistro(dato);

                        } catch (NumberFormatException ex) {
                            err = true;

                        } catch (Exception ex) {
                            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    return new Boolean(err);
                }
            };

            new Thread(task).start();

            task.setOnSucceeded((e) -> {
                boolean errors = task.getValue();
                if (errors) {

                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Errors in Data");
                    alert.setHeaderText(null);
                    alert.setContentText("Some values could not be parsed as numbers thus the "
                            + "program will give incorrect results");

                    alert.showAndWait();
                    con.hayValoresErr();
                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Data added");
                    alert.setHeaderText(null);
                    alert.setContentText("All data was added correctly");

                    alert.showAndWait();
                    
                    con.hayValores();
                }
            });

        }
    }

    @FXML
    private void deleteDataAction(ActionEvent event) {
        DatosDao ddao = DatosDao.getInstance();
        Datos dato = new Datos();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ddao.getAllRegistros().stream().forEach((t) -> {
                    try {
                        ddao.deleteRegistro(t.getId());
                    } catch (NonexistentEntityException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                return null;
            }

        };

        new Thread(task).start();
        task.setOnSucceeded((e) -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Deleted data");
            alert.setHeaderText(null);
            alert.setContentText("Data was succesfully deleted");
            alert.showAndWait();
            con.doUpdate(0);
        });

    }

    @FXML
    private void calculateAction(ActionEvent event) {
        Dialog<Integer> dialog = new Dialog();
            dialog.setTitle("Choice precission Dialog");
            dialog.setHeaderText("Enter desired decimals:");
            ButtonType addButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

            BorderPane pane = new BorderPane();
            ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
                    1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)
            );
            pane.setCenter(cb);

            Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
            addButton.setDisable(true);

            cb.getSelectionModel().selectFirst();

            dialog.getDialogPane().setContent(pane);
            addButton.setDisable(false);

            Platform.runLater(() -> cb.requestFocus());
            
            dialog.setResultConverter((param) -> {
                if (param == addButtonType) {
                    
                    return (int)cb.getValue();
                    
                }
                return null;
            });

            
            Optional<Integer> result = dialog.showAndWait();
            
            if(result.isPresent()){
                con.doUpdate((int)cb.getValue());
            }
        
        
        
    }
}
