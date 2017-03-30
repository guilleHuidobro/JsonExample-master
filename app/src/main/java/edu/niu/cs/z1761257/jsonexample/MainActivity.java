package edu.niu.cs.z1761257.jsonexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView stateListView;

    private List<Fixture> fixtureList = new ArrayList<>();
    private  FixtureArrayAdapter fixtureArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateListView = (ListView)findViewById(R.id.stateListView);
        //stateArrayAdapter = new StateArrayAdapter(this, stateList);
        fixtureArrayAdapter = new FixtureArrayAdapter(this, fixtureList);
        //stateListView.setAdapter(stateArrayAdapter);
        stateListView.setAdapter(fixtureArrayAdapter);
/*
        stateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fixture fixture = fixtureArrayAdapter.getItem(position);

                Toast.makeText(getApplicationContext(),
                        "Iniciar screen de detalle para: \n" + fixture.getEquipoLocal(),
                        Toast.LENGTH_SHORT).show();
            }
        });
*/
        getData();
    }//end of onCreate

    public void equipoLocal(){

    }

    //Handle the button click
    public void getData(){
    String urlString = getString(R.string.web_url_fixture);

        try{
            URL url = new URL(urlString);
            StateTask stateTask = new StateTask();
            stateTask.execute(url);
        }catch(Exception e){
            e.printStackTrace();
        }


    }//end of getView


    private class StateTask extends AsyncTask<URL, String,JSONObject>{

        @Override
        protected JSONObject doInBackground(URL... params) {

            HttpURLConnection connection = null;
            try{

                connection = (HttpURLConnection)params[0].openConnection();
                int response = connection.getResponseCode();

                if(response == HttpURLConnection.HTTP_OK){

                    StringBuilder builder = new StringBuilder();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while ((line = reader.readLine())!= null ) {
                            builder.append(line);
                        }
                    }catch (IOException e){
                        publishProgress("Read Error");
                        e.printStackTrace();
                    }//inner try

                    return new JSONObject(builder.toString());

                }else{
                    publishProgress("Connection Error");
                }


            }//end outer try
            catch (Exception e){

                publishProgress("Connection Error 2");
                e.printStackTrace();

            }finally {
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
           Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_SHORT).show();
        }//end of onProgressUpdate

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
           // super.onPostExecute(jsonObject);
            //convertJSONtoArrayList(jsonObject);
            convertFixtureJSONtoArrayList(jsonObject);
            //stateArrayAdapter.notifyDataSetChanged();
            fixtureArrayAdapter.notifyDataSetChanged();

            stateListView.smoothScrollToPosition(0);

        }//end of onPostExecute
    }//end of StateTask


    private void convertFixtureJSONtoArrayList(JSONObject states){

        fixtureList.clear();
        try{
            JSONArray list = states.getJSONArray("fixture");

            for(int i=0; i<list.length(); i++) {
                JSONObject stateobj = list.getJSONObject(i);
                fixtureList.add(new Fixture(stateobj.getString("equipo_local"),stateobj.getString("equipo_visitante")));

            }//end of for loop

        }catch(JSONException e){
            e.printStackTrace();

        }//end of catch

    }//end of convertJSONArrayList

    

}//end of MainActivity
