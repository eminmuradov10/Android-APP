package com.example.my_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PopUpWindow extends Activity {

    String addword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popupwindow);
        final EditText wordbox= findViewById(R.id.wordbox);
        Button submitbutton=findViewById(R.id.submitbutton);

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        //sets popupwindow size
        getWindow().setLayout((int)(width*.6),(int)(height*.5));

        submitbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                addword=wordbox.getText().toString();
                Toast.makeText(PopUpWindow.this,addword, Toast.LENGTH_SHORT).show();
                String message=addword;
                Intent intent=new Intent();
                intent.putExtra("MESSAGE",message);
                setResult(5,intent);

                finish();//finishing activity
            }

        });



    }
}

