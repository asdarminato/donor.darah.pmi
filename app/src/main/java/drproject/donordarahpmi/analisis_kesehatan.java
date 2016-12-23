package drproject.donordarahpmi;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Date;
import java.util.Calendar;

public class analisis_kesehatan extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rekam_user, user_medis;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    Button btnCheck;
    RadioGroup RGBerat, RGTekanan, RGKadar, RGTidur;
    RadioButton RBerat, RTekanan, RKadar, RTidur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analisis_kesehatan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Analisis Kesehatan");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        final Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTimeZone());

        final String idSession = getIntent().getStringExtra("EXTRA_SESSION_ID");

        RGBerat = (RadioGroup)findViewById(R.id.RadioBerat);
        RGTekanan = (RadioGroup)findViewById(R.id.RadioTekanan);
        RGKadar = (RadioGroup)findViewById(R.id.RadioKadar);
        RGTidur = (RadioGroup)findViewById(R.id.RadioTidur);

        btnCheck = (Button)findViewById(R.id.buttonCheck);

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

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int SelectedIdBerat = RGBerat.getCheckedRadioButtonId();
                int SelectedIdTekanan = RGTekanan.getCheckedRadioButtonId();
                int SelectedIdKadar = RGKadar.getCheckedRadioButtonId();
                int SelectedIdTidur = RGTidur.getCheckedRadioButtonId();

                RBerat = (RadioButton)findViewById(SelectedIdBerat);
                RTekanan = (RadioButton)findViewById(SelectedIdTekanan);
                RKadar = (RadioButton)findViewById(SelectedIdKadar);
                RTidur = (RadioButton)findViewById(SelectedIdTidur);

                String status = "NULL";
                if(RBerat.getText().equals("Ya") && RTekanan.getText().equals("Ya") &&
                        RKadar.getText().equals("Ya") && RTidur.getText().equals("Tidak")){
                    Intent i = new Intent(analisis_kesehatan.this, analisis_kesehatan2.class);
                    i.putExtra("EXTRA_SESSION_ID",idSession);
                    startActivity(i);

                }else {
                    status = "Ditunda";
                    if(idSession != null){

                        String tgl_hari_ini = c.getTime().toString();
                        rekam_user = database.getReference("rekam_medis").child(idSession).child(tgl_hari_ini).child("status");
                        rekam_user.setValue(status);
                    }else{
                        System.out.println("kosong");
                    }
                    Intent i = new Intent(analisis_kesehatan.this, MainActivity.class);
                    i.putExtra("EXTRA_SESSION_ID",idSession);
                    startActivity(i);

                }



            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
