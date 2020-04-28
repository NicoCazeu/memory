package com.softcaze.memory.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.softcaze.memory.R;

public class ContactFormActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_form);

        ((Button) findViewById(R.id.btnOK)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                String sub = ((EditText)findViewById(R.id.txtSubject)).getText().toString();
                String mess = ((EditText)findViewById(R.id.txtMessage)).getText().toString();
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"softcaze@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, sub);
                i.putExtra(Intent.EXTRA_TEXT   , mess);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactFormActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
