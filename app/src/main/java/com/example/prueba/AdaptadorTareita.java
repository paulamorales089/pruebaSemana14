package com.example.prueba;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AdaptadorTareita extends BaseAdapter {

    private ArrayList<TareitaPaSubir> tareas;
    private Boolean pressed = false;

    public AdaptadorTareita() {
        tareas = new ArrayList<>();
    }

    public void clear() {
        tareas.clear();
        notifyDataSetChanged();
    }

    public void addTareas(TareitaPaSubir tarea) {
        tareas.add(tarea);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return tareas.size();
    }

    @Override
    public Object getItem(int i) {
        return tareas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View renglon, ViewGroup lista) {
        LayoutInflater inflater = LayoutInflater.from(lista.getContext());
        View renglonView = inflater.inflate(R.layout.tareita, null);

        TareitaPaSubir tarea = tareas.get(i);
        Button eliminarTarea = renglonView.findViewById(R.id.eliminarBoton);
        TextView tituloTarea = renglonView.findViewById(R.id.tareaTitulo);
        TextView descripcionTarea = renglonView.findViewById(R.id.descripcionTareita);
        TextView fechaTarea = renglonView.findViewById(R.id.fechaTarea);
        Button completadaTarea = renglonView.findViewById(R.id.completadoBoton);

        tituloTarea.setText(tarea.getNombre());
        descripcionTarea.setText(tarea.getDescripcion());
        fechaTarea.setText(tarea.getFecha());

        if (tarea.getStatus()=="pending"){
           completadaTarea.setBackgroundColor(Color.parseColor("#b48484"));
           completadaTarea.setText("tarea no completada, vas a perder");
        } else {
            completadaTarea.setBackgroundColor(Color.parseColor("#57e66e"));
            completadaTarea.setText("tarea completada, NO vas a perder :D");

        }

        eliminarTarea.setOnClickListener((v) -> {
            String id = tarea.getId();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("semana14").child("tareas").child(tarea.getUsuario()).child(id);
            reference.setValue(null);
        });

        completadaTarea.setOnClickListener((v) -> {

            String id = tarea.getId();



            if (tarea.getStatus() == "pending") {
                String stat = "completed";

                TareitaPaSubir toDo = new TareitaPaSubir(id, tarea.getDescripcion(), tarea.getNombre(), stat, tarea.getFecha(), tarea.getUsuario());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("semana14").child("tareas").child(tarea.getUsuario()).child(id);
                reference.setValue(toDo);



            } else {
                String stat = "pending";

                TareitaPaSubir toDo = new TareitaPaSubir(id, tarea.getDescripcion(), tarea.getNombre(), stat, tarea.getFecha(), tarea.getUsuario());
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("semana14").child("tareas").child(tarea.getUsuario()).child(id);
                reference.setValue(toDo);


            }
        });





        return renglonView;
    }

}
