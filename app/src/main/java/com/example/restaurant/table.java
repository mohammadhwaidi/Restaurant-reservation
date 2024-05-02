package com.example.restaurant;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotel.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class table extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        ListView lst = findViewById(R.id.lstTables);
        ArrayList<HashMap<String, String>> tables = new ArrayList<>();
        ArrayAdapter<HashMap<String, String>> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, tables);
        lst.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://half-a-dozen-wonder.000webhostapp.com/gettables.php";

        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject row = response.getJSONObject(i);
                        String id = String.valueOf(row.getInt("id"));
                        String tableNumber = String.valueOf(row.getInt("table_number"));
                        String nOfPersons = String.valueOf(row.getInt("nOfpersons"));
                        String price = String.valueOf(row.getDouble("price"));

                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", id);
                        map.put("table_number", tableNumber);
                        map.put("nOfPersons", nOfPersons);
                        map.put("price", price);

                        tables.add(map);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(table.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}
