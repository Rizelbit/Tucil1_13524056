package tucil1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MainApp extends Application {

    private Stage primaryStage;
    private GridPane gridBoard;
    private Label labelStatus;
    private VBox placeholderBox;
    private Button btnSaveTxt;
    private Button btnSaveImg;

    private Board currSolvedBoard;
    private double currExecTime;
    private long currIterations;

    private final String FONT_FAMILY = "'Segoe UI', 'Helvetica Neue', 'Arial', sans-serif";

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #E5E7EB;");

        Label title = new Label("Queens LinkedIn Solver");
        title.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1F2937;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnFileChooser = new Button("📁 Pilih File Input");
        btnSaveTxt = new Button("💾 Simpan Teks");
        btnSaveImg = new Button("📷 Simpan Gambar");

        styleButton(btnFileChooser, "#0A66C2", "#004182");
        styleButton(btnSaveTxt, "#059669", "#047857");
        styleButton(btnSaveImg, "#4F46E5", "#4338CA");

        btnSaveTxt.setDisable(true);
        btnSaveImg.setDisable(true);

        buttonBox.getChildren().addAll(btnFileChooser, btnSaveTxt, btnSaveImg);

        StackPane centerArea = new StackPane();
        centerArea.setAlignment(Pos.CENTER);
        centerArea.setMinHeight(400);

        gridBoard = new GridPane();
        gridBoard.setAlignment(Pos.CENTER);

        DropShadow boardShadow = new DropShadow();
        boardShadow.setColor(Color.rgb(0, 0, 0, 0.2));
        boardShadow.setRadius(15);
        gridBoard.setEffect(boardShadow);
        gridBoard.setVisible(false);

        placeholderBox = new VBox(10);
        placeholderBox.setAlignment(Pos.CENTER);

        Label logoIcon = new Label("♛");
        logoIcon.setStyle("-fx-font-size: 120px; -fx-text-fill: #9CA3AF;");
        Label logoText = new Label("Belum ada papan yang dimuat.\nSilakan pilih file untuk memulai!");
        logoText.setTextAlignment(TextAlignment.CENTER);
        logoText.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-text-fill: #6B7280;");

        placeholderBox.getChildren().addAll(logoIcon, logoText);
        centerArea.getChildren().addAll(placeholderBox, gridBoard);

        VBox statusContainer = new VBox();
        statusContainer.setAlignment(Pos.CENTER);
        statusContainer.setMaxWidth(450);
        statusContainer.setStyle("-fx-background-color: #F9FAFB; -fx-background-radius: 12px; -fx-border-color: #D1D5DB; -fx-border-radius: 12px; -fx-padding: 20px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        labelStatus = new Label("Status: Menunggu instruksi");
        labelStatus.setTextAlignment(TextAlignment.CENTER);
        labelStatus.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-text-fill: #374151; -fx-font-weight: bold;");
        statusContainer.getChildren().add(labelStatus);

        btnFileChooser.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Buka File Konfigurasi Papan");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File folderTest = new File("test");
            if (folderTest.exists() && folderTest.isDirectory()) {
                fileChooser.setInitialDirectory(folderTest);
            }

            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                fileProcessing(file);
            }
        });

        btnSaveTxt.setOnAction(e -> saveToText());
        btnSaveImg.setOnAction(e -> saveToImg());

        root.getChildren().addAll(title, buttonBox, centerArea, statusContainer);

        Scene scene = new Scene(root, 750, 800);
        stage.setTitle("Queens LinkedIn Solver");
        stage.setScene(scene);
        stage.show();
    }

    private void styleButton(Button btn, String baseColor, String hoverColor) {
        String baseStyle = "-fx-background-color: " + baseColor + "; -fx-text-fill: white; -fx-font-family: " + FONT_FAMILY + "; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 8px;";
        String hoverStyle = "-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-font-family: " + FONT_FAMILY + "; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 8px;";

        btn.setStyle(baseStyle);
        btn.setCursor(Cursor.HAND);
        btn.setOnMouseEntered(e -> { if(!btn.isDisabled()) btn.setStyle(hoverStyle); });
        btn.setOnMouseExited(e -> { if(!btn.isDisabled()) btn.setStyle(baseStyle); });
    }

    private void fileProcessing(File file) {
        btnSaveTxt.setDisable(true);
        btnSaveImg.setDisable(true);

        try {
            Scanner readFile = new Scanner(file);
            ArrayList<String> rowFile = new ArrayList<>();
            while (readFile.hasNextLine()) {
                String line = readFile.nextLine().trim();
                if (!line.isEmpty()) {
                    rowFile.add(line);
                }
            }
            readFile.close();

            int n = rowFile.size();
            if (n == 0) throw new Exception("Error: File input kosong!");

            for (String row : rowFile) {
                if (row.length() != n) {
                    throw new Exception("Error: Papan tidak berbentuk persegi (N x N)!");
                }
            }

            char[][] map = new char[n][n];
            Set<Character> uniqueColors = new HashSet<>();

            for (int i = 0; i < n; i++) {
                map[i] = rowFile.get(i).toCharArray();
                for (int j = 0; j < n; j++) {
                    uniqueColors.add(map[i][j]);
                }
            }

            if (uniqueColors.size() != n) {
                throw new Exception("Error: Jumlah warna (" + uniqueColors.size() + ") tidak sama dengan N (" + n + ")!");
            }

            Board board = new Board(map, n);

            Solver solver = new Solver(currentBoard -> {
                Platform.runLater(() -> drawBoard(currentBoard));
            }, n);

            placeholderBox.setVisible(false);
            gridBoard.setVisible(true);
            labelStatus.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 16px; -fx-text-fill: #D97706; -fx-font-weight: bold;");
            labelStatus.setText("Mencari solusi menggunakan algoritma Brute Force");

            Thread backgroundThread = new Thread(() -> {
                double startTime = System.currentTimeMillis();
                boolean hasSolution = solver.solve(board, 0);
                double finishTime = System.currentTimeMillis();
                double executionTime = finishTime - startTime;

                Platform.runLater(() -> {
                    if (hasSolution) {

                        currSolvedBoard = board;
                        currExecTime = executionTime;
                        currIterations = solver.countCase;

                        btnSaveTxt.setDisable(false);
                        btnSaveImg.setDisable(false);

                        labelStatus.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 18px; -fx-text-fill: #059669; -fx-font-weight: bold;");
                        labelStatus.setText(String.format("SOLUSI DITEMUKAN!\nWaktu pencarian: %.2f ms\nBanyak kasus ditinjau: %d iterasi", executionTime, solver.countCase));
                        drawBoard(board);
                    } else {
                        labelStatus.setStyle("-fx-font-family: " + FONT_FAMILY + "; -fx-font-size: 18px; -fx-text-fill: #DC2626; -fx-font-weight: bold;");
                        labelStatus.setText(String.format("TIDAK ADA SOLUSI!\nWaktu pencarian: %.2f ms\nBanyak kasus ditinjau: %d iterasi", executionTime, solver.countCase));
                        gridBoard.getChildren().clear();
                        placeholderBox.setVisible(true);
                        gridBoard.setVisible(false);
                    }
                });
            });

            backgroundThread.setDaemon(true);
            backgroundThread.start();

        } catch (FileNotFoundException e) {
            labelStatus.setStyle("-fx-text-fill: #DC2626; -fx-font-family: " + FONT_FAMILY + "; -fx-font-weight: bold;");
            labelStatus.setText("Error: File tidak dapat dibaca!");
        } catch (Exception e) {
            labelStatus.setStyle("-fx-text-fill: #DC2626; -fx-font-family: " + FONT_FAMILY + "; -fx-font-weight: bold;");
            labelStatus.setText(e.getMessage());
        }
    }

    public void drawBoard(Board board) {
        gridBoard.getChildren().clear();

        int boxSize = 50;

        for (int row = 0; row < board.n; row++) {
            for (int col = 0; col < board.n; col++) {

                StackPane box = new StackPane();

                char charColor = board.boardMap[row][col];
                Color color = getColor(charColor);

                Rectangle rect = new Rectangle(boxSize, boxSize);
                rect.setFill(color);
                rect.setStroke(Color.web("#374151"));
                rect.setStrokeWidth(1.5);
                box.getChildren().add(rect);

                if (board.queenPos[row] == col) {
                    Label queensLabel = new Label("♛");
                    queensLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 30));
                    queensLabel.setTextFill(Color.web("#111827"));
                    box.getChildren().add(queensLabel);
                }

                gridBoard.add(box, col, row);
            }
        }
    }

    private Color getColor(char c) {
        double hue = (c * 37) % 360;
        return Color.hsb(hue, 0.35, 0.95);
    }

    private void saveToText() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Solusi Teks");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File folderTest = new File("test");
        if (folderTest.exists() && folderTest.isDirectory()) fileChooser.setInitialDirectory(folderTest);

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                for (int i = 0; i < currSolvedBoard.n; i++) {
                    for (int j = 0; j < currSolvedBoard.n; j++) {
                        if (currSolvedBoard.queenPos[i] == j) {
                            writer.print("#");
                        } else {
                            writer.print(currSolvedBoard.boardMap[i][j]);
                        }
                    }
                    writer.println();
                }

                writer.println();
                writer.printf("Waktu pencarian : %.2f ms\n", currExecTime);
                writer.println("Banyak kasus ditinjau: " + currIterations + " kasus");

                labelStatus.setText(labelStatus.getText() + "\n(Solusi teks berhasil disimpan!)");
            } catch (IOException ex) {
                labelStatus.setText("Gagal menyimpan file teks!");
            }
        }
    }

    private void saveToImg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Simpan Gambar Papan");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));

        File folderTest = new File("test");
        if (folderTest.exists() && folderTest.isDirectory()) fileChooser.setInitialDirectory(folderTest);

        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                WritableImage snapshot = gridBoard.snapshot(new SnapshotParameters(), null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
                labelStatus.setText(labelStatus.getText() + "\n(Gambar papan berhasil disimpan!)");
            } catch (IOException ex) {
                labelStatus.setText("Gagal menyimpan gambar!");
            }
        }
    }
}
