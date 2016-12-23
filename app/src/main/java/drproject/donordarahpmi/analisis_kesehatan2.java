package drproject.donordarahpmi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class analisis_kesehatan2 extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rekam_user, user_medis;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    Button btnCheck2;
    RadioGroup RGUmur, RGPenyakit, RGAlkohol, RGObat;
    RadioButton RUmur, RPenyakit, RAlkohol, RObat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analisis_kesehatan2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Analisis Kesehatan");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        final Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTimeZone());

        final String idSession = getIntent().getStringExtra("EXTRA_SESSION_ID");

        RGUmur = (RadioGroup)findViewById(R.id.RadioUmur);
        RGPenyakit = (RadioGroup)findViewById(R.id.RadioPenyakit);
        RGAlkohol = (RadioGroup)findViewById(R.id.RadioAlkohol);
        RGObat = (RadioGroup)findViewById(R.id.RadioObat);

        btnCheck2 = (Button)findViewById(R.id.buttonCheck2);

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");
                }
            }
        };

        btnCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int SelectedIdUmur = RGUmur.getCheckedRadioButtonId();
                int SelectedIdPenyakit = RGPenyakit.getCheckedRadioButtonId();
                int SelectedIdAlkohol = RGAlkohol.getCheckedRadioButtonId();
                int SelectedIdObat = RGObat.getCheckedRadioButtonId();

                RUmur = (RadioButton)findViewById(SelectedIdUmur);
                RPenyakit = (RadioButton)findViewById(SelectedIdPenyakit);
                RAlkohol = (RadioButton)findViewById(SelectedIdAlkohol);
                RObat = (RadioButton)findViewById(SelectedIdObat);

                String status = "NULL";
                if(RUmur.getText().equals("Ya") && RPenyakit.getText().equals("Tidak") &&
                        RAlkohol.getText().equals("Tidak") && RObat.getText().equals("Tidak")){
                    status = "Diperbolehkan";

                }else {
                    status = "Dilarang";

                }
                if(idSession != null){

                    String tgl_hari_ini = c.getTime().toString();
                    rekam_user = database.getReference("rekam_medis").child(idSession).child(tgl_hari_ini).child("status");
                    rekam_user.setValue(status);
                }else{
                    System.out.println("kosong");
                }

                Intent i = new Intent(analisis_kesehatan2.this, MainActivity.class);
                i.putExtra("EXTRA_SESSION_ID",idSession);
                startActivity(i);



            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}