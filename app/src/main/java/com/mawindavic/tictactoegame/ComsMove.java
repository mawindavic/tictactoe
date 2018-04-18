package com.mawindavic.tictactoegame;
/*
 * Created by Mawinda on 13-Apr-18.
 */

import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

class ComsMove {

    private final int board_dimension;
    private int[][] game_board;
    private final char[][] game_board_char;
    private int[] compMove = new int[2];

    ComsMove(char[][] game_board_char, int board_dimension) {
        this.board_dimension = board_dimension;
        this.game_board_char = game_board_char;
        this.game_board = generateBoard(game_board_char);
    }

    private int[][] generateBoard(char[][] charBoard) {
        int[][] board = new int[board_dimension][board_dimension];
        for (int i = 0; i < charBoard.length; i++) {
            for (int j = 0; j < charBoard.length; j++) {
                if (charBoard[i][j] == 'X') {
                    board[i][j] = 1;
                } else if (charBoard[i][j] == 'O') {
                    board[i][j] = 2;
                } else {
                    board[i][j] = 0;
                }
            }
        }
        return board;
    }

    int[] callComsBestMove() {
        checkForBestMove();
        return comsBestMove();
    }

    /**
     * Determine best move
     */
    private void checkForBestMove() {
       int y = experimentWin(1);
        int x = experimentWin(2);
       int z = Math.min(x,y);
       game_board = generateBoard(game_board_char);
       if(remainingSpots().size() == 8 && board_dimension == 3 && topCornerMove()){
           z = Math.max(x,y);
       }
        compMove = remainingSpots().get(z);

    }

    private boolean topCornerMove() {
        for (int i = 0; i < 3 ; i++) {
            for (int j = 0; j <3 ; j++) {
                if(game_board[0][2] == 1){
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Check for posible win
     * @param playr signature
     * @return board depth
     */
    private int experimentWin(int playr) {
        //resetting board
        game_board = generateBoard(game_board_char);
        int player = playr;
        int position= 0;
        for (int[] point:remainingSpots()) {
            Log.e(TAG, "experimentComsWin: "+point[0]+","+point[1] );
            testMove(point,player);
            if(humanWins() || comsWin()  || remainingSpots().size() == 0){
                return position;
            }else{
                if(player == 2){
                    player = 1;
                }else {
                    player = 2;
                }
            }
            if(remainingSpots().size() != 0 )
            position ++;
        }

        return position;
    }

    /**
     * The best move for comp
     * @return array of coordinate
     */
    private int[] comsBestMove() {
        int[] bestMove = new int[3];
        int position = 0;
        for (int i = 0; i < board_dimension; i++) {
            for (int j = 0; j < board_dimension; j++) {
                if (i == compMove[0] && j == compMove[1]) {
                    bestMove[0] = position;
                    bestMove[1] = compMove[0];
                    bestMove[2] = compMove[1];
                }
                position++;
            }
        }
        return bestMove;
    }


    /**
     * Experimental move to determine best move
     * @param point coordinate
     * @param player signature
     */
    private void testMove(int[] point, int player) {
        game_board[point[0]][point[1]] = player;
    }

    /**
     * Check for unmarked spots on the board
     * @return Arraylist
     */
    private ArrayList<int[]> remainingSpots() {
        ArrayList<int[]> remainingSpots = new ArrayList<>();
        for (int i = 0; i < board_dimension ; i++) {
            for (int j = 0; j < board_dimension ; j++) {
               if(game_board[i][j]== 0){
                   int[] point = new int[2];
                   point[0]=i;
                   point[1]=j;
                  remainingSpots.add(point);
               }
            }

        }
        return remainingSpots;
    }

    /**
     * Check if Human wins
      * @return booleans
     */
    private boolean humanWins() {
        return checkRowForWin(1) || checkColumnForWin(1) || checkDiagonalForWin(1);
    }

    /**
     * Check if com wins
      * @return boolean
     */
    private boolean comsWin() {
        return checkRowForWin(2) || checkColumnForWin(2) || checkDiagonalForWin(2);
    }

    /**
     * Check win in diagonals
     * @param player signature
     * @return boolean
     */
    private boolean checkDiagonalForWin(int player) {
        if (board_dimension == 3) {
            return compare(player, game_board[0][0], game_board[1][1], game_board[2][2]) || compare(player, game_board[2][0], game_board[1][1], game_board[0][2]);
        } else if (board_dimension == 5) {
            return compare(player, game_board[0][0], game_board[1][1], game_board[2][2], game_board[3][3], game_board[4][4]) || compare(player, game_board[4][0], game_board[3][1], game_board[2][2], game_board[1][3], game_board[0][4]);
        }
        return false;
    }

    /**
     * Check win in columns
     * @param player signature
     * @return boolean
     */
    private boolean checkColumnForWin(int player) {
        for (int i = 0; i < board_dimension; i++) {
            if (board_dimension == 5) {
                if (compare(player, game_board[0][i], game_board[1][i], game_board[2][i], game_board[3][i], game_board[4][i])) {
                    return true;
                }
            } else {
                if (compare(player, game_board[0][i], game_board[1][i], game_board[2][i])) {
                    return true;
                }
            }

        }
        return false;

    }

    /**
     * Check win in Rows
      * @param player signature
     * @return boolean
     */
    private boolean checkRowForWin(int player) {
        for (int i = 0; i < board_dimension; i++) {
            if (board_dimension == 5) {
                if (compare(player, game_board[i][0], game_board[i][1], game_board[i][2], game_board[i][3], game_board[i][4])) {
                    return true;
                }
            } else {
                if (compare(player, game_board[i][0], game_board[i][1], game_board[i][2])) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * experimentWin(2);
      * @param player signature
     * @param value1 first
     * @param value2 second
     * @param value3 third
     * @param value4 forth
     * @param value5 fifth
     * @return boolean
     */
    private boolean compare(int player, int value1, int value2, int value3, int value4, int value5) {
        return ((player == value1) && (value1 == value2) && (value2 == value3) && (value3 == value4) && (value4 == value5));
    }

    /**
     * experimentWin(2);
     * @param player signature
     * @param value1 first
     * @param value2 second
     * @param value3 third
     * @return boolean
     */
    private boolean compare(int player, int value1, int value2, int value3) {
        return ((player == value1) && (value1 == value2) && (value2 == value3));
    }

}
