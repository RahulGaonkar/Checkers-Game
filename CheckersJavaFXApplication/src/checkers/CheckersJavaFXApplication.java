package checkers;

import java.util.Collection;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.effect.SepiaTone;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class CheckersJavaFXApplication extends Application {

    private static int alphaBetaTreeCutOffLevelSet;
    private static int numberOfComputerMoves;
    private static int checkersDifficultyLevelChoice = -1;
    private static int checkersPlayerOne = -1;
    private static int clickedSourceCellXCoordinate = -1;
    private static int clickedSourceCellYCoordinate = -1;
    private static Button sourceCheckersBoardCell;
    private static Button[][] allCurrentCheckersBoardGridPaneButtons = new Button[6][6];

    /**
     * @param args the command line arguments
     * @throws java.lang.CloneNotSupportedException
     */
    public static void main(String[] args) throws CloneNotSupportedException {
        launch(args);
    }

    /**
     *
     * @param primaryStage the JavaFX Stage for checkers board
     * @throws CloneNotSupportedException
     */
    @Override
    public void start(Stage primaryStage) throws CloneNotSupportedException {
        // Create the custom checkers menu dialog box.
        checkersStartMenu();
        if (checkersDifficultyLevelChoice == -1 && checkersPlayerOne == -1) {
            System.exit(0);
        }
        CheckersNutshell checkersInitialState = new CheckersNutshell(
                new int[][]{{0, 2, 0, 2, 0, 2}, {2, 0, 2, 0, 2, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 1}, {1, 0, 1, 0, 1, 0}});
        setCheckersBoardConfigurationStage(primaryStage, checkersInitialState);
        Platform.runLater(() -> {
            try {
                if (checkersPlayerOne == 2) {
                    CheckersNutshell checkersStateAfterComputerMove = computerPlayerMove(primaryStage, checkersInitialState);
                    checkersGameOverAfterComputerMove(primaryStage, checkersStateAfterComputerMove);
                }
            } catch (CloneNotSupportedException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

    /**
     *
     * @param primaryStage the JavaFX Stage for checkers board
     * @param checkersState checkers board configuration object
     */
    private void setCheckersBoardConfigurationStage(Stage primaryStage, CheckersNutshell checkersState) {
        primaryStage.setTitle("Checkers");
        //Adding GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 5, 5, 5));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        initializingCheckersBoardConfigurationGridPane(primaryStage, gridPane, checkersState);
        //Adding GridPane to the scene
        Scene scene = new Scene(gridPane, 635, 635);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     *
     * @param primaryStage the JavaFX Stage for checkers board
     * @param gridPane the JavaFX Grid to display the checkers board
     * @param checkersState checkers board configuration object
     */
    private void initializingCheckersBoardConfigurationGridPane(Stage primaryStage, GridPane gridPane, CheckersNutshell checkersState) {
        for (int checkersBoardRow = 0; checkersBoardRow < checkersState.getCheckersBoardConfiguration().length; checkersBoardRow++) {
            for (int checkersBoardColumn = 0; checkersBoardColumn < checkersState.getCheckersBoardConfiguration()[0].length; checkersBoardColumn++) {
                final int checkersBoardCellRow = checkersBoardRow;
                final int checkersBoardCellColumn = checkersBoardColumn;
                StackPane checkersBoardCell = new StackPane();
                Button square = new Button();
                square.setPrefSize(100, 100);
                square.setDisable(true);
                square.setOnAction(e -> {
                    try {
                        onClick(primaryStage, checkersBoardCellRow, checkersBoardCellColumn, square, checkersState);
                    } catch (CloneNotSupportedException ex) {
                        System.out.println(ex.getMessage());
                    }
                });
                if (((checkersBoardRow + checkersBoardColumn) % 2) == 0) {
                    square.setStyle("-fx-background-color: #ffffff");
                } else {
                    square.setStyle("-fx-background-color: #000000");
                }
                checkersBoardCell.getChildren().add(square);
                allCurrentCheckersBoardGridPaneButtons[checkersBoardCellRow][checkersBoardCellColumn] = square;
                placingCheckersPieceIfPresent(primaryStage, checkersBoardCell, checkersState, checkersBoardCellRow, checkersBoardCellColumn);
                GridPane.setRowIndex(checkersBoardCell, checkersBoardRow);
                GridPane.setColumnIndex(checkersBoardCell, checkersBoardColumn);
                gridPane.getChildren().add(checkersBoardCell);
            }
        }
    }

    /**
     *
     * @param primaryStage the JavaFX Stage for checkers board
     * @param checkersBoardCell the JavaFX StackPane for individual checkers
     * board square
     * @param checkersState checkers board configuration object
     * @param checkersBoardCellRow individual checkers board square row number
     * @param checkersBoardCellColumn individual checkers board square column
     * number
     */
    private void placingCheckersPieceIfPresent(Stage primaryStage, StackPane checkersBoardCell, CheckersNutshell checkersState, int checkersBoardCellRow, int checkersBoardCellColumn) {
        if (checkersState.getCheckersBoardConfiguration()[checkersBoardCellRow][checkersBoardCellColumn] == 1) {
            Button humanPiece = new Button();
            humanPiece.setOnAction(e -> {
                try {
                    onClick(primaryStage, checkersBoardCellRow, checkersBoardCellColumn, humanPiece, checkersState);
                } catch (CloneNotSupportedException ex) {
                    System.out.println(ex.getMessage());
                }
            });
            humanPiece.setStyle(
                    "-fx-background-radius: 100em; "
                    + "-fx-min-width: 90px; "
                    + "-fx-min-height: 90px; "
                    + "-fx-max-width: 90px; "
                    + "-fx-max-height: 90px;"
                    + "-fx-background-color: #8b0000;"
            );
            checkersBoardCell.getChildren().add(humanPiece);
        } else if (checkersState.getCheckersBoardConfiguration()[checkersBoardCellRow][checkersBoardCellColumn] == 2) {
            Button computerPiece = new Button();
            computerPiece.setStyle(
                    "-fx-background-radius: 100em; "
                    + "-fx-min-width: 90px; "
                    + "-fx-min-height: 90px; "
                    + "-fx-max-width: 90px; "
                    + "-fx-max-height: 90px;"
                    + "-fx-background-color: #000080;"
            );
            checkersBoardCell.getChildren().add(computerPiece);
        }
    }

    /**
     *
     * @param primaryStage the JavaFX Stage for checkers board
     * @param checkersBoardCellRow individual checkers board clicked square row
     * number
     * @param checkersBoardCellColumn individual checkers board clicked square
     * column number
     * @param checkersBoardCell the JavaFX button of the clicked checkers board
     * square
     * @param checkersState checkers board configuration object
     * @throws CloneNotSupportedException
     */
    private void onClick(Stage primaryStage, int checkersBoardCellRow, int checkersBoardCellColumn, Button checkersBoardCell, CheckersNutshell checkersState) throws CloneNotSupportedException {
        SepiaTone clickedCheckersBoardCell = new SepiaTone();
        clickedCheckersBoardCell.setLevel(0.6);
        if ((clickedSourceCellXCoordinate == checkersBoardCellRow) && (clickedSourceCellYCoordinate == checkersBoardCellColumn)) {
            unsetEffectToLegalHumanPieceMove(checkersState);
            clickedSourceCellXCoordinate = -1;
            clickedSourceCellYCoordinate = -1;
            sourceCheckersBoardCell.setEffect(null);
        } else if (clickedSourceCellXCoordinate == -1 && clickedSourceCellYCoordinate == -1) {
            sourceCheckersBoardCell = checkersBoardCell;
            clickedSourceCellXCoordinate = checkersBoardCellRow;
            clickedSourceCellYCoordinate = checkersBoardCellColumn;
            sourceCheckersBoardCell.setEffect(clickedCheckersBoardCell);
            setEffectToLegalHumanPieceMove(checkersState);
        } else {
            unsetEffectToLegalHumanPieceMove(checkersState);
            sourceCheckersBoardCell.setEffect(null);
            CheckersNutshell checkersStateAfterHumanMove = checkersState.nextCheckersHumanLegalMoveBoardConfiguration(
                    clickedSourceCellXCoordinate, clickedSourceCellYCoordinate, checkersBoardCellRow, checkersBoardCellColumn);
            clickedSourceCellXCoordinate = -1;
            clickedSourceCellYCoordinate = -1;
            setCheckersBoardConfigurationStage(primaryStage, checkersStateAfterHumanMove);
            Platform.runLater(() -> {
                try {
                    if (checkersStateAfterHumanMove != checkersState) {
                        checkersGameOverAfterHumanMove(primaryStage, checkersStateAfterHumanMove);
                    }
                } catch (CloneNotSupportedException ex) {
                    System.out.println(ex.getMessage());
                }
            });
        }
    }

    /**
     *
     * @param primaryStage the JavaFX Stage for checkers board
     * @param checkersState checkers board configuration object
     * @return checkers board configuration object
     * @throws CloneNotSupportedException
     */
    public CheckersNutshell computerPlayerMove(Stage primaryStage, CheckersNutshell checkersState)
            throws CloneNotSupportedException {
        numberOfComputerMoves++;
        switch (checkersDifficultyLevelChoice) {
            case 1:
                alphaBetaTreeCutOffLevelSet = 2;
                break;
            case 2:
                mediumDifficultyLevelalphaBetaTreeCutOff(numberOfComputerMoves);
                break;
            default:
                hardDifficultyLevelalphaBetaTreeCutOff(numberOfComputerMoves);
                break;
        }
        CheckersNutshell.setAlphaBetaTreeParameters(0, 0, 1, 0, 0, alphaBetaTreeCutOffLevelSet);
        CheckersNutshell checkersStateAfterComputerMove = checkersState
                .nextCheckersComputerLegalMoveBoardConfiguration();
        setCheckersBoardConfigurationStage(primaryStage, checkersStateAfterComputerMove);
        checkersAlphaBetaTreeStatisticsDialogBox();
        return checkersStateAfterComputerMove;
    }

    /**
     *
     * @param numberOfComputerMoves the number of computer moves done so far
     */
    private void hardDifficultyLevelalphaBetaTreeCutOff(int numberOfComputerMoves) {
        switch (numberOfComputerMoves) {
            case 1:
                alphaBetaTreeCutOffLevelSet = 25;
                break;
            case 2:
                alphaBetaTreeCutOffLevelSet = 27;
                break;
            default:
                alphaBetaTreeCutOffLevelSet = 0;
                break;
        }
    }

    /**
     *
     * @param numberOfComputerMoves the number of computer moves done so far
     */
    private void mediumDifficultyLevelalphaBetaTreeCutOff(int numberOfComputerMoves) {
        alphaBetaTreeCutOffLevelSet = 10 + (2 * (numberOfComputerMoves - 1));
    }

    /**
     *
     * @param primaryStage the JavaFX Stage for checkers board
     * @param checkersStateAfterComputerMove checkers board configuration object
     * @throws CloneNotSupportedException
     */
    public void checkersGameOverAfterComputerMove(Stage primaryStage, CheckersNutshell checkersStateAfterComputerMove)
            throws CloneNotSupportedException {
        int checkersStateAfterComputerMoveUtilityValue = checkersStateAfterComputerMove.isCheckersGameOver();
        switch (checkersStateAfterComputerMoveUtilityValue) {
            case 2:
                if (!checkersStateAfterComputerMove.isHumanPieceLegalMoveLeft()) {
                    checkersGameOverAfterHumanMove(primaryStage, checkersStateAfterComputerMove);
                }
                break;
            case 100:
                checkersDialogBoxOnGameOver("Computer Player Won The Game");
                break;
            case -100:
                checkersDialogBoxOnGameOver("Human Player Won The Game");
                break;
            default:
                checkersDialogBoxOnGameOver("The game has drawn");
                break;
        }
    }

    /**
     *
     * @param primaryStage the JavaFX Stage for checkers board
     * @param checkersStateAfterHumanMove checkers board configuration object
     * @throws CloneNotSupportedException
     */
    public void checkersGameOverAfterHumanMove(Stage primaryStage, CheckersNutshell checkersStateAfterHumanMove)
            throws CloneNotSupportedException {
        int checkersStateAfterHumanMoveUtilityValue = checkersStateAfterHumanMove.isCheckersGameOver();
        switch (checkersStateAfterHumanMoveUtilityValue) {
            case 2:
                CheckersNutshell checkersStateAfterComputerMove = checkersStateAfterHumanMove;
                if (checkersStateAfterHumanMove.isComputerPieceLegalMoveLeft()) {
                    checkersStateAfterComputerMove = computerPlayerMove(primaryStage, checkersStateAfterHumanMove);
                }
                checkersGameOverAfterComputerMove(primaryStage, checkersStateAfterComputerMove);
                break;
            case 100:
                checkersDialogBoxOnGameOver("Computer Player Won The Game");
                break;
            case -100:
                checkersDialogBoxOnGameOver("Human Player Won The Game");
                break;
            default:
                checkersDialogBoxOnGameOver("The game has drawn");
                break;
        }
    }

    /**
     *
     * @param checkersState checkers board configuration object
     */
    private void unsetEffectToLegalHumanPieceMove(CheckersNutshell checkersState) {
        Collection<Integer[]> allLegalHumanPieceMove = checkersState.allCurrentLegalHumanMove(checkersState, clickedSourceCellXCoordinate, clickedSourceCellYCoordinate);
        allLegalHumanPieceMove.forEach((legalHumanPieceMove) -> {
            allCurrentCheckersBoardGridPaneButtons[legalHumanPieceMove[0]][legalHumanPieceMove[1]].setStyle("-fx-background-color: #000000");
            allCurrentCheckersBoardGridPaneButtons[legalHumanPieceMove[0]][legalHumanPieceMove[1]].setDisable(true);
        });
    }

    /**
     *
     * @param checkersState checkers board configuration object
     */
    private void setEffectToLegalHumanPieceMove(CheckersNutshell checkersState) {
        Collection<Integer[]> allLegalHumanPieceMove = checkersState.allCurrentLegalHumanMove(checkersState, clickedSourceCellXCoordinate, clickedSourceCellYCoordinate);
        allLegalHumanPieceMove.forEach((legalHumanPieceMove) -> {
            allCurrentCheckersBoardGridPaneButtons[legalHumanPieceMove[0]][legalHumanPieceMove[1]].setStyle("-fx-background-color: #2e8b57");
            allCurrentCheckersBoardGridPaneButtons[legalHumanPieceMove[0]][legalHumanPieceMove[1]].setDisable(false);
        });
    }

    /**
     *
     * @param difficultyLevel the difficulty level of the game
     * @param player1 the player who will play first (Human or Computer)
     */
    private void initializingCheckersGameOptions(String difficultyLevel, String player1) {
        if (difficultyLevel.contains("Easy")) {
            checkersDifficultyLevelChoice = 1;
        } else if (difficultyLevel.contains("Medium")) {
            checkersDifficultyLevelChoice = 2;
        } else if (difficultyLevel.contains("Hard")) {
            checkersDifficultyLevelChoice = 3;
        }
        if (player1.contains("Human")) {
            checkersPlayerOne = 1;
        } else if (player1.contains("Computer")) {
            checkersPlayerOne = 2;
        }
    }

    /**
     *
     */
    private void checkersStartMenu() {
        Dialog<Pair<String, String>> checkersDialogBox = new Dialog<>();
        checkersDialogBox.setTitle("Checkers");
        checkersDialogBox.setResizable(true);
        checkersDialogBox.getDialogPane().setPrefSize(500, 500);
        checkersDialogBox.getDialogPane().setStyle("-fx-background-color: #a9a9a9");
        checkersDialogBox.setHeaderText("Welcome");
        ButtonType doneButton = new ButtonType("Play", ButtonBar.ButtonData.OK_DONE);
        checkersDialogBox.getDialogPane().getButtonTypes().addAll(doneButton, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        Label gameOptions = new Label("Checkers Game Options");
        gameOptions.setPrefSize(400.0, 100.0);
        gameOptions.setStyle("-fx-font-size: 2.5em ;");
        gridPane.add(gameOptions, 4, 0, 3, 1);
        Label difficultyLevel = new Label("Difficulty Level:");
        difficultyLevel.setPrefSize(150.0, 150.0);
        difficultyLevel.setStyle("-fx-font-size: 1.5em ;");
        gridPane.add(difficultyLevel, 4, 3);
        ChoiceBox checkersDifficultyLevel = new ChoiceBox(FXCollections.observableArrayList(
                "Easy", "Medium", "Difficult")
        );
        checkersDifficultyLevel.setValue("Easy");
        gridPane.add(checkersDifficultyLevel, 5, 3);
        Label player1 = new Label("Player One:");
        player1.setPrefSize(200.0, 50.0);
        player1.setStyle("-fx-font-size: 1.5em ;");
        gridPane.add(player1, 4, 7);
        ChoiceBox checkersPlayer1 = new ChoiceBox(FXCollections.observableArrayList(
                "Human", "Computer")
        );
        checkersPlayer1.setValue("Human");
        gridPane.add(checkersPlayer1, 5, 7);

        checkersDialogBox.getDialogPane().setContent(gridPane);

        checkersDialogBox.setResultConverter(dialogButton -> {
            if (dialogButton == doneButton) {
                return new Pair<>(checkersDifficultyLevel.valueProperty().toString(), checkersPlayer1.valueProperty().toString());
            }
            return null;
        });
        Optional<Pair<String, String>> checkersDialogBoxResult = checkersDialogBox.showAndWait();
        checkersDialogBoxResult.ifPresent(pair -> {
            initializingCheckersGameOptions(pair.getKey(), pair.getValue());
        });
    }

    /**
     *
     */
    private void checkersAlphaBetaTreeStatisticsDialogBox() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Checkers");
        alert.setHeaderText("CHECKERS COMPUTER MOVE ALPHABETA TREE STATISTICS");
        alert.getDialogPane().setStyle("-fx-background-color: #a9a9a9");
        alert.getDialogPane().setPrefSize(600, 300);
        alert.setContentText(CheckersNutshell.printAlphaBetaTreeStatistics());
        alert.showAndWait();
    }

    /**
     *
     * @param alertBoxContent the result after the game is over
     */
    private void checkersDialogBoxOnGameOver(String alertBoxContent) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Checkers");
        alert.setHeaderText("GAME OVER!!!!");
        alert.getDialogPane().setStyle("-fx-background-color: #a9a9a9");
        alert.getDialogPane().setPrefSize(300, 150);
        alert.setContentText(alertBoxContent);
        alert.showAndWait();
    }

}
