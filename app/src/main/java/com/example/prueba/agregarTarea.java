package com.example.prueba;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class agregarTarea extends AppCompatActivity implements View.OnClickListener{

    EditText nombreTarea;
    EditText descripcionTraea;
    Button agregarBoton;
    ListView listaTareas;

    FirebaseDatabase db;

    String usuarioDeAntes;

    AdaptadorTareita adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        nombreTarea = findViewById(R.id.nombreTarea_editTex);
        descripcionTraea = findViewById(R.id.descripcionTarea_editText);
        agregarBoton = findViewById(R.id.agregarTarea_button);
        listaTareas = findViewById(R.id.listaTareas_listView);

        db = FirebaseDatabase.getInstance();

        usuarioDeAntes = getIntent().getExtras().getString("idMagiColor");

        adapter = new AdaptadorTareita();
        listaTareas.setAdapter(adapter);

        agregarBoton.setOnClickListener(this);


        cargateBaseMilitar();




    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.agregarTarea_button:
                String descrip = descripcionTraea.getText().toString();
                String nTarea = nombreTarea.getText().toString();

                if (descrip.isEmpty()) {
                    Toast.makeText(this, "la tarea debe tener un nombre", Toast.LENGTH_LONG).show();
                } else if (descrip.isEmpty()) {
                    Toast.makeText(this, "la tarea debe tener una descripcion", Toast.LENGTH_LONG).show();
                } else {
                    DatabaseReference reference = db.getReference().child("semana14").child("tareas").child(usuarioDeAntes);
                    String key = reference.push().getKey();
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    TareitaPaSubir tarea = new TareitaPaSubir(key, descrip, nTarea, "pending",formattedDate, usuarioDeAntes);
                    reference.child(key).setValue(tarea);
                    descripcionTraea.setText("");
                    nombreTarea.setText("");
                }
        }
    }


    public void cargateBaseMilitar (){
        DatabaseReference ref = db.getReference().child("semana14").child("tareas").child(usuarioDeAntes);
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot data) {
                        adapter.clear();
                        for (DataSnapshot child : data.getChildren()){
                            TareitaPaSubir tarea = child.getValue(TareitaPaSubir.class);
                            adapter.addTareas(tarea);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
}