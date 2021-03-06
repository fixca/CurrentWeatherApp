package me.fixca.weather.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import me.fixca.weather.R;
import me.fixca.weather.request.Request;
import me.fixca.weather.request.URLRequest;

public class DongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Intent thisIntent = getIntent();
        ListView listView = findViewById(R.id.listview);
        try {
            String jsonString = thisIntent.getExtras().getString("jsonString");
            JSONArray jsonArray = new JSONArray(jsonString);
            List<String> list = new LinkedList<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                list.add((String) jsonObject.get("value"));
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener((parent, v, position, id) -> {
                try {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(position);
                    String x = (String) jsonObject.get("x");
                    String y = (String) jsonObject.get("y");
                    Request request = new Request(() -> {
                        URLRequest urlRequest = new URLRequest("https://www.kma.go.kr/wid/queryDFS.jsp?gridx=" + x + "&gridy=" + y);
                        String string = urlRequest.sendRequest();
                        if(string != null) {
                            Intent lvIntent = new Intent(getApplicationContext(), WeatherActivity.class);
                            lvIntent.putExtra("xmlString", string);
                            startActivity(lvIntent);
                        }
                        else {
                            Toast.makeText(DongActivity.this, "????????? ??????????????????! ????????? ????????? ????????? ????????? ??????, ?????? ?????? ??????????????????!", Toast.LENGTH_LONG).show();
                        }
                    });
                    request.execute();
                }
                catch(Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "????????? ??????????????????! ????????? ????????? ????????? ????????? ??????, ?????? ?????? ??????????????????!", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "????????? ??????????????????! ????????? ????????? ????????? ????????? ??????, ?????? ?????? ??????????????????!", Toast.LENGTH_LONG).show();
        }
    }
}