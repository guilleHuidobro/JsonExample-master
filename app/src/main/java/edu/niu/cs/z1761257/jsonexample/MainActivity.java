package edu.niu.cs.z1761257.jsonexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
    Integer partido = null;
    String prueba = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateListView = (ListView)findViewById(R.id.stateListView);
        //stateArrayAdapter = new StateArrayAdapter(this, stateList);
        fixtureArrayAdapter = new FixtureArrayAdapter(this, fixtureList);
        //stateListView.setAdapter(stateArrayAdapter);
        stateListView.setAdapter(fixtureArrayAdapter);



        stateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                partido = position + 1;

                final TextView equipoLocal = (TextView)view.findViewById(R.id.equipoLocal);
                final TextView equipoVisitante = (TextView)view.findViewById(R.id.equipoVisitante);
                final TextView empate = (TextView)view.findViewById(R.id.empate);

                equipoLocal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        equipoLocal.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPred));
                        equipoVisitante.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorEquipos));
                        empate.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorEquipos));

                        prueba = String.valueOf(partido) + " " + equipoLocal.getText()+ " " + "1";

                        comprobarPrediccion(position,prueba);
                        Toast.makeText(getApplicationContext(),prueba,
                                Toast.LENGTH_SHORT).show();

                    }
                });
                equipoVisitante.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        equipoLocal.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorEquipos));
                        equipoVisitante.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPred));
                        empate.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorEquipos));

                        prueba = String.valueOf(partido) + " " + equipoVisitante.getText()+ " " + "1";

                        comprobarPrediccion(position,prueba);
                        Toast.makeText(getApplicationContext(),prueba,
                                Toast.LENGTH_SHORT).show();

                    }
                });
                empate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        equipoLocal.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorEquipos));
                        equipoVisitante.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.textColorEquipos));
                        empate.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPred));

                        prueba = String.valueOf(partido) + " " + empate.getText()+ " " + "1";

                        comprobarPrediccion(position,prueba);
                        Toast.makeText(getApplicationContext(),prueba,
                                Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        getData();
    }//end of onCreate



    List<String> predList = new ArrayList<String>();
    List<Integer> posiciones = new ArrayList<Integer>();
    public void comprobarPrediccion(int posicion,String pred){

        if(!posiciones.contains(posicion)){
            posiciones.add(posicion);
        }else{
            posiciones.remove(posicion);
            posiciones.add(posicion);
        }

        if (posiciones.size() < 10){
            predList.add(pred);
        }

    }

    /*
    *
    *                         Toast.makeText(getApplicationContext(),
                                "Iniciar screen de detalle para: \n" + empate.getText(),
                                Toast.LENGTH_SHORT).show();
    *
    * */

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
