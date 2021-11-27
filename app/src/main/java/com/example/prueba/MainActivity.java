package com.example.prueba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText nombreUsuario;
    Button ingresarBoton;

    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreUsuario = findViewById(R.id.nombreUusario_editText);
        ingresarBoton = findViewById(R.id.ingresarUusario_button);

        ingresarBoton.setOnClickListener(this);

        db = FirebaseDatabase.getInstance();


    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ingresarUusario_button:

                String username = nombreUsuario.getText().toString();

                db.getReference().child("semana14").child("usuarios").orderByChild("name").equalTo(username).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot data) {
                                Boolean existe = false;
                                for (DataSnapshot child : data.getChildren()) {
                                    Usuario user = child.getValue(Usuario.class);
                                    existe = true;
                                    Intent i = new Intent(nombreUsuario.getContext(),agregarTarea.class);
                                    i.putExtra("idMagiColor",user.getId());
                                    nombreUsuario.setText("");


                                   startActivity(i);
                                }
                                if (existe == false) {
                                    if (username.isEmpty() || username.contains(" ")) {
                                        Toast.makeText(nombreUsuario.getContext(), "No ha digitado un usuario, o bien, tiene espacios", Toast.LENGTH_LONG).show();
                                    } else {
                                        String key = db.getReference().child("usuarios").push().getKey();

                                        Usuario user = new Usuario(key, username);

                                        db.getReference().child("semana14").child("usuarios").child(key).setValue(user);

                                        Log.e("---", "" + user.getId());


                                        Intent i = new Intent(nombreUsuario.getContext(),agregarTarea.class);
                                        i.putExtra("idMagiColor",user.getId());
                                        nombreUsuario.setText("");

                                       startActivity(i);
                                    }
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );

                break;
        }
    }
}