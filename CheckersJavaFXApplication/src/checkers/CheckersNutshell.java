package checkers;

import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Rahul
 */
public class CheckersNutshell implements Cloneable {

    private static int currentDepthOfAlphaBetaTreeBranch;
    private static int maximumDepthOfAlphaBetaTree;
    private static int totalNumberOfNodesGenerated;
    private static int numberOfTimePruningOccurredInMaxValueFunction;
    private static int numberOfTimePruningOccurredInMinValueFunction;
    private static int alphaBetaTreeCutOffLevelSet;

    /**
     *
     * @param currentDepthOfAlphaBetaTreeBranch the current depth of alpha beta
     * tree branch
     * @param maximumDepthOfAlphaBetaTree the maximum depth of alpha beta tree
     * @param totalNumberOfNodesGenerated the total number of nodes generated
     * @param numberOfTimePruningOccurredInMaxValueFunction the number of time
     * pruning occurred in max value function
     * @param numberOfTimePruningOccurredInMinValueFunction the number of time
     * pruning occurred in min value function
     * @param alphaBetaTreeCutOffLevelSet the alpha beta tree cut off level set
     */
    public static void setAlphaBetaTreeParameters(int currentDepthOfAlphaBetaTreeBranch,
            int maximumDepthOfAlphaBetaTree, int totalNumberOfNodesGenerated,
            int numberOfTimePruningOccurredInMaxValueFunction, int numberOfTimePruningOccurredInMinValueFunction,
            int alphaBetaTreeCutOffLevelSet) {
        CheckersNutshell.currentDepthOfAlphaBetaTreeBranch = currentDepthOfAlphaBetaTreeBranch;
        CheckersNutshell.maximumDepthOfAlphaBetaTree = maximumDepthOfAlphaBetaTree;
        CheckersNutshell.totalNumberOfNodesGenerated = totalNumberOfNodesGenerated;
        CheckersNutshell.numberOfTimePruningOccurredInMaxValueFunction = numberOfTimePruningOccurredInMaxValueFunction;
        CheckersNutshell.numberOfTimePruningOccurredInMinValueFunction = numberOfTimePruningOccurredInMinValueFunction;
        CheckersNutshell.alphaBetaTreeCutOffLevelSet = alphaBetaTreeCutOffLevelSet;
    }

    private int[][] checkersBoardConfiguration;

    /**
     *
     * @param checkersBoardConfiguration the checkers board configuration
     */
    public CheckersNutshell(int[][] checkersBoardConfiguration) {
        this.checkersBoardConfiguration = checkersBoardConfiguration;
    }

    /**
     * @return the checkers board configuration
     */
    public int[][] getCheckersBoardConfiguration() {
        return this.checkersBoardConfiguration;
    }

    /**
     *
     * @return the checkers board configuration object
     * @throws CloneNotSupportedException
     */
    @Override
    public CheckersNutshell clone() throws CloneNotSupportedException {
        CheckersNutshell checkersNutshellClone = (CheckersNutshell) super.clone();
        checkersNutshellClone.checkersBoardConfiguration = this.checkersBoardConfiguration.clone();
        for (int checkersBoardConfigurationRow = 0; checkersBoardConfigurationRow < this.checkersBoardConfiguration.length; checkersBoardConfigurationRow++) {
            checkersNutshellClone.checkersBoardConfiguration[checkersBoardConfigurationRow] = this.checkersBoardConfiguration[checkersBoardConfigurationRow]
                    .clone();
        }
        return checkersNutshellClone;
    }

    /**
     *
     * @param pieceOldXPosition human piece source position x co-ordinate
     * @param pieceOldYPosition human piece source position y co-ordinate
     * @param pieceNewXPosition human piece destination position x co-ordinate
     * @param pieceNewYPosition human piece destination position y co-ordinate
     * @return the checkers board configuration object
     */
    public CheckersNutshell nextCheckersHumanLegalMoveBoardConfiguration(int pieceOldXPosition, int pieceOldYPosition,
            int pieceNewXPosition, int pieceNewYPosition) {
        if (this.humanLegalMove(pieceOldXPosition, pieceOldYPosition, pieceNewXPosition, pieceNewYPosition)) {
            return this.executingCheckersHumanLegalMove(pieceOldXPosition, pieceOldYPosition, pieceNewXPosition,
                    pieceNewYPosition);
        } else {
            System.out.println("Not a Legal Move.Please try again.");
            return this;
        }
    }

