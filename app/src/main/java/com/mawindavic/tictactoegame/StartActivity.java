package com.mawindavic.tictactoegame;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //single player button
        Button singlePlayerBtn = findViewById(R.id.single_player);
        singlePlayerBtn.setOnClickListener(this);

        //two players button
        Button twoPlayersBtn = findViewById(R.id.two_players);
        twoPlayersBtn.setOnClickListener(this);

        //settings button
        Button settingsBtn = findViewById(R.id.settings_btn);
        settingsBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i) {
            case R.id.single_player:
                startGame("single_player");
                break;
            case R.id.two_players:
                startGame("two_player");
                break;
            case R.id.settings_btn:
                openSettings();
                break;
        }
    }

    /**
     * Start game
     *
     * @param game_type mode
     */
    private void startGame(String game_type) {
        //intent to Game activity
        Intent intent = new Intent(StartActivity.this, GameActivity.class);
        intent.putExtra("game_type", game_type);
        intent.putExtra("player_1_score",0);
        intent.putExtra("player_2_score",0);
        intent.putExtra("player_1_name","");
        intent.putExtra("player_2_name","");
        intent.putExtra("games_played",0);
        intent.putExtra("games_drawed",0);
        startActivity(intent);
    }

    private void openSettings() {
        final Dialog dialog = new Dialog(StartActivity.this);
        CompoundButton.OnCheckedChangeListener settingListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switch (buttonView.getId()) {
                        case R.id.three_game:
                            setGameType(3);
                            break;
                        case R.id.five_game:
                            setGameType(5);
                            break;
                    }
                }
                dialog.dismiss();
            }
        };

        LayoutInflater inflater = LayoutInflater.from(StartActivity.this);
        View subView = inflater.inflate(R.layout.setting_layout, null);

        RadioButton three_board = subView.findViewById(R.id.three_game);
        three_board.setOnCheckedChangeListener(settingListener);
        RadioButton five_board = subView.findViewById(R.id.five_game);
        five_board.setOnCheckedChangeListener(settingListener);

        SharedPreferences preferences = StartActivity.this.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        if (preferences.getInt("COUNT", 3) == 3) {
            three_board.setChecked(true);
            five_board.setChecked(false);
        } else if (preferences.getInt("COUNT", 3) == 5) {
            three_board.setChecked(false);
            five_board.setChecked(true);
        }
        dialog.setTitle(R.string.select_board_size);
        dialog.setContentView(subView);
        dialog.show();

    }

    /**
     * Set board size
     *
     * @param i side units
     */
    private void setGameType(int i) {
        SharedPreferences pref = StartActivity.this.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("COUNT", i);
        editor.apply();
    }
}
