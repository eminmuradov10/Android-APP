package com.example.my_application;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class UserSettings extends Activity {
    String emailaddress;
    String loginpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings);
        final EditText email_address=findViewById(R.id.email_address);
        final EditText login_password=findViewById(R.id.login_password);
        Button submitbutton2=findViewById(R.id.submitbutton2);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.6),(int)(height*.5));

        submitbutton2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                emailaddress=email_address.getText().toString();
                loginpassword=login_password.getText().toString();
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://studev.groept.be/api/a19sd707/addNewUser/"+emailaddress+"/"+loginpassword;

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                // Log.d("Response",   "Response is: "+ response.substring(0,500));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response",   "That didn't work!");
                    }
                });

// Add the request to the RequestQueue.

                queue.add(stringRequest);
                finish();
            }
        });
    }
}
