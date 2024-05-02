package com.example.restaurant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hotel.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class addReserveTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_table);

        EditText et_number = findViewById(R.id.et_number);
        EditText et_nOfPersons = findViewById(R.id.et_nOfPersons);
        EditText et_price = findViewById(R.id.et_price);
        Button btn = findViewById(R.id.btn_add);
        ProgressBar prog = findViewById(R.id.prog);

        prog.setVisibility(View.INVISIBLE);

       ArrayList<HashMap<String, String>> tables = new ArrayList<>();
        ArrayAdapter<HashMap<String, String>> adapter = new ArrayAdapter<>(this,
        android.R.layout.simple_spinner_item, tables);


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://half-a-dozen-wonder.000webhostapp.com/gettables.php";

        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i< response.length(); i++){
                    HashMap<String, String> map = new HashMap<>();
                    try {
                        JSONObject row = response.getJSONObject(i);
                        map.put("id", String.valueOf(row.getInt("id")));
                        map.put("type", row.getString("type_name"));
                        tables.add(map);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//end for
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(addReserveTable.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://half-a-dozen-wonder.000webhostapp.com/addtables.php";
                btn.setEnabled(false);
                prog.setVisibility(View.VISIBLE);

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(addReserveTable.this, response, Toast.LENGTH_SHORT).show();
                        btn.setEnabled(true);
                        prog.setVisibility(View.INVISIBLE);
                        et_number.setText("");
                        et_nOfPersons.setText("");
                        et_price.setText("");
                        TextView tv = findViewById(R.id.lblMsg);
                        tv.setText(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(addReserveTable.this, error.toString(), Toast.LENGTH_LONG).show();
                        btn.setEnabled(true);
                        prog.setVisibility(View.INVISIBLE);
                        TextView tv = findViewById(R.id.lblMsg);
                        tv.setText(error.toString());
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        map.put("table_number", et_number.getText().toString());
                       // map.put("type", ((HashMap)sp.getSelectedItem()).get("id").toString());
                        map.put("price", et_price.getText().toString());
                        map.put("nOfPersons", et_nOfPersons.getText().toString());
                        map.put("key", "Test@12345");
                        return map;
                    }
                };
                queue.add(request);
            }
        });
    }
}