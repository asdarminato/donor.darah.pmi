package drproject.donordarahpmi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private EditText nrusername;
    private EditText nrpassword;

    private Button nrbregister;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        nrusername = (EditText) findViewById(R.id.rusername);
        nrpassword = (EditText) findViewById(R.id.rpassword);

        nrbregister = (Button) findViewById(R.id.rbregister);



        nrbregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startregister();

            }
        });
    }

    private void startregister() {
        String username = nrusername.getText().toString();
        String password = nrpassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {

            Toast.makeText(Register.this, "Username atau Password Kosong!", Toast.LENGTH_LONG).show();

        } else {


            mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(Register.this, "Proses Tambah Bermasalah!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Register.this, "Registerasi Berhasil :)", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Register.this, MainActivity.class);
                        startActivity(i);
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }
}