    /**
     *
     * @param pieceOldXPosition human piece source position x co-ordinate
     * @param pieceOldYPosition human piece source position x co-ordinate
     * @param pieceNewXPosition human piece destination position x co-ordinate
     * @param pieceNewYPosition human piece destination position y co-ordinate
     * @return the boolean value depending on if the human move is legal
     */
    public boolean humanLegalMove(int pieceOldXPosition, int pieceOldYPosition, int pieceNewXPosition,
            int pieceNewYPosition) {
        if (pieceOldXPosition < 0 || pieceOldXPosition > 5 || pieceOldYPosition < 0 || pieceOldYPosition > 5
                || pieceNewXPosition < 0 || pieceNewXPosition > 5 || pieceNewYPosition < 0 || pieceNewYPosition > 5) {
            return false;
        }
        if (this.checkersBoardConfiguration[pieceOldXPosition][pieceOldYPosition] != 1) {
            return false;
        }
        switch (pieceOldXPosition - pieceNewXPosition) {
            case 1:
                if (!humanCaptureMoveCanBeMade()) {
                    if (((pieceOldYPosition - pieceNewYPosition) == 1 || (pieceOldYPosition - pieceNewYPosition) == -1)
                            && this.checkersBoardConfiguration[pieceNewXPosition][pieceNewYPosition] == 0) {
                        return true;
                    }
                }
                return false;
            case 2:
                if (((pieceOldYPosition - pieceNewYPosition) == 2 || (pieceOldYPosition - pieceNewYPosition) == -2)
                        && this.checkersBoardConfiguration[pieceNewXPosition][pieceNewYPosition] == 0) {
                    if ((pieceOldYPosition - pieceNewYPosition) == 2
                            && this.checkersBoardConfiguration[pieceOldXPosition - 1][pieceOldYPosition - 1] == 2) {
                        return true;
                    }
                    if ((pieceOldYPosition - pieceNewYPosition) == -2
                            && this.checkersBoardConfiguration[pieceOldXPosition - 1][pieceOldYPosition + 1] == 2) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    /**
     *
     * @return the boolean value depending on if a human capture move is
     * possible
     */
    private boolean humanCaptureMoveCanBeMade() {
        for (int checkersBoardConfigurationRow = this.checkersBoardConfiguration.length
                - 1; checkersBoardConfigurationRow >= 0; checkersBoardConfigurationRow--) {
            for (int checkersBoardConfigurationColumn = this.checkersBoardConfiguration[0].length
                    - 1; checkersBoardConfigurationColumn >= 0; checkersBoardConfigurationColumn--) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 1) {
                    if (this.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 2, checkersBoardConfigurationColumn - 2)) {
                        return true;
                    }
                    if (this.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 2, checkersBoardConfigurationColumn + 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @param pieceOldXPosition human piece source position x co-ordinate
     * @param pieceOldYPosition human piece source position y co-ordinate
     * @param pieceNewXPosition human piece destination position x co-ordinate
     * @param pieceNewYPosition human piece destination position y co-ordinate
     * @return the checkers board configuration object
     */
    public CheckersNutshell executingCheckersHumanLegalMove(int pieceOldXPosition, int pieceOldYPosition,
            int pieceNewXPosition, int pieceNewYPosition) {
        int[][] newCheckersBoardConfiguration = this.checkersBoardConfiguration;
        newCheckersBoardConfiguration[pieceNewXPosition][pieceNewYPosition] = newCheckersBoardConfiguration[pieceOldXPosition][pieceOldYPosition];
        newCheckersBoardConfiguration[pieceOldXPosition][pieceOldYPosition] = 0;
        if ((pieceNewXPosition - pieceOldXPosition) == -2) {
            if ((pieceNewYPosition - pieceOldYPosition) == 2) {
                newCheckersBoardConfiguration[pieceOldXPosition - 1][pieceOldYPosition + 1] = 0;
            } else {
                newCheckersBoardConfiguration[pieceOldXPosition - 1][pieceOldYPosition - 1] = 0;
            }
        }
        return new CheckersNutshell(newCheckersBoardConfiguration);
    }

    /**
     *
     * @return the checkers board configuration object
     * @throws CloneNotSupportedException
     */
    public CheckersNutshell nextCheckersComputerLegalMoveBoardConfiguration() throws CloneNotSupportedException {
        int[] nextLegalComputerMoveAction = alphaBetaSearch(this.clone());
        if (!(nextLegalComputerMoveAction[0] == 0 && nextLegalComputerMoveAction[1] == 0
                && nextLegalComputerMoveAction[2] == 0 && nextLegalComputerMoveAction[3] == 0)) {
            return this.executingCheckersComputerLegalMove(nextLegalComputerMoveAction[0],
                    nextLegalComputerMoveAction[1], nextLegalComputerMoveAction[2], nextLegalComputerMoveAction[3]);
        } else {
            System.out.println("No Legal Computer Move Left");
            return this;
        }
    }

    /**
     *
     * @param pieceOldXPosition computer piece source position x co-ordinate
     * @param pieceOldYPosition computer piece source position y co-ordinate
     * @param pieceNewXPosition computer piece destination position x
     * co-ordinate
     * @param pieceNewYPosition computer piece destination position y
     * co-ordinate
     * @return the boolean value depending on if the computer move is legal
     */
    public boolean computerLegalMove(int pieceOldXPosition, int pieceOldYPosition, int pieceNewXPosition,
            int pieceNewYPosition) {
        if (pieceOldXPosition < 0 || pieceOldXPosition > 5 || pieceOldYPosition < 0 || pieceOldYPosition > 5
                || pieceNewXPosition < 0 || pieceNewXPosition > 5 || pieceNewYPosition < 0 || pieceNewYPosition > 5) {
            return false;
        }
        if (this.checkersBoardConfiguration[pieceOldXPosition][pieceOldYPosition] != 2) {
            return false;
        }
        switch (pieceOldXPosition - pieceNewXPosition) {
            case -1:
                if (!computerCaptureMoveCanBeMade()) {
                    if (((pieceOldYPosition - pieceNewYPosition) == 1 || (pieceOldYPosition - pieceNewYPosition) == -1)
                            && this.checkersBoardConfiguration[pieceNewXPosition][pieceNewYPosition] == 0) {
                        return true;
                    }
                }
                return false;
            case -2:
                if (((pieceOldYPosition - pieceNewYPosition) == 2 || (pieceOldYPosition - pieceNewYPosition) == -2)
                        && this.checkersBoardConfiguration[pieceNewXPosition][pieceNewYPosition] == 0) {
                    if ((pieceOldYPosition - pieceNewYPosition) == 2
                            && this.checkersBoardConfiguration[pieceOldXPosition + 1][pieceOldYPosition - 1] == 1) {
                        return true;
                    }
                    if ((pieceOldYPosition - pieceNewYPosition) == -2
                            && this.checkersBoardConfiguration[pieceOldXPosition + 1][pieceOldYPosition + 1] == 1) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    /**
     *
     * @return the boolean value depending on if a computer capture move is
     * possible
     */
    private boolean computerCaptureMoveCanBeMade() {
        for (int checkersBoardConfigurationRow = 0; checkersBoardConfigurationRow < this.checkersBoardConfiguration.length; checkersBoardConfigurationRow++) {
            for (int checkersBoardConfigurationColumn = 0; checkersBoardConfigurationColumn < this.checkersBoardConfiguration[0].length; checkersBoardConfigurationColumn++) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 2) {
                    if (this.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn - 2)) {
                        return true;
                    }
                    if (this.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn + 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @param pieceOldXPosition computer piece source position x co-ordinate
     * @param pieceOldYPosition computer piece source position y co-ordinate
     * @param pieceNewXPosition computer piece destination position x
     * co-ordinate
     * @param pieceNewYPosition computer piece destination position y
     * co-ordinate
     * @return the checkers board configuration object
     */
    public CheckersNutshell executingCheckersComputerLegalMove(int pieceOldXPosition, int pieceOldYPosition,
            int pieceNewXPosition, int pieceNewYPosition) {
        int[][] newCheckersBoardConfiguration = this.checkersBoardConfiguration;
        newCheckersBoardConfiguration[pieceNewXPosition][pieceNewYPosition] = newCheckersBoardConfiguration[pieceOldXPosition][pieceOldYPosition];
        newCheckersBoardConfiguration[pieceOldXPosition][pieceOldYPosition] = 0;
        if ((pieceNewXPosition - pieceOldXPosition) == 2) {
            if ((pieceNewYPosition - pieceOldYPosition) == 2) {
                newCheckersBoardConfiguration[pieceOldXPosition + 1][pieceOldYPosition + 1] = 0;
            } else {
                newCheckersBoardConfiguration[pieceOldXPosition + 1][pieceOldYPosition - 1] = 0;
            }
        }
        return new CheckersNutshell(newCheckersBoardConfiguration);
    }

    /**
     *
     * @return the utility value of the checkers board configuration
     */
    public int isCheckersGameOver() {
        if (!isAHumanPieceLeft()) {
            return 100;
        } else if (!isAComputerPieceLeft()) {
            return -100;
        } else if (!isHumanPieceLegalMoveLeft()) {
            if (!isComputerPieceLegalMoveLeft()) {
                return checkersTerminalBoardConfigurationUtilityValue();
            }
        }
        return 2;
    }

    /**
     *
     * @return the boolean value depending on the human pieces left on the
     * checkers board
     */
    private boolean isAHumanPieceLeft() {
        for (int checkersBoardConfigurationRow = this.checkersBoardConfiguration.length
                - 1; checkersBoardConfigurationRow >= 0; checkersBoardConfigurationRow--) {
            for (int checkersBoardConfigurationColumn = this.checkersBoardConfiguration[0].length
                    - 1; checkersBoardConfigurationColumn >= 0; checkersBoardConfigurationColumn--) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return the boolean value depending on the computer pieces left on the
     * checkers board
     */
    private boolean isAComputerPieceLeft() {
        for (int[] checkersBoardConfigurationrow : this.checkersBoardConfiguration) {
            for (int checkersBoardConfigurationColumn = 0; checkersBoardConfigurationColumn < checkersBoardConfigurationrow.length; checkersBoardConfigurationColumn++) {
                if (checkersBoardConfigurationrow[checkersBoardConfigurationColumn] == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return the boolean value depending on if the human piece legal move is
     * left on the checkers board
     */
    public boolean isHumanPieceLegalMoveLeft() {
        for (int checkersBoardConfigurationRow = this.checkersBoardConfiguration.length
                - 1; checkersBoardConfigurationRow >= 0; checkersBoardConfigurationRow--) {
            for (int checkersBoardConfigurationColumn = this.checkersBoardConfiguration[0].length
                    - 1; checkersBoardConfigurationColumn >= 0; checkersBoardConfigurationColumn--) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 1) {
                    if (this.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 1, checkersBoardConfigurationColumn + 1)) {
                        return true;
                    } else if (this.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 1, checkersBoardConfigurationColumn - 1)) {
                        return true;
                    } else if (this.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 2, checkersBoardConfigurationColumn - 2)) {
                        return true;
                    } else if (this.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 2, checkersBoardConfigurationColumn + 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @return the boolean value depending on if the computer piece legal move
     * is left on the checkers board
     */
    public boolean isComputerPieceLegalMoveLeft() {
        for (int checkersBoardConfigurationRow = 0; checkersBoardConfigurationRow < this.checkersBoardConfiguration.length; checkersBoardConfigurationRow++) {
            for (int checkersBoardConfigurationColumn = 0; checkersBoardConfigurationColumn < this.checkersBoardConfiguration[0].length; checkersBoardConfigurationColumn++) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 2) {
                    if (this.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 1, checkersBoardConfigurationColumn + 1)) {
                        return true;
                    } else if (this.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 1, checkersBoardConfigurationColumn - 1)) {
                        return true;
                    } else if (this.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn - 2)) {
                        return true;
                    } else if (this.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn + 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @return the utility value of the terminal checkers board configuration
     */
    private int checkersTerminalBoardConfigurationUtilityValue() {
        int humanPieceCount = 0;
        int computerPieceCount = 0;
        for (int[] checkersBoardConfigurationrow : this.checkersBoardConfiguration) {
            for (int checkersBoardConfigurationColumn = 0; checkersBoardConfigurationColumn < checkersBoardConfigurationrow.length; checkersBoardConfigurationColumn++) {
                if (checkersBoardConfigurationrow[checkersBoardConfigurationColumn] == 1) {
                    humanPieceCount++;
                } else if (checkersBoardConfigurationrow[checkersBoardConfigurationColumn] == 2) {
                    computerPieceCount++;
                }
            }
        }
        if (humanPieceCount > computerPieceCount) {
            return -100;
        } else if (computerPieceCount > humanPieceCount) {
            return 100;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param checkersBoard the checkers board configuration object
     * @param humanPieceXPosition human piece source position x co-ordinate
     * @param humanPieceYPosition human piece source position y co-ordinate
     * @return a collection of all current legal destinations of human piece
     */
    public Collection<Integer[]> allCurrentLegalHumanMove(CheckersNutshell checkersBoard, int humanPieceXPosition, int humanPieceYPosition) {
        Collection<Integer[]> allCurrentLegalHumanMoveSet = new HashSet<>();
        if (checkersBoard.humanLegalMove(humanPieceXPosition, humanPieceYPosition,
                humanPieceXPosition - 1, humanPieceYPosition + 1)) {
            allCurrentLegalHumanMoveSet.add(
                    new Integer[]{humanPieceXPosition - 1, humanPieceYPosition + 1});
        }
        if (checkersBoard.humanLegalMove(humanPieceXPosition, humanPieceYPosition,
                humanPieceXPosition - 1, humanPieceYPosition - 1)) {
            allCurrentLegalHumanMoveSet.add(
                    new Integer[]{humanPieceXPosition - 1, humanPieceYPosition - 1});
        }
        if (checkersBoard.humanLegalMove(humanPieceXPosition, humanPieceYPosition,
                humanPieceXPosition - 2, humanPieceYPosition + 2)) {
            allCurrentLegalHumanMoveSet.add(
                    new Integer[]{humanPieceXPosition - 2, humanPieceYPosition + 2});
        }
        if (checkersBoard.humanLegalMove(humanPieceXPosition, humanPieceYPosition,
                humanPieceXPosition - 2, humanPieceYPosition - 2)) {
            allCurrentLegalHumanMoveSet.add(
                    new Integer[]{humanPieceXPosition - 2, humanPieceYPosition - 2});
        }
        return allCurrentLegalHumanMoveSet;
    }

    /**
     *
     * @param checkersBoard the checkers board configuration object
     * @return a 2-D array of all current legal moves of all computer pieces
     * with default utility value
     */
    private int[][] allCurrentLegalComputerMove(CheckersNutshell checkersBoard) {
        int[][] allCurrentLegalComputerMoves = new int[10][5];
        int currentLegalComputerMoveIncrementor = 0;
        for (int checkersBoardConfigurationRow = 0; checkersBoardConfigurationRow < checkersBoard.checkersBoardConfiguration.length; checkersBoardConfigurationRow++) {
            for (int checkersBoardConfigurationColumn = 0; checkersBoardConfigurationColumn < checkersBoard.checkersBoardConfiguration[0].length; checkersBoardConfigurationColumn++) {
                if (checkersBoard.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 2) {
                    if (checkersBoard.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 1, checkersBoardConfigurationColumn + 1)) {
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][0] = checkersBoardConfigurationRow;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][1] = checkersBoardConfigurationColumn;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][2] = checkersBoardConfigurationRow + 1;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][3] = checkersBoardConfigurationColumn + 1;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][4] = Integer.MIN_VALUE;
                        currentLegalComputerMoveIncrementor++;
                    }
                    if (checkersBoard.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 1, checkersBoardConfigurationColumn - 1)) {
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][0] = checkersBoardConfigurationRow;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][1] = checkersBoardConfigurationColumn;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][2] = checkersBoardConfigurationRow + 1;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][3] = checkersBoardConfigurationColumn - 1;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][4] = Integer.MIN_VALUE;
                        currentLegalComputerMoveIncrementor++;
                    }
                    if (checkersBoard.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn - 2)) {
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][0] = checkersBoardConfigurationRow;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][1] = checkersBoardConfigurationColumn;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][2] = checkersBoardConfigurationRow + 2;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][3] = checkersBoardConfigurationColumn - 2;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][4] = Integer.MIN_VALUE;
                        currentLegalComputerMoveIncrementor++;
                    }
                    if (checkersBoard.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn + 2)) {
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][0] = checkersBoardConfigurationRow;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][1] = checkersBoardConfigurationColumn;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][2] = checkersBoardConfigurationRow + 2;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][3] = checkersBoardConfigurationColumn + 2;
                        allCurrentLegalComputerMoves[currentLegalComputerMoveIncrementor][4] = Integer.MIN_VALUE;
                        currentLegalComputerMoveIncrementor++;
                    }
                }
            }
        }
        return allCurrentLegalComputerMoves;
    }

    /**
     *
     * @param checkersBoard the checkers board configuration object
     * @return the best computer move found using alpha beta search
     * @throws CloneNotSupportedException
     */
    private int[] alphaBetaSearch(CheckersNutshell checkersBoard) throws CloneNotSupportedException {
        int[] bestMove = new int[]{0, 0, 0, 0, 0};
        int bestUtilityValue = Integer.MIN_VALUE;
        int[][] allCurrentLegalComputerMoves = allCurrentLegalComputerMove(checkersBoard);
        for (int[] currentLegalComputerMove : allCurrentLegalComputerMoves) {
            if (currentLegalComputerMove[4] == Integer.MIN_VALUE) {
                currentDepthOfAlphaBetaTreeBranch++;
                totalNumberOfNodesGenerated++;
                int currentLegalComputerMoveUtilityValue = minValue(checkersBoard.clone().executingCheckersComputerLegalMove(currentLegalComputerMove[0], currentLegalComputerMove[1], currentLegalComputerMove[2], currentLegalComputerMove[3]), -100, 100);
                if (currentLegalComputerMoveUtilityValue == 100) {
                    numberOfTimePruningOccurredInMaxValueFunction++;
                    bestMove = new int[]{currentLegalComputerMove[0], currentLegalComputerMove[1], currentLegalComputerMove[2], currentLegalComputerMove[3]};
                    return bestMove;
                }
                currentLegalComputerMove[4] = currentLegalComputerMoveUtilityValue;
            } else {
                break;
            }
        }

        for (int[] currentLegalComputerMove : allCurrentLegalComputerMoves) {
            if (!(currentLegalComputerMove[0] == 0 && currentLegalComputerMove[1] == 0 && currentLegalComputerMove[2] == 0 && currentLegalComputerMove[3] == 0)) {
                if (currentLegalComputerMove[4] >= bestUtilityValue) {
                    bestUtilityValue = currentLegalComputerMove[4];
                    bestMove = new int[]{currentLegalComputerMove[0], currentLegalComputerMove[1], currentLegalComputerMove[2], currentLegalComputerMove[3]};
                }
            } else {
                break;
            }
        }
        return bestMove;
    }

    /**
     *
     * @param checkersBoard the checkers board configuration object
     * @param checkersAlphaBetaSearchAlphaValue the checkers alpha beta search
     * alpha value
     * @param checkersAlphaBetaSearchBetaValue the checkers alpha beta search
     * beta value
     * @return the max value function utility value checkers board configuration
     * @throws CloneNotSupportedException
     */
    private int maxValue(CheckersNutshell checkersBoard, int checkersAlphaBetaSearchAlphaValue,
            int checkersAlphaBetaSearchBetaValue) throws CloneNotSupportedException {
        int currentCheckersBoardConfigurationUtilityValue = checkersBoard.isCheckersGameOver();
        if (currentCheckersBoardConfigurationUtilityValue != 2) {
            if (maximumDepthOfAlphaBetaTree < currentDepthOfAlphaBetaTreeBranch) {
                maximumDepthOfAlphaBetaTree = currentDepthOfAlphaBetaTreeBranch;
            }
            currentDepthOfAlphaBetaTreeBranch--;
            return currentCheckersBoardConfigurationUtilityValue;
        }
        if (currentDepthOfAlphaBetaTreeBranch == alphaBetaTreeCutOffLevelSet) {
            maximumDepthOfAlphaBetaTree = alphaBetaTreeCutOffLevelSet;
            currentDepthOfAlphaBetaTreeBranch--;
            return alphaBetaTreeEvaluationFunction();
        }
        int checkersAlphaBetaSearchUtilityValue = Integer.MIN_VALUE;
        for (int checkersBoardConfigurationRow = 0; checkersBoardConfigurationRow < checkersBoard.checkersBoardConfiguration.length; checkersBoardConfigurationRow++) {
            for (int checkersBoardConfigurationColumn = 0; checkersBoardConfigurationColumn < checkersBoard.checkersBoardConfiguration[0].length; checkersBoardConfigurationColumn++) {
                if (checkersBoard.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 2) {
                    if (checkersBoard.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 1, checkersBoardConfigurationColumn + 1)) {
                        currentDepthOfAlphaBetaTreeBranch++;
                        totalNumberOfNodesGenerated++;
                        checkersAlphaBetaSearchUtilityValue = Math.max(checkersAlphaBetaSearchUtilityValue,
                                minValue(checkersBoard.clone().executingCheckersComputerLegalMove(
                                        checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                                        checkersBoardConfigurationRow + 1, checkersBoardConfigurationColumn + 1),
                                        checkersAlphaBetaSearchAlphaValue, checkersAlphaBetaSearchBetaValue));
                        if (checkersAlphaBetaSearchUtilityValue >= checkersAlphaBetaSearchBetaValue) {
                            numberOfTimePruningOccurredInMaxValueFunction++;
                            currentDepthOfAlphaBetaTreeBranch--;
                            return checkersAlphaBetaSearchUtilityValue;
                        }
                        checkersAlphaBetaSearchAlphaValue = Math.max(checkersAlphaBetaSearchAlphaValue,
                                checkersAlphaBetaSearchUtilityValue);
                    }
                    if (checkersBoard.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 1, checkersBoardConfigurationColumn - 1)) {
                        currentDepthOfAlphaBetaTreeBranch++;
                        totalNumberOfNodesGenerated++;
                        checkersAlphaBetaSearchUtilityValue = Math.max(checkersAlphaBetaSearchUtilityValue,
                                minValue(checkersBoard.clone().executingCheckersComputerLegalMove(
                                        checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                                        checkersBoardConfigurationRow + 1, checkersBoardConfigurationColumn - 1),
                                        checkersAlphaBetaSearchAlphaValue, checkersAlphaBetaSearchBetaValue));
                        if (checkersAlphaBetaSearchUtilityValue >= checkersAlphaBetaSearchBetaValue) {
                            numberOfTimePruningOccurredInMaxValueFunction++;
                            currentDepthOfAlphaBetaTreeBranch--;
                            return checkersAlphaBetaSearchUtilityValue;
                        }
                        checkersAlphaBetaSearchAlphaValue = Math.max(checkersAlphaBetaSearchAlphaValue,
                                checkersAlphaBetaSearchUtilityValue);
                    }
                    if (checkersBoard.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn + 2)) {
                        currentDepthOfAlphaBetaTreeBranch++;
                        totalNumberOfNodesGenerated++;
                        checkersAlphaBetaSearchUtilityValue = Math.max(checkersAlphaBetaSearchUtilityValue,
                                minValue(checkersBoard.clone().executingCheckersComputerLegalMove(
                                        checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                                        checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn + 2),
                                        checkersAlphaBetaSearchAlphaValue, checkersAlphaBetaSearchBetaValue));
                        if (checkersAlphaBetaSearchUtilityValue >= checkersAlphaBetaSearchBetaValue) {
                            numberOfTimePruningOccurredInMaxValueFunction++;
                            currentDepthOfAlphaBetaTreeBranch--;
                            return checkersAlphaBetaSearchUtilityValue;
                        }
                        checkersAlphaBetaSearchAlphaValue = Math.max(checkersAlphaBetaSearchAlphaValue,
                                checkersAlphaBetaSearchUtilityValue);
                    }
                    if (checkersBoard.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn - 2)) {
                        currentDepthOfAlphaBetaTreeBranch++;
                        totalNumberOfNodesGenerated++;
                        checkersAlphaBetaSearchUtilityValue = Math.max(checkersAlphaBetaSearchUtilityValue,
                                minValue(checkersBoard.clone().executingCheckersComputerLegalMove(
                                        checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                                        checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn - 2),
                                        checkersAlphaBetaSearchAlphaValue, checkersAlphaBetaSearchBetaValue));
                        if (checkersAlphaBetaSearchUtilityValue >= checkersAlphaBetaSearchBetaValue) {
                            numberOfTimePruningOccurredInMaxValueFunction++;
                            currentDepthOfAlphaBetaTreeBranch--;
                            return checkersAlphaBetaSearchUtilityValue;
                        }
                        checkersAlphaBetaSearchAlphaValue = Math.max(checkersAlphaBetaSearchAlphaValue,
                                checkersAlphaBetaSearchUtilityValue);
                    }
                }
            }
        }
        currentDepthOfAlphaBetaTreeBranch--;
        return checkersAlphaBetaSearchUtilityValue;
    }

    /**
     *
     * @param checkersBoard the checkers board configuration object
     * @param checkersAlphaBetaSearchAlphaValue the checkers alpha beta search
     * alpha value
     * @param checkersAlphaBetaSearchBetaValue the checkers alpha beta search
     * beta value
     * @return the min value function utility value checkers board configuration
     * @throws CloneNotSupportedException
     */
    private int minValue(CheckersNutshell checkersBoard, int checkersAlphaBetaSearchAlphaValue,
            int checkersAlphaBetaSearchBetaValue) throws CloneNotSupportedException {
        int currentCheckersBoardConfigurationUtilityValue = checkersBoard.isCheckersGameOver();
        if (currentCheckersBoardConfigurationUtilityValue != 2) {
            if (maximumDepthOfAlphaBetaTree < currentDepthOfAlphaBetaTreeBranch) {
                maximumDepthOfAlphaBetaTree = currentDepthOfAlphaBetaTreeBranch;
            }
            currentDepthOfAlphaBetaTreeBranch--;
            return currentCheckersBoardConfigurationUtilityValue;
        }
        if (currentDepthOfAlphaBetaTreeBranch == alphaBetaTreeCutOffLevelSet) {
            maximumDepthOfAlphaBetaTree = alphaBetaTreeCutOffLevelSet;
            currentDepthOfAlphaBetaTreeBranch--;
            return alphaBetaTreeEvaluationFunction();
        }
        int checkersAlphaBetaSearchUtilityValue = Integer.MAX_VALUE;
        for (int checkersBoardConfigurationRow = checkersBoard.checkersBoardConfiguration.length
                - 1; checkersBoardConfigurationRow >= 0; checkersBoardConfigurationRow--) {
            for (int checkersBoardConfigurationColumn = checkersBoard.checkersBoardConfiguration[0].length
                    - 1; checkersBoardConfigurationColumn >= 0; checkersBoardConfigurationColumn--) {
                if (checkersBoard.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 1) {
                    if (checkersBoard.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 1, checkersBoardConfigurationColumn + 1)) {
                        currentDepthOfAlphaBetaTreeBranch++;
                        totalNumberOfNodesGenerated++;
                        checkersAlphaBetaSearchUtilityValue = Math.min(checkersAlphaBetaSearchUtilityValue,
                                maxValue(checkersBoard.clone().executingCheckersHumanLegalMove(
                                        checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                                        checkersBoardConfigurationRow - 1, checkersBoardConfigurationColumn + 1),
                                        checkersAlphaBetaSearchAlphaValue, checkersAlphaBetaSearchBetaValue));
                        if (checkersAlphaBetaSearchUtilityValue <= checkersAlphaBetaSearchAlphaValue) {
                            numberOfTimePruningOccurredInMinValueFunction++;
                            currentDepthOfAlphaBetaTreeBranch--;
                            return checkersAlphaBetaSearchUtilityValue;
                        }
                        checkersAlphaBetaSearchBetaValue = Math.min(checkersAlphaBetaSearchBetaValue,
                                checkersAlphaBetaSearchUtilityValue);
                    }
                    if (checkersBoard.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 1, checkersBoardConfigurationColumn - 1)) {
                        currentDepthOfAlphaBetaTreeBranch++;
                        totalNumberOfNodesGenerated++;
                        checkersAlphaBetaSearchUtilityValue = Math.min(checkersAlphaBetaSearchUtilityValue,
                                maxValue(checkersBoard.clone().executingCheckersHumanLegalMove(
                                        checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                                        checkersBoardConfigurationRow - 1, checkersBoardConfigurationColumn - 1),
                                        checkersAlphaBetaSearchAlphaValue, checkersAlphaBetaSearchBetaValue));
                        if (checkersAlphaBetaSearchUtilityValue <= checkersAlphaBetaSearchAlphaValue) {
                            numberOfTimePruningOccurredInMinValueFunction++;
                            currentDepthOfAlphaBetaTreeBranch--;
                            return checkersAlphaBetaSearchUtilityValue;
                        }
                        checkersAlphaBetaSearchBetaValue = Math.min(checkersAlphaBetaSearchBetaValue,
                                checkersAlphaBetaSearchUtilityValue);
                    }
                    if (checkersBoard.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 2, checkersBoardConfigurationColumn + 2)) {
                        currentDepthOfAlphaBetaTreeBranch++;
                        totalNumberOfNodesGenerated++;
                        checkersAlphaBetaSearchUtilityValue = Math.min(checkersAlphaBetaSearchUtilityValue,
                                maxValue(checkersBoard.clone().executingCheckersHumanLegalMove(
                                        checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                                        checkersBoardConfigurationRow - 2, checkersBoardConfigurationColumn + 2),
                                        checkersAlphaBetaSearchAlphaValue, checkersAlphaBetaSearchBetaValue));
                        if (checkersAlphaBetaSearchUtilityValue <= checkersAlphaBetaSearchAlphaValue) {
                            numberOfTimePruningOccurredInMinValueFunction++;
                            currentDepthOfAlphaBetaTreeBranch--;
                            return checkersAlphaBetaSearchUtilityValue;
                        }
                        checkersAlphaBetaSearchBetaValue = Math.min(checkersAlphaBetaSearchBetaValue,
                                checkersAlphaBetaSearchUtilityValue);
                    }
                    if (checkersBoard.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 2, checkersBoardConfigurationColumn - 2)) {
                        currentDepthOfAlphaBetaTreeBranch++;
                        totalNumberOfNodesGenerated++;
                        checkersAlphaBetaSearchUtilityValue = Math.min(checkersAlphaBetaSearchUtilityValue,
                                maxValue(checkersBoard.clone().executingCheckersHumanLegalMove(
                                        checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                                        checkersBoardConfigurationRow - 2, checkersBoardConfigurationColumn - 2),
                                        checkersAlphaBetaSearchAlphaValue, checkersAlphaBetaSearchBetaValue));
                        if (checkersAlphaBetaSearchUtilityValue <= checkersAlphaBetaSearchAlphaValue) {
                            numberOfTimePruningOccurredInMinValueFunction++;
                            currentDepthOfAlphaBetaTreeBranch--;
                            return checkersAlphaBetaSearchUtilityValue;
                        }
                        checkersAlphaBetaSearchBetaValue = Math.min(checkersAlphaBetaSearchBetaValue,
                                checkersAlphaBetaSearchUtilityValue);
                    }
                }
            }
        }
        currentDepthOfAlphaBetaTreeBranch--;
        return checkersAlphaBetaSearchUtilityValue;
    }

    /**
     *
     * @return the utility value of checkers board configuration when a cut-off
     * occurs using different heuristic functions
     */
    private int alphaBetaTreeEvaluationFunction() {

        return (numberOfComputerPieceLeftHeuristic() - numberOfHumanPieceLeftHeuristic()
                + averageProximityOfComputerPiecesFromOtherSideHeuristic()
                - averageProximityOfHumanPiecesFromOtherSideHeuristic()
                + numberOfComputerPieceCaptureMovePossibleHeuristic() - numberOfHumanPieceCaptureMovePossibleHeuristic()
                + numberOfComputerPieceRegularMovePossibleHeuristic() - numberOfHumanPieceRegularMovePossibleHeuristic()
                + safeComputerPiecesHeuristic() - safeHumanPiecesHeuristic()
                + emptySpaceAdjacentToMultipleComputerPieceHeuristic()
                - emptySpaceAdjacentToMultipleHumanPieceHeuristic());

    }

    /**
     *
     * @return the number of human pieces left on checkers board
     */
    private int numberOfHumanPieceLeftHeuristic() {
        int numberOfHumanPieceLeft = 0;
        for (int checkersBoardConfigurationRow = this.checkersBoardConfiguration.length
                - 1; checkersBoardConfigurationRow >= 0; checkersBoardConfigurationRow--) {
            for (int checkersBoardConfigurationColumn = this.checkersBoardConfiguration[0].length
                    - 1; checkersBoardConfigurationColumn >= 0; checkersBoardConfigurationColumn--) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 1) {
                    numberOfHumanPieceLeft++;
                }
            }
        }
        return numberOfHumanPieceLeft;
    }

    /**
     *
     * @return the number of computer pieces left on checkers board
     */
    private int numberOfComputerPieceLeftHeuristic() {
        int numberOfComputerPieceLeft = 0;
        for (int[] checkersBoardConfiguration1 : this.checkersBoardConfiguration) {
            for (int checkersBoardConfigurationColumn = 0; checkersBoardConfigurationColumn < this.checkersBoardConfiguration[0].length; checkersBoardConfigurationColumn++) {
                if (checkersBoardConfiguration1[checkersBoardConfigurationColumn] == 2) {
                    numberOfComputerPieceLeft++;
                }
            }
        }
        return numberOfComputerPieceLeft;
    }

    /**
     *
     * @return the average proximity of all the human pieces from the other side
     */
    private int averageProximityOfHumanPiecesFromOtherSideHeuristic() {
        int numberOfHumanPiecesLeft = 0;
        int sumOfProximityOfHumanPiecesFromOtherSide = 0;
        for (int checkersBoardConfigurationRow = this.checkersBoardConfiguration.length
                - 1; checkersBoardConfigurationRow >= 0; checkersBoardConfigurationRow--) {
            for (int checkersBoardConfigurationColumn = this.checkersBoardConfiguration[0].length
                    - 1; checkersBoardConfigurationColumn >= 0; checkersBoardConfigurationColumn--) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 1) {
                    numberOfHumanPiecesLeft++;
                    sumOfProximityOfHumanPiecesFromOtherSide += this.checkersBoardConfiguration[0].length
                            - checkersBoardConfigurationColumn;
                }
            }
        }
        return sumOfProximityOfHumanPiecesFromOtherSide / numberOfHumanPiecesLeft;
    }

    /**
     *
     * @return the average proximity of all the computer pieces from the other
     * side
     */
    private int averageProximityOfComputerPiecesFromOtherSideHeuristic() {
        int numberOfComputerPiecesLeft = 0;
        int sumOfProximityOfComputerPiecesFromOtherSide = 0;
        for (int[] checkersBoardConfiguration1 : this.checkersBoardConfiguration) {
            for (int checkersBoardConfigurationColumn = 0; checkersBoardConfigurationColumn < this.checkersBoardConfiguration[0].length; checkersBoardConfigurationColumn++) {
                if (checkersBoardConfiguration1[checkersBoardConfigurationColumn] == 2) {
                    numberOfComputerPiecesLeft++;
                    sumOfProximityOfComputerPiecesFromOtherSide += this.checkersBoardConfiguration[0].length
                            - checkersBoardConfigurationColumn;
                }
            }
        }
        return sumOfProximityOfComputerPiecesFromOtherSide / numberOfComputerPiecesLeft;
    }

    /**
     *
     * @return the number of human piece capture move possible
     */
    private int numberOfHumanPieceCaptureMovePossibleHeuristic() {
        int numberOfHumanPieceCaptureMovePossible = 0;
        for (int checkersBoardConfigurationRow = this.checkersBoardConfiguration.length
                - 1; checkersBoardConfigurationRow >= 0; checkersBoardConfigurationRow--) {
            for (int checkersBoardConfigurationColumn = this.checkersBoardConfiguration[0].length
                    - 1; checkersBoardConfigurationColumn >= 0; checkersBoardConfigurationColumn--) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 1) {
                    if (this.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 2, checkersBoardConfigurationColumn - 2)) {
                        numberOfHumanPieceCaptureMovePossible++;
                    }
                    if (this.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 2, checkersBoardConfigurationColumn + 2)) {
                        numberOfHumanPieceCaptureMovePossible++;
                    }
                }
            }
        }

        return numberOfHumanPieceCaptureMovePossible;
    }

    /**
     *
     * @return the number of computer piece capture move possible
     */
    private int numberOfComputerPieceCaptureMovePossibleHeuristic() {
        int numberOfComputerPieceCaptureMovePossible = 0;
        for (int checkersBoardConfigurationRow = 0; checkersBoardConfigurationRow < this.checkersBoardConfiguration.length; checkersBoardConfigurationRow++) {
            for (int checkersBoardConfigurationColumn = 0; checkersBoardConfigurationColumn < this.checkersBoardConfiguration[0].length; checkersBoardConfigurationColumn++) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 2) {
                    if (this.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn - 2)) {
                        numberOfComputerPieceCaptureMovePossible++;
                    }
                    if (this.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 2, checkersBoardConfigurationColumn + 2)) {
                        numberOfComputerPieceCaptureMovePossible++;
                    }
                }
            }
        }
        return numberOfComputerPieceCaptureMovePossible;
    }

    /**
     *
     * @return the number of human piece regular move possible
     */
    private int numberOfHumanPieceRegularMovePossibleHeuristic() {
        int numberOfHumanPieceRegularMovePossible = 0;
        for (int checkersBoardConfigurationRow = this.checkersBoardConfiguration.length
                - 1; checkersBoardConfigurationRow >= 0; checkersBoardConfigurationRow--) {
            for (int checkersBoardConfigurationColumn = this.checkersBoardConfiguration[0].length
                    - 1; checkersBoardConfigurationColumn >= 0; checkersBoardConfigurationColumn--) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 1) {
                    if (this.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 1, checkersBoardConfigurationColumn - 1)) {
                        numberOfHumanPieceRegularMovePossible++;
                    }
                    if (this.humanLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow - 1, checkersBoardConfigurationColumn + 1)) {
                        numberOfHumanPieceRegularMovePossible++;
                    }
                }
            }
        }

        return numberOfHumanPieceRegularMovePossible++;
    }

    /**
     *
     * @return the number of computer piece regular move possible
     */
    private int numberOfComputerPieceRegularMovePossibleHeuristic() {
        int numberOfComputerPieceRegularMovePossible = 0;
        for (int checkersBoardConfigurationRow = 0; checkersBoardConfigurationRow < this.checkersBoardConfiguration.length; checkersBoardConfigurationRow++) {
            for (int checkersBoardConfigurationColumn = 0; checkersBoardConfigurationColumn < this.checkersBoardConfiguration[0].length; checkersBoardConfigurationColumn++) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 2) {
                    if (this.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 1, checkersBoardConfigurationColumn - 1)) {
                        numberOfComputerPieceRegularMovePossible++;
                    }
                    if (this.computerLegalMove(checkersBoardConfigurationRow, checkersBoardConfigurationColumn,
                            checkersBoardConfigurationRow + 1, checkersBoardConfigurationColumn + 1)) {
                        numberOfComputerPieceRegularMovePossible++;
                    }
                }
            }
        }

        return numberOfComputerPieceRegularMovePossible++;
    }

    /**
     *
     * @return the number of safe human pieces along the edges of the checkers
     * board
     */
    private int safeHumanPiecesHeuristic() {
        int safeHumanPieces = 0;
        for (int checkersBoardHumanPiecePosition = 1; checkersBoardHumanPiecePosition < this.checkersBoardConfiguration.length
                - 1; checkersBoardHumanPiecePosition++) {
            if (this.checkersBoardConfiguration[checkersBoardHumanPiecePosition][0] == 1) {
                safeHumanPieces++;
            } else if (this.checkersBoardConfiguration[checkersBoardHumanPiecePosition][checkersBoardConfiguration.length
                    - 1] == 1) {
                safeHumanPieces++;
            } else if (this.checkersBoardConfiguration[0][checkersBoardHumanPiecePosition] == 1) {
                safeHumanPieces++;
            } else if (this.checkersBoardConfiguration[checkersBoardConfiguration.length
                    - 1][checkersBoardHumanPiecePosition] == 1) {
                safeHumanPieces++;
            }
        }
        if (this.checkersBoardConfiguration[0][0] == 1) {
            safeHumanPieces++;
        } else if (this.checkersBoardConfiguration[0][this.checkersBoardConfiguration.length - 1] == 1) {
            safeHumanPieces++;
        } else if (this.checkersBoardConfiguration[this.checkersBoardConfiguration.length - 1][0] == 1) {
            safeHumanPieces++;
        } else if (this.checkersBoardConfiguration[this.checkersBoardConfiguration.length
                - 1][this.checkersBoardConfiguration.length - 1] == 1) {
            safeHumanPieces++;
        }
        return safeHumanPieces;
    }

    /**
     *
     * @return the number of safe computer pieces along the edges of the
     * checkers board
     */
    private int safeComputerPiecesHeuristic() {
        int safeComputerPieces = 0;
        for (int checkersBoardComputerPiecePosition = 1; checkersBoardComputerPiecePosition < this.checkersBoardConfiguration.length
                - 1; checkersBoardComputerPiecePosition++) {
            if (this.checkersBoardConfiguration[checkersBoardComputerPiecePosition][0] == 2) {
                safeComputerPieces++;
            } else if (this.checkersBoardConfiguration[checkersBoardComputerPiecePosition][checkersBoardConfiguration.length
                    - 1] == 2) {
                safeComputerPieces++;
            } else if (this.checkersBoardConfiguration[0][checkersBoardComputerPiecePosition] == 2) {
                safeComputerPieces++;
            } else if (this.checkersBoardConfiguration[checkersBoardConfiguration.length
                    - 1][checkersBoardComputerPiecePosition] == 2) {
                safeComputerPieces++;
            }
        }
        if (this.checkersBoardConfiguration[0][0] == 2) {
            safeComputerPieces++;
        } else if (this.checkersBoardConfiguration[0][this.checkersBoardConfiguration.length - 1] == 2) {
            safeComputerPieces++;
        } else if (this.checkersBoardConfiguration[this.checkersBoardConfiguration.length - 1][0] == 2) {
            safeComputerPieces++;
        } else if (this.checkersBoardConfiguration[this.checkersBoardConfiguration.length
                - 1][this.checkersBoardConfiguration.length - 1] == 2) {
            safeComputerPieces++;
        }
        return safeComputerPieces;
    }

    /**
     *
     * @return the number of patterns where a blank space is surrounded by three
     * or four human pieces
     */
    private int emptySpaceAdjacentToMultipleHumanPieceHeuristic() {
        int emptySpaceAdjacentToMultipleHumanPiece = 0;
        for (int checkersBoardConfigurationRow = this.checkersBoardConfiguration.length
                - 1; checkersBoardConfigurationRow >= 0; checkersBoardConfigurationRow--) {
            for (int checkersBoardConfigurationColumn = this.checkersBoardConfiguration[0].length
                    - 1; checkersBoardConfigurationColumn >= 0; checkersBoardConfigurationColumn--) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 0) {
                    if (emptySpaceHasAdjacentMultipleHumanPieces(checkersBoardConfigurationRow,
                            checkersBoardConfigurationColumn)) {
                        emptySpaceAdjacentToMultipleHumanPiece++;
                    }
                }
            }
        }

        return emptySpaceAdjacentToMultipleHumanPiece;
    }

    /**
     *
     * @param emptySpaceXPosition blank space position X Co-ordinate
     * @param emptySpaceYPosition blank space position Y Co-ordinate
     * @return boolean value depending on whether the blank space is surrounded
     * by three or four human pieces
     */
    private boolean emptySpaceHasAdjacentMultipleHumanPieces(int emptySpaceXPosition, int emptySpaceYPosition) {
        return ((checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition - 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition - 1] == 1)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition - 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition - 1][emptySpaceYPosition] == 1)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition + 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition + 1] == 1))
                || ((checkersBoardConfigurationBoundChecks(emptySpaceXPosition - 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition - 1][emptySpaceYPosition] == 1)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition + 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition + 1] == 1)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition + 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition + 1][emptySpaceYPosition] == 1))
                || ((checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition + 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition + 1] == 1)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition + 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition + 1][emptySpaceYPosition] == 1)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition - 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition - 1] == 1))
                || ((checkersBoardConfigurationBoundChecks(emptySpaceXPosition + 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition + 1][emptySpaceYPosition] == 1)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition - 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition - 1] == 1)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition - 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition
                - 1][emptySpaceYPosition] == 1));
    }

    /**
     *
     * @return the number of patterns where a blank space is surrounded by three
     * or four computer pieces
     */
    private int emptySpaceAdjacentToMultipleComputerPieceHeuristic() {
        int emptySpaceAdjacentToMultipleComputerPiece = 0;
        for (int checkersBoardConfigurationRow = 0; checkersBoardConfigurationRow < this.checkersBoardConfiguration.length; checkersBoardConfigurationRow++) {
            for (int checkersBoardConfigurationColumn = 0; checkersBoardConfigurationColumn < this.checkersBoardConfiguration[0].length; checkersBoardConfigurationColumn++) {
                if (this.checkersBoardConfiguration[checkersBoardConfigurationRow][checkersBoardConfigurationColumn] == 0) {
                    if (emptySpaceHasAdjacentMultipleComputerPieces(checkersBoardConfigurationRow,
                            checkersBoardConfigurationColumn)) {
                        emptySpaceAdjacentToMultipleComputerPiece++;
                    }
                }
            }
        }
        return emptySpaceAdjacentToMultipleComputerPiece;
    }

    /**
     *
     * @param emptySpaceXPosition blank space position x co-ordinate
     * @param emptySpaceYPosition blank space position y co-ordinate
     * @return boolean value depending on whether the blank space is surrounded
     * by three or four computer pieces
     */
    private boolean emptySpaceHasAdjacentMultipleComputerPieces(int emptySpaceXPosition, int emptySpaceYPosition) {
        return ((checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition - 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition - 1] == 2)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition - 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition - 1][emptySpaceYPosition] == 2)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition + 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition + 1] == 2))
                || ((checkersBoardConfigurationBoundChecks(emptySpaceXPosition - 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition - 1][emptySpaceYPosition] == 2)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition + 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition + 1] == 2)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition + 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition + 1][emptySpaceYPosition] == 2))
                || ((checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition + 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition + 1] == 2)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition + 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition + 1][emptySpaceYPosition] == 2)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition - 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition - 1] == 2))
                || ((checkersBoardConfigurationBoundChecks(emptySpaceXPosition + 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition + 1][emptySpaceYPosition] == 2)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition, emptySpaceYPosition - 1)
                && this.checkersBoardConfiguration[emptySpaceXPosition][emptySpaceYPosition - 1] == 2)
                && (checkersBoardConfigurationBoundChecks(emptySpaceXPosition - 1, emptySpaceYPosition)
                && this.checkersBoardConfiguration[emptySpaceXPosition
                - 1][emptySpaceYPosition] == 2));
    }

    /**
     *
     * @param checkersBoardXPosition checkers piece or blank space position x
     * co-ordinate
     * @param checkersBoardYPosition checkers piece or blank space position y
     * co-ordinate
     * @return boolean value depending on whether the position is outside the
     * checkers board configuration
     */
    private boolean checkersBoardConfigurationBoundChecks(int checkersBoardXPosition, int checkersBoardYPosition) {
        return !(checkersBoardXPosition < 0 || checkersBoardXPosition > 5 || checkersBoardYPosition < 0
                || checkersBoardYPosition > 5);
    }

    /**
     *
     * @return alpha beta tree statistics as a string
     */
    public static String printAlphaBetaTreeStatistics() {
        StringBuilder alphaBetaTreeStatisticsOutput = new StringBuilder();
        alphaBetaTreeStatisticsOutput.append("Maximum Depth Of The Alpha Beta Tree\t:\t").append(CheckersNutshell.maximumDepthOfAlphaBetaTree).append("\nTotal Number Of Nodes Generated In The Alpha Beta Tree\t:\t").append(CheckersNutshell.totalNumberOfNodesGenerated).append("\nNumber Of Times Pruning Occurred In Max Value Function\t:\t").append(CheckersNutshell.numberOfTimePruningOccurredInMaxValueFunction).append("\nNumber Of Times Pruning Occurred In Min Value Function\t:\t").append(CheckersNutshell.numberOfTimePruningOccurredInMinValueFunction);
        if (alphaBetaTreeCutOffLevelSet != 0) {
            alphaBetaTreeStatisticsOutput.append("\nAlpha Beta Tree Cut-Off Level Set\t:\t").append(CheckersNutshell.alphaBetaTreeCutOffLevelSet).append("\n");
        }
        return alphaBetaTreeStatisticsOutput.toString();
    }
}
