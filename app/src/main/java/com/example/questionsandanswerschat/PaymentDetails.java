package com.example.questionsandanswerschat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {

    TextView txtId,txtStatus,txtAmount;
    Button backToGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtId = findViewById(R.id.txtId);
        txtStatus = findViewById(R.id.txtStatus);
        txtAmount = findViewById(R.id.txtAmount);
        backToGame = findViewById(R.id.bt_backToGame);

        //Receive information from Donations Activity Intent

        Intent intent = getIntent();
        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            viewDetails(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }



        backToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentDetails.this, MainActivity.class));
            }
        });
    }

    private void viewDetails(JSONObject response, String Amount) {

        //Set parameters to TextViews
        try{
            txtId.setText(response.getString("id"));
            txtStatus.setText(response.getString("state"));
            txtAmount.setText("€" + Amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
