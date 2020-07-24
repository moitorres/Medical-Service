package csf.itesm.serviciomedico;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<ObjetoPaciente> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<ObjetoPaciente> data) {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.informacion_pacientes, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ObjetoPaciente pacienteActual = mData.get(position);

        //Matricula y nombre del paciente
        String paciente = pacienteActual.getMatricula()+ " - ";
        paciente += pacienteActual.getNombre()+ " " + pacienteActual.getAppaterno() + " " + pacienteActual.getApmaterno();
        holder.TextViewPaciente.setText(paciente);

        //Padecimientos del paciente
        String padecimientos = "Padecimientos: " + pacienteActual.getPadecimientos();
        holder.TextViewPadecimientos.setText(padecimientos);

        //Medicamentos del paciente
        String medicamentos = "Medicamentos que consume: " + pacienteActual.getMedicamentos();
        holder.TextViewMedicamentos.setText(medicamentos);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView TextViewPaciente;
        TextView TextViewPadecimientos;
        TextView TextViewMedicamentos;

        ObjetoPaciente paciente;

        public ViewHolder(View itemView) {

            super(itemView);

            TextViewPaciente = itemView.findViewById(R.id.txt_paciente);
            TextViewPadecimientos = itemView.findViewById(R.id.txt_padecimientos);
            TextViewMedicamentos = itemView.findViewById(R.id.txt_medicamentos);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    ObjetoPaciente getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}