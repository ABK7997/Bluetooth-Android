package waffleo.hackisu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//TODO - UNFINISHED CLASS

public class Game1 extends AppCompatActivity {

    Button red, green, blue, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);

        red = (Button)findViewById(R.id.redButton);
        green = (Button)findViewById(R.id.greenButton);
        blue = (Button)findViewById(R.id.blueButton);
        back = (Button)findViewById(R.id.buttonBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Game1.this, GamesList.class);
                startActivity(i);
            }
        });
    }
}
