package com.example.uf2_pt2_m08_maurocitt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uf2_pt2_m08_maurocitt.model.Coche;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CochesAdapter extends RecyclerView.Adapter<CochesAdapter.ViewHolder> {
private View mView;
public List<Coche> listaCoche;
private Context context;
private DatabaseReference databaseReference;

public CochesAdapter(Context context, DatabaseReference databaseReference, List<Coche> listaCoche) {
        this.context = context;
        this.databaseReference = databaseReference;
        this.listaCoche = listaCoche;
        }

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView matricula;
    public TextView nombre;


    @SuppressLint("WrongViewCast")
    public ViewHolder(View itemView) {
        super(itemView);
        matricula = itemView.findViewById(R.id.matricula);
        nombre = itemView.findViewById(R.id.nombre);
        itemView.setOnClickListener(this); // Per escoltar els clicks (no oblidar!)
    }

    // Mètode a implementar de la interfície View.OnClickListener
    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        mostraPopupMenu(v, position);
        //Toast.makeText(context, "Has clicat la posició: " + position, Toast.LENGTH_SHORT).show();
    }
}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Coche item = listaCoche.get(position);
        holder.matricula.setText(String.valueOf(item.getMatricula()));
        holder.nombre.setText(String.valueOf(item.getNombre()));
    }

    @Override
    public int getItemCount() {
        return listaCoche.size();
    }

    private void mostraPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(this.context, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new Menu(position));
        popupMenu.show();
    }

public class Menu implements PopupMenu.OnMenuItemClickListener {
    Integer pos;

    public Menu(int pos) {
        this.pos = pos;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Coche coche;
        switch (menuItem.getItemId()) {
            case R.id.borrar:
                databaseReference.child(listaCoche.get(pos).getMatricula()).removeValue();
            default:
        }
        return false;
    }
}
}