package com.rosery.app;
import android.content.Intent;import android.os.Bundle;import android.widget.EditText;import android.widget.Toast;import androidx.appcompat.app.AppCompatActivity;
public class LoginActivity extends AppCompatActivity{
 protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_login);findViewById(R.id.loginBackButton).setOnClickListener(v->finish());EditText email=findViewById(R.id.emailInput),pass=findViewById(R.id.passwordInput);
 findViewById(R.id.loginSubmit).setOnClickListener(v->{String e=email.getText().toString().trim(),p=pass.getText().toString();if(e.isEmpty()||p.isEmpty()){Toast.makeText(this,"Please enter email and password.",Toast.LENGTH_SHORT).show();return;}String[] u=UserManager.authenticate(this,e,p);if(u==null){Toast.makeText(this,"Invalid email/password or inactive account.",Toast.LENGTH_SHORT).show();return;}new Session(this).login(u[0],u[1],u[2]);if("admin".equals(u[2]))startActivity(new Intent(this,AdminDashboardActivity.class));else startActivity(new Intent(this,MainActivity.class));finish();});
 findViewById(R.id.registerLink).setOnClickListener(v->startActivity(new Intent(this,RegisterActivity.class)));}
}
