package waffleo.hackisu;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Set;
import java.util.ArrayList;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;

//The main, first window of the app. A presso of a button summons a list which displays
//every Bluetooth device paired with the Android. Pressing any item in the list
//then initiates a (attempted) connection via Bluetooth.

public class DeviceList extends AppCompatActivity {

    //Main activity
    Button btnPaired, quitButton;
    ListView devicelist;

    //Bluetooth
    BluetoothAdapter myBluetooth = null;
    Set<BluetoothDevice> pairedDevices; //Check later - may not work with explicit Data Type

    //Address (inconsequential on its own but necessary for preserving the Bluetooth device address between activities)
    public final static String EXTRA_ADDRESS = "address";

    //List View - touch List to choose a Bluetooth connection
    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            // Make an intent to start next activity.
            Intent i = new Intent(DeviceList.this, GamesList.class);
            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Start up main view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign btnPaired to one of the display buttons
        btnPaired = (Button)findViewById(R.id.find);
        quitButton = (Button) findViewById(R.id.exit);
        devicelist = (ListView)findViewById(R.id.listView);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            finish();
        }
        else if (!myBluetooth.isEnabled()) { //Not enabled
            //Ask user to turn Bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }

        //Button Paired Listener - Identifies nearby Bluetooth devices
        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pairedDevicesList(); //method that will be called
            }
        });

        //Quit Button - exits application
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

    }

    //OTHER METHODS

    //Find Arduino (or anything else paired with device)
    private boolean pairedDevicesList() {

        boolean connected = true;
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else //No devices found
        {
            Toast.makeText(getApplicationContext(), "No Devices Found.", Toast.LENGTH_LONG).show();
            connected = false;
        }

        //Generate List
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

        return connected;
    }
}
