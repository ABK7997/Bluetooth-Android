package waffleo.hackisu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;

public class GamesList extends AppCompatActivity {

    //Buttons
    Button disconnect, game1, game2, lights;

    //Bluetooth Stuff
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    static UUID myUUID = UUID.fromString("8c343019-e76a-469c-baeb-b868408bf29a"); //Randomly-generated UUID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_list);

        //Supposed to retrieved UUID from the device, but doesn't work
        /*
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = tManager.getDeviceId();
        myUUID = UUID.fromString(uuid);
        System.out.println(uuid);
        */

        //get address of the bluetooth device
        Intent newint = getIntent();

        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
        System.out.println("LED ADDRESS: " + address);

        //Button Assignments
        disconnect = (Button)findViewById(R.id.disconnect);
        game1 = (Button)findViewById(R.id.game1);
        game2 = (Button)findViewById(R.id.game2);
        lights = (Button)findViewById(R.id.lights);

        //Button Touch Commands
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GamesList.this, Game1.class);
                startActivity(i);
            }
        });

        game2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        lights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Start
        ConnectBT bt = new ConnectBT() ;
        bt.execute();
    }

    //Toast a message
    private void msg(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    //Disconnect
    void dismiss() {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            {
                msg("Error Closing the Connection");
            }
        }
        finish(); //return to the first layout
    }

    //Acts like a Thread for the Bluetooth Connection
    //Has a startup and shutdown process
    //As well as a method which runs in the backround repeatedly
    //Doesn't work well for multiple activities since the connection become lost, so
    //had originally intended to change the way this works to keep the same connection constantly
    private class ConnectBT extends AsyncTask<Void, Void, Void> // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(GamesList.this, "Connecting...", "Please wait!!!");  //show a dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available

                    //The following lines don't seem to have any solution
                    //The device cannot connect to another via Bluetooth even though nothing theoretically is wrong
                    //Supposedly, this is because of some kind of problem with all Android devices beyond v4.0
                    //Aside from extremely complicated work-arounds, a solution could not be found

                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                e.printStackTrace();
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                //It never goes fine
                msg("Connection Failed.");
                finish();
            }
            else
            {
                msg("Connected");
            }
            progress.dismiss();
        }
    }
}
