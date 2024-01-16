package com.example.uf2_pt2_m08_maurocitt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uf2_pt2_m08_maurocitt.model.Coche;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ValueEventListener, ChildEventListener, View.OnClickListener {

    private CochesAdapter cochesAdapter;
    private List<Coche> listaCoches;
    private RecyclerView recyclerViewCoches;
    private Button btnAdd;
    private Button btnConsulta;
    private Button btnModificar;
    private EditText nombre;
    private EditText apellido;
    private EditText telefono;
    private EditText marca;
    private EditText modelo;
    private EditText matricula;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference dbCoche = FirebaseDatabase.getInstance().getReference().child("coches");

        dbCoche.addChildEventListener(this);
        dbCoche.addValueEventListener(this);

        listaCoches = new ArrayList<Coche>();

        cochesAdapter = new CochesAdapter(this, dbCoche, listaCoches);
        recyclerViewCoches = findViewById(R.id.recyclerView);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnConsulta = findViewById(R.id.btnConsulta);
        btnConsulta.setOnClickListener(this);

        btnModificar = findViewById(R.id.btnModificar);
        btnModificar.setOnClickListener(this);

        nombre = findViewById(R.id.nom);
        apellido = findViewById(R.id.lastNom);
        telefono = findViewById(R.id.telefono);
        marca = findViewById(R.id.marca);
        modelo = findViewById(R.id.modelo);
        matricula = findViewById(R.id.matricula);

        // també es pot posar al xml les 3 línies següents
        recyclerViewCoches.setHasFixedSize(true);
        recyclerViewCoches.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCoches.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerViewCoches.setAdapter(cochesAdapter);
    }

    // Mètode a implentar de la interfície "ValueEventListener"
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Toast.makeText(MainActivity.this, "Han canviat dades " + dataSnapshot.getKey() + ": " + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, "Hi ha " + dataSnapshot.getChildrenCount() + " dates a la llista", Toast.LENGTH_SHORT).show();

        // Eliminem tot el contingut per no afegir cada cop que hi ha un canvi
        listaCoches.removeAll(listaCoches);
        // Recorrem tots els elements del DataSnapshot i els mostrem
        for (DataSnapshot element : dataSnapshot.getChildren()) {
            Coche coche = new Coche(
                    element.child("nombre").getValue().toString(),
                    element.child("apellido").getValue().toString(),
                    element.child("telefono").getValue().toString(),
                    element.child("marca").getValue().toString(),
                    element.child("modelo").getValue().toString(),
                    element.child("matricula").getValue().toString()
            );
            listaCoches.add(coche);
        }

        // Per si hi ha canvis, que es refresqui l'adaptador
        cochesAdapter.notifyDataSetChanged();
        recyclerViewCoches.scrollToPosition(listaCoches.size() - 1);
    }

    // Mètodes a implentar de la interfície "ChildEventListener"
    // Aquest mètode es crida cada cop que s'afegeix un element nou i també el primer cop (que entra n vegades, en funció del nombre d'elements)
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//        Toast.makeText(MainActivity.this, "Has afegit " + dataSnapshot.getKey() + ": " + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//        Toast.makeText(MainActivity.this, "Has canviat " + dataSnapshot.getKey() + ": " + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
//        Toast.makeText(MainActivity.this, "Has eliminat " + dataSnapshot.getKey() + ": " + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//        Toast.makeText(MainActivity.this, "Has mogut " + dataSnapshot.getKey() + ": " + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
//        Log.e(TAGLOG, "Error!", databaseError.toException());
    }

    @Override
    public void onClick(View v) {
        DatabaseReference dbCoche = null;
        Coche coche;
        switch (v.getId()) {
            case R.id.btnAdd:
                dbCoche = FirebaseDatabase.getInstance().getReference().child("coches");
                Query query = dbCoche.orderByChild("matricula").equalTo(matricula.getText().toString().toUpperCase());
                DatabaseReference finalDbCoche = dbCoche;
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() || matricula.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this, "Matrícula inválida", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!dataSnapshot.exists() && !nombre.getText().toString().equals("") && !apellido.getText().toString().equals("") && !telefono.getText().toString().equals("") && !marca.getText().toString().equals("") && !modelo.getText().toString().equals("")) {
                                Coche coche = agregarCoche();
                                finalDbCoche.child(coche.getMatricula()).setValue(coche);
                            } else {
                                Toast.makeText(MainActivity.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle potential errors
                        Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.btnConsulta:
                dbCoche = FirebaseDatabase.getInstance().getReference().child("coches");
                Query query2 = dbCoche.orderByChild("matricula").equalTo(matricula.getText().toString());
                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Coche coche = snapshot.getValue(Coche.class);
                                nombre.setText(coche.getNombre());
                                apellido.setText(coche.getApellido());
                                telefono.setText(coche.getTelefono());
                                marca.setText(coche.getMarca());
                                modelo.setText(coche.getModelo());
                                matricula.setText(coche.getMatricula());
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "No hay coche con la matricula deseada", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle potential errors
                        Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            case R.id.btnModificar:
                dbCoche = FirebaseDatabase.getInstance().getReference().child("coches");
                Query query3 = dbCoche.orderByChild("matricula").equalTo(matricula.getText().toString());
                DatabaseReference finalDbCoche1 = dbCoche;
                query3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            try {
                                Coche coche = agregarCoche();
                                finalDbCoche1.child(coche.getMatricula()).setValue(coche);
                            } catch (Exception NullPointerException) {
                                Toast.makeText(MainActivity.this, "Introduce un telefono valido", Toast.LENGTH_SHORT).show();
                            }

                            Toast.makeText(MainActivity.this, "Coche modificado", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this, "No hay coche con la matricula deseada", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle potential errors
                        Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;

        }
    }
    public Coche agregarCoche() {
        Coche coche = new Coche();
        if (matricula.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Introduce una matricula", Toast.LENGTH_SHORT);
            toast.show();
        } else if (nombre.getText().toString().equals("") || apellido.getText().toString().equals("") || telefono.getText().toString().equals("") || marca.getText().toString().equals("") || modelo.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Introduce todos los datos", Toast.LENGTH_SHORT);
            toast.show();
        } else if (telefono.getText().toString().length() == 9) {
            try {
                int parsedTelefono = Integer.parseInt(telefono.getText().toString());
                coche = new Coche(
                        nombre.getText().toString(),
                        apellido.getText().toString(),
                        telefono.getText().toString(),
                        marca.getText().toString(),
                        modelo.getText().toString(),
                        matricula.getText().toString().toUpperCase()
                );
                Toast toast = Toast.makeText(getApplicationContext(), "Coche modificado", Toast.LENGTH_SHORT);
                toast.show();

            } catch (Exception e){
                Toast toast = Toast.makeText(getApplicationContext(), "Introduce un telefono valido", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        return coche;
    }

    public Coche modificarCoche(){
        Coche coche = new Coche();
        if (matricula.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Introduce una matricula", Toast.LENGTH_SHORT);
            toast.show();
        } else if (nombre.getText().toString().equals("") || apellido.getText().toString().equals("") || telefono.getText().toString().equals("") || marca.getText().toString().equals("") || modelo.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Introduce todos los datos", Toast.LENGTH_SHORT);
            toast.show();
        } else if (telefono.getText().toString().length() == 9) {
            try {
                int parsedTelefono = Integer.parseInt(telefono.getText().toString());
                coche = new Coche(
                        nombre.getText().toString(),
                        apellido.getText().toString(),
                        telefono.getText().toString(),
                        marca.getText().toString(),
                        modelo.getText().toString(),
                        matricula.getText().toString().toUpperCase()
                );
                Toast toast = Toast.makeText(getApplicationContext(), "Coche modificado", Toast.LENGTH_SHORT);
                toast.show();

            } catch (Exception e){
                Toast toast = Toast.makeText(getApplicationContext(), "Introduce un telefono valido", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        return coche;
    }
}