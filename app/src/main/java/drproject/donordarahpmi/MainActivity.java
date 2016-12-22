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

public class MainActivity extends AppCompatActivity {

    private EditText nusername;
    private EditText npassword;

    private Button nblogin;
    private Button nbregister;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        nusername = (EditText) findViewById(R.id.username);
        npassword = (EditText) findViewById(R.id.password);

        nblogin = (Button) findViewById(R.id.blogin);
        nbregister = (Button) findViewById(R.id.bregister);

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent i = new Intent(MainActivity.this, Index_admin.class);
                    startActivity(i);

                } else {
                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");
                }
            }
        };

        nblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startsignin();

            }
        });

        nbregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().signOut();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void startsignin() {
        String username = nusername.getText().toString();
        String password = npassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {

            Toast.makeText(MainActivity.this, "Username atau Password Kosong!", Toast.LENGTH_LONG).show();

        } else {


            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Login Bermasalah!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}