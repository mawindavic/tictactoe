package com.mawindavic.tictactoegame;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private Boolean first_player = true;
    private char[][] game_board;
    private Button player1;
    private Button player2;
    private int count,player_1_score = 0,player_2_score = 0,gamesCount=0,gamesDrawed=0;
    private String player_1, player_2;
    private GridLayout gameBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //shared preference
        SharedPreferences preferences = GameActivity.this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        count = preferences.getInt("COUNT", 3);
        game_board = new char[count][count];
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);


        String game_type = getIntent().getStringExtra("game_type");
        player_1 = getIntent().getStringExtra("player_1_name");
        player_2 = getIntent().getStringExtra("player_2_name");
        player_1_score = getIntent().getIntExtra("player_1_score",0);
        player_2_score = getIntent().getIntExtra("player_2_score",0);
        gamesCount = getIntent().getIntExtra("games_played",0);
        gamesDrawed = getIntent().getIntExtra("games_drawed",0);

        if (game_type.trim().equalsIgnoreCase("two_player")) {
            if(player_1.isEmpty() && player_2.isEmpty()){
                takePlayersNames();
            }else {
                player1.setText(player_1);
                player2.setText(player_2);
                setCurrentScore();
            }

        } else {
            player_1 = getString(R.string.you);
            player_2 = getString(R.string.com);
            player1.setText(player_1);
            player2.setText(player_2);
            setCurrentScore();
        }

        GridLayout gameBoard_3 = findViewById(R.id.game_board);
        GridLayout gameBoard_5 = findViewById(R.id.game_board_5);
        if (count == 5) {
            gameBoard_3.setVisibility(View.GONE);
            gameBoard_5.setVisibility(View.VISIBLE);
            gameBoard = gameBoard_5;
        } else {
            gameBoard_5.setVisibility(View.GONE);
            gameBoard_3.setVisibility(View.VISIBLE);
            gameBoard = gameBoard_3;
        }

        int position = 0;
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                final int finalI = i;
                final int finalJ = j;
                gameBoard.getChildAt(position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        markView(v, finalI, finalJ);
                    }
                });
                position++;
            }

        }

        //resetting the board
        Button resetBtn = findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });
    }

    private void resetBoard() {
        game_board = new char[count][count];
        first_player = true;
        int position = 0;
        for (int i = 0; i < count ; i++) {
            for (int j = 0; j < count; j++) {
                TextView view = (TextView) gameBoard.getChildAt(position);
                view.setText("");
                position ++;
            }

        }
        player_1_score = player_2_score = gamesCount= gamesDrawed=0;
        setCurrentScore();
    }

    /**
     * Sets scores
     */
    private void setCurrentScore() {
        TextView playerOneScore = findViewById(R.id.player_1_score);
        TextView playerTwoScore = findViewById(R.id.player_2_score);
        TextView gamesPlayed = findViewById(R.id.games_count);
        TextView gamesDraw = findViewById(R.id.game_draws);


        String scoreOne = player_1+" "+ player_1_score;
        String scoreTwo = player_2 +" "+player_2_score;
        String gamesCount_1 = "Games "+gamesCount;
        String gamesDraw_1 ="Draws "+gamesDrawed;


        playerOneScore.setText(scoreOne);
        playerTwoScore.setText(scoreTwo);
        gamesPlayed.setText(gamesCount_1);
        gamesDraw.setText(gamesDraw_1);
    }

    /**
     * Takes players name in two players game
     */
    private void takePlayersNames() {
        final Dialog dialog = new Dialog(GameActivity.this);
        LayoutInflater inflater = LayoutInflater.from(GameActivity.this);
        @SuppressLint("InflateParams") View subView = inflater.inflate(R.layout.players_layout, null);

        //initializing views
        final TextInputEditText player1Name = subView.findViewById(R.id.player_1_name);
        final TextInputEditText player2Name = subView.findViewById(R.id.player_2_name);
        Button cancel = subView.findViewById(R.id.cancel_btn);
        Button submit = subView.findViewById(R.id.submit_btn);

        //Views Actions
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namesAreValid(player1Name, player2Name)) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setTitle(R.string.players_names_3_char);
        dialog.setContentView(subView);
        dialog.setCancelable(false);
        dialog.show();

    }

    /**
     * Checks if players are valid
     *
     * @param player1Name for first player
     * @param player2Name for second player
     * @return boolean
     */
    private boolean namesAreValid(TextInputEditText player1Name, TextInputEditText player2Name) {
        player_1 = player1Name.getText().toString();
        player_2 = player2Name.getText().toString();

        String error = "Name Required(Not COM)";


        if (player_1.isEmpty() || player_1.equalsIgnoreCase("COM")) {
            player1Name.requestFocus();
            player1Name.setError(error);
            return false;
        } else if (player_2.isEmpty() || player_2.equalsIgnoreCase("COM")) {
            player2Name.requestFocus();
            player2Name.setError(error);
            return false;
        } else {
            player1.setText(player_1);
            player2.setText(player_2);
        }
        setCurrentScore();
        return true;
    }

    /**
     * Mark gridview either X or O
     *
     * @param childAt :position of grid in Grid Layout
     * @param row     of view in Grid Layout
     * @param col     of view in Grid Layout
     */
    private void markView(View childAt, int row, int col) {
        TextView childView = (TextView) childAt;
        if (childView.getText().toString().isEmpty()) {
            char c = 'X';
            childView.setTextColor(Color.RED);
            String currentPlayer = player1.getText().toString();
            if (!first_player) {
                c = 'O';
                childView.setTextColor(Color.GREEN);
                currentPlayer = player2.getText().toString();
            }

            String string = "" + c;
            childView.setText(string);
            game_board[row][col] = c;

            if (isThisPlayerWinner(c)) {
                if(first_player){
                    player_1_score ++;
                }else {
                    player_2_score ++;
                }
                gamesCount++;
                gameOver(currentPlayer);

            } else if (isGameBoardFull()) {
                gamesDrawed++;
                gamesCount++;
                gameOver("draw");
            } else {
                changeActivePlayer();
            }

        }

    }

    /**
     * Intent to game over activity incase of win or draw
     * @param msg for draw or winners name
     */
    private void gameOver(String msg) {
        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
        intent.putExtra("winner", msg);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("game_type", getIntent().getStringExtra("game_type"));
        intent.putExtra("player_1_score",player_1_score);
        intent.putExtra("player_2_score",player_2_score);
        intent.putExtra("player_1_name",player_1);
        intent.putExtra("player_2_name",player_2);
        intent.putExtra("games_played",gamesCount);
        intent.putExtra("games_drawed",gamesDrawed);
        startActivity(intent);
        finish();
    }

    /**
     * Changes active player
     */
    private void changeActivePlayer() {
        if (first_player) {
            player1.setBackgroundResource(R.drawable.inactive_player_background);
            player2.setBackgroundResource(R.drawable.active_player_background);
        } else {
            player2.setBackgroundResource(R.drawable.inactive_player_background);
            player1.setBackgroundResource(R.drawable.active_player_background);
        }
        first_player = !first_player;

        if (!first_player && player_2.equalsIgnoreCase("COM")) {
            coms_play();
        }
    }

    /**
     * Com play incase of single player
     */
    private void coms_play() {
         ComsMove  comsMove = new ComsMove(game_board,count);
        int[] compMove =  comsMove.callComsBestMove();
        markView(gameBoard.getChildAt(compMove[0]),compMove[1],compMove[2]);
    }
    /**
     * Check if board is full i.e game draw
     *
     * @return boolean
     */
    private boolean isGameBoardFull() {
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                if (game_board[i][j] != 'X' && game_board[i][j] != 'O') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if recent played player is winner
     *
     * @param c symbol for recent played player
     * @return boolean
     */
    private boolean isThisPlayerWinner(char c) {
        return checkRowWin(c) || checkColumnWin(c) || checkDiagonalWin(c);
    }


    /**
     * Check for win in diagonals
     *
     * @param c char checked for win
     * @return boolean
     */
    private boolean checkDiagonalWin(char c) {
        if (count == 5) {
            return compareChar(c, game_board[0][0], game_board[1][1], game_board[2][2], game_board[3][3], game_board[4][4]) || compareChar(c, game_board[4][0], game_board[3][1], game_board[2][2], game_board[1][3], game_board[0][4]);
        } else if (count == 3) {
            return compareChar(c, game_board[0][0], game_board[1][1], game_board[2][2]) || compareChar(c, game_board[2][0], game_board[1][1], game_board[0][2]);
        }
        return false;
    }

    /**
     * Check for win in columns
     *
     * @param c char checked for win
     * @return boolean
     */
    private boolean checkColumnWin(char c) {

        if (count == 5) {
            for (int i = 0; i < count; i++) {
                if (compareChar(c, game_board[0][i], game_board[1][i], game_board[2][i], game_board[3][i], game_board[4][i])) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < count; i++) {
                if (compareChar(c, game_board[0][i], game_board[1][i], game_board[2][i])) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check for win in rows
     *
     * @param c char checked for win
     * @return boolean
     */
    private boolean checkRowWin(char c) {

        if (count == 5) {
            for (int i = 0; i < count; i++) {
                if (compareChar(c, game_board[i][0], game_board[i][1], game_board[i][2], game_board[i][3], game_board[i][4])) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < count; i++) {
                if (compareChar(c, game_board[i][0], game_board[i][1], game_board[i][2])) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Compare chars in 3 by 3 game if are equal for win
     *
     * @param c  reference char
     * @param c1 char 1
     * @param c2 char 2
     * @param c3 char 3
     * @return boolean
     */
    private boolean compareChar(char c, char c1, char c2, char c3) {
        return ((c == c1) && (c1 == c2) && (c2 == c3));
    }

    /**
     * Compare chars in 3 by 3 game if are equal for win
     *
     * @param c  reference char
     * @param c1 char 1
     * @param c2 char 2
     * @param c3 char 3
     * @param c4 char 4
     * @param c5 char 5
     * @return boolean
     */
    private boolean compareChar(char c, char c1, char c2, char c3, char c4, char c5) {
        return ((c == c1) && (c1 == c2) && (c2 == c3) && (c3 == c4) && (c4 == c5));
    }



}
