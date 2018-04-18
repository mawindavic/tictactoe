package com.mawindavic.tictactoegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        TextView msgView = findViewById(R.id.msg);
        Button retry_btn = findViewById(R.id.retry_btn);
        retry_btn.setOnClickListener(this);
        Button quit_btn = findViewById(R.id.quit_btn);
        quit_btn.setOnClickListener(this);
        String msg = getIntent().getStringExtra("winner");
        if (msg.equalsIgnoreCase("draw")) {
            msgView.setText(fromHtml("</b>GAME OVER!</b><br><strong>DRAW</strong>"));
        } else {
            ImageView img = findViewById(R.id.img);
            img.setImageResource(R.drawable.win);
            msgView.setText(fromHtml("<b>GAME OVER!</b><br><strong>"+msg+"</strong><br><b>WIN</b>"));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retry_btn:
                Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("game_type", getIntent().getStringExtra("game_type"));
                intent.putExtra("player_1_score",getIntent().getIntExtra("player_1_score",0));
                intent.putExtra("player_2_score",getIntent().getIntExtra("player_2_score",0));
                intent.putExtra("player_1_name",getIntent().getStringExtra("player_1_name"));
                intent.putExtra("player_2_name",getIntent().getStringExtra("player_2_name"));
                intent.putExtra("games_played",getIntent().getIntExtra("games_played",0));
                intent.putExtra("games_drawed",getIntent().getIntExtra("games_drawed",0));
                startActivity(intent);
                finish();
                break;
            case R.id.quit_btn:
                 finish();
                break;
        }

    }

    @SuppressWarnings("deprecation")
    private static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
