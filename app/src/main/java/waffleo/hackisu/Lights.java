package waffleo.hackisu;

import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

//TODO - UNFINISHED CLASS

//Meant to control a simple LED on the Arduino as its own operation separate from the games
//Couldn't get around to it, though
public class Lights extends AppCompatActivity {

    BluetoothSocket bt;

    Button on, off, disconnect;
    SeekBar brightness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lights);

        //Button assignments
        on = (Button)findViewById(R.id.buttonOn);
        off = (Button)findViewById(R.id.buttonOff);
        disconnect = (Button)findViewById(R.id.disconnect);

        brightness = (SeekBar) findViewById(R.id.seekBar);

        //Setting touch commands for each button
        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOnLED();
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOffLED();
            }
        });

        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //OnTouch methods - On, Off, Dismiss
    void turnOnLED() {
        if (bt!=null)
        {
            try {
                bt.getOutputStream().write("TO".getBytes());
            } catch (IOException e) {
                msg("Error Turning On LED");
            }
        }
    }

    void turnOffLED() {
        if (bt!=null)
        {
            try
            {
                bt.getOutputStream().write("TF".getBytes());
            }
            catch (IOException e)
            {
                msg("Error Turning Off LED");
            }
        }
    }

    void dismiss() {
        if (bt!=null) //If the btSocket is busy
        {
            try
            {
                bt.close(); //close connection
            }
            catch (IOException e)
            { msg("Error Closing the Connection");}
        }
        finish(); //return to the first layout
    }

    //Toast a message
    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    //TODO - Seekbar
}
