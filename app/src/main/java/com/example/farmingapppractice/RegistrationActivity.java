package com.example.farmingapppractice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmingapppractice.data.Constant;
import com.example.farmingapppractice.data.ID;
import com.example.farmingapppractice.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {

    //*********************************************
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextCnfPassword;
    private TextView textViewUid;
    private ProgressBar spinner;
    private Button btnRegister;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mAuth,mAuth2;
    String userId;



    //**********************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

         mAuth = FirebaseDatabase.getInstance().getReference().child(Constant.Q2_User);
         mAuth2 = FirebaseDatabase.getInstance().getReference(Constant.IDS).child(Constant.Q2_Id);

        this.bindComponents();
        spinner.setVisibility(View.GONE);
        generateUserId();
        this.addListener();


    }
 //********************************************
 private void bindComponents(){
     this.btnRegister = findViewById(R.id.btn_res_pass);
     this.editTextName = findViewById(R.id.text_full_name);
     this.editTextEmail = findViewById(R.id.text_email);
     this.editTextPhone = findViewById(R.id.text_phone);
     this.editTextPassword = findViewById(R.id.text_password);
     this.editTextCnfPassword = findViewById(R.id.text_cnf_password);
     this.textViewUid = findViewById(R.id.textUid);
     this.spinner = findViewById(R.id.spin_weight_unit);
 }
 //***************************************************
 private void addListener(){
     btnRegister.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             registerUser();
         }
     });
 }
 //*****************************************************
 //Generate UserId


     void generateUserId()
     {
        mAuth2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ID dbId = snapshot.getValue(ID.class);
                userId=dbId.getUserId();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
     }

    private  void generateNextUserId()
    {
        mAuth2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ID dBId = snapshot.getValue(ID.class);
                String uId = dBId.getUserId().substring(1);

                int newUId = Integer.parseInt(uId)+1;

                String newii = Integer.toString(newUId);
                dBId.setUserId("U"+newii);
                String newi = dBId.getUserId();
                Toast.makeText(getApplicationContext(),newi,Toast.LENGTH_SHORT).show();
                mAuth2.setValue(dBId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



//*******************************************************

    public  void registerUser(){
        if(validateUserDetails()){
            spinner.setVisibility(View.VISIBLE);
            firebaseAuth = FirebaseAuth.getInstance();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String name = editTextName.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();


            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                User user = new User(userId,name,email,phone);

                                FirebaseDatabase.getInstance().getReference(Constant.Q2_User)
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful())
                                                {
                                                    spinner.setVisibility(View.GONE);

                                                    generateNextUserId();

                                                   startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));

                                                }

                                            }
                                        });


                            }
                        }
                    });

        }
    }

//*************************************************************

  public  boolean validateUserDetails(){
      String name = editTextName.getText().toString().trim();
      String email = editTextEmail.getText().toString().trim();
      String phone = editTextPhone.getText().toString().trim();
      String password = editTextPassword.getText().toString().trim();
      String cnfPassword = editTextCnfPassword.getText().toString().trim();

      if(name.isEmpty()){
          editTextName.setError("Name Cannot be Empty");
          editTextName.requestFocus();
          return false;
      }
      if(email.isEmpty()){
          editTextEmail.setError("Email Cannot be Empty");
          editTextEmail.requestFocus();
          return false;
      }
      if(phone.isEmpty()){
          editTextPhone.setError("Phone Number Cannot be Empty");
          editTextPhone.requestFocus();
          return false;
      }
      if(password.isEmpty()){
          editTextPassword.setError("Password cannot be Empty");
          editTextPassword.requestFocus();
          return false;
      }
      if(cnfPassword.isEmpty()){
          editTextCnfPassword.setError("Re-Enter Password");
          editTextCnfPassword.requestFocus();
          return false;
      }
      if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
          editTextEmail.setError("Please Provide Valid Email");
          editTextEmail.requestFocus();
          return false;
      }
      if(!Patterns.PHONE.matcher(phone).matches()){
          editTextPhone.setError("Provide Valid Phone Number");
          editTextPhone.requestFocus();
          return false;
      }
      if(password.length() < 8){
          editTextPassword.setError("Password length should be greater than 6");
          editTextPassword.requestFocus();
          return false;
      }
      if(!password.equals(cnfPassword)){
          editTextCnfPassword.setError("Password doesn't matches");
          editTextCnfPassword.requestFocus();
          return false;
      }
      return true;
  }
 //***********************************************************





}