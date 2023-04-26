package com.timecontrolgui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * JavaFX App
 */
public class App extends Application {

    public String defaultDirectory = getDocumentsFolder();

    @Override
    public void start(Stage stage) throws IOException {
        // create a label
        Label nomeLabel = new Label("Nome");
        TextField nomeTextField = new TextField();
        nomeTextField.setPromptText("Ex. João Pedro");
        nomeTextField.setPrefWidth(800);
        nomeLabel.setPrefWidth(80);

        Label setorLabel = new Label("Setor");
        TextField setorTextField = new TextField();
        setorTextField.setPromptText("Ex. Estágio TI");

        Label horaEntradaLabel = new Label("Hora de Entrada");
        TextField horaEntradaTextField = new TextField();
        horaEntradaTextField.setPromptText("Ex. 07");

        Label horaSaidaLabel = new Label("Hora de Saída");
        TextField horaSaidaTextField = new TextField();
        horaSaidaTextField.setPromptText("Ex. 12");

        Label feriadosLabel = new Label("Dias de Férias");
        TextField feriadosTextField = new TextField();
        feriadosTextField.setPromptText("Ex. 1, 2, 5, 7");

        Label pastaSalvarLabel = new Label("Pasta para salvar");
        DirectoryChooser pastaSalvarDirectoryChooser = new DirectoryChooser();
        pastaSalvarDirectoryChooser.setInitialDirectory(new File(defaultDirectory));
        Label pastaSelecionadaLabel = new Label(defaultDirectory);

        Button pastaSalvarButton = new Button("Selecionar pasta...");
        pastaSalvarButton.setOnAction(event -> {
            File selectedDirectory = pastaSalvarDirectoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                pastaSelecionadaLabel.setText(selectedDirectory.getAbsolutePath());
            }
        });

        Button criarFormulatiroButton = new Button("Criar Formulário");
        criarFormulatiroButton.setPrefWidth(500);
        criarFormulatiroButton.setOnAction(event -> {
            criarFormulario(
                    nomeTextField.getText(),
                    setorTextField.getText(),
                    feriadosTextField.getText(),
                    pastaSelecionadaLabel.getText(),
                    horaEntradaTextField.getText(),
                    horaSaidaTextField.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Time Control");
            alert.setHeaderText(null);
            alert.setContentText("Formulário criado com sucesso!");
            alert.show();
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        // Row 1
        gridPane.add(nomeLabel, 0, 0);
        gridPane.add(nomeTextField, 1, 0, 2, 1);
        // Row 2
        gridPane.add(setorLabel, 0, 1);
        gridPane.add(setorTextField, 1, 1, 2, 1);
        // Row 3
        gridPane.add(horaEntradaLabel, 0, 2);
        gridPane.add(horaEntradaTextField, 1, 2, 2, 1);
        // Row 4
        gridPane.add(horaSaidaLabel, 0, 3);
        gridPane.add(horaSaidaTextField, 1, 3, 2, 1);
        // Row 5
        gridPane.add(feriadosLabel, 0, 4);
        gridPane.add(feriadosTextField, 1, 4, 2, 1);
        // Row 6
        gridPane.add(pastaSalvarLabel, 0, 5);
        gridPane.add(pastaSalvarButton, 1, 5);
        gridPane.add(pastaSelecionadaLabel, 2, 5);
        // Row 7
        gridPane.add(criarFormulatiroButton, 0, 6, 3, 1);

        // create a scene with the layout container
        Scene scene = new Scene(gridPane, 500, 250);

        // set the scene on the stage and show it
        stage.getIcons()
                .add(new Image(getClass().getResourceAsStream("/com/timecontrolgui/icon.png")));
        stage.setResizable(false);
        stage.setTitle("Time Control");
        stage.setScene(scene);
        stage.show();

        criarFormulatiroButton.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void criarFormulario(String nome, String setor, String feriados, String pastaSalvar,
            String horaEntrada, String horaSaida) {
        System.out.println("Nome: " + nome);
        System.out.println("Setor: " + setor);
        System.out.println("Feriados: " + feriados);
        System.out.println("Pasta Salvar: " + pastaSalvar);

        String[] feriadosArray = feriados.split(",");
        int[] feriadosIntArray = new int[feriadosArray.length];
        for (int i = 0; i < feriadosArray.length; i++) {
            try {
                feriadosIntArray[i] = Integer.parseInt(feriadosArray[i].trim());
            } catch (Exception e) {
                feriadosIntArray[0] = 0;
                break;
            }
        }

        Form newForm = new Form(nome, setor, feriadosIntArray, pastaSalvar, horaEntrada, horaSaida);

        try {
            newForm.createForm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDocumentsFolder() {
        FileSystem fileSystem = FileSystems.getDefault();
        Path documentsDirectory = fileSystem.getPath(System.getProperty("user.home"), "Documents");
        String documentsPath = documentsDirectory.toString();

        return documentsPath;
    }
}