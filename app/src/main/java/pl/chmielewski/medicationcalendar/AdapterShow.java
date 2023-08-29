package pl.chmielewski.medicationcalendar;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament.ManuallyDeletedMedicament;
import pl.chmielewski.medicationcalendar.data.manuallyDeletedMedicament.ManuallyDeletedMedicamentRepository;
import pl.chmielewski.medicationcalendar.data.medicament.Medicament;
import pl.chmielewski.medicationcalendar.data.medicament.MedicamentRepository;

public class AdapterShow extends RecyclerView.Adapter<AdapterShow.myviewholder> {
    private List<Medicament> medicamentList;
    private MedicamentRepository medicamentRepository;
    private ManuallyDeletedMedicamentRepository manuallyDeletedMedicamentRepository;

    public AdapterShow(Application application) {
        medicamentRepository = new MedicamentRepository(application);
        manuallyDeletedMedicamentRepository = new ManuallyDeletedMedicamentRepository(application);
        medicamentList = new ArrayList<>();
    }

    public void setMedicamentList(List<Medicament> medicaments) {
        medicamentList = medicaments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        Medicament medicament = medicamentList.get(position);
        holder.name.setText(medicament.getMedicamentName());
        holder.dose.setText(medicament.getMedicamentDose());
        holder.numberOfDoses.setText(String.valueOf(medicament.getMedicamentNumberOfDoses()));
        holder.additionalInfo.setText(medicament.getMedicamentAdditionalInfo());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(view.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialog_content))
                        .setExpanded(true, 1100)
                        .create();

                View myview = dialogPlus.getHolderView();
                final EditText edtMedicamentName = myview.findViewById(R.id.edtMedicamentName);
                final EditText edtMedicamentDose = myview.findViewById(R.id.edtMedicamentDose);
                final EditText edtMedicamentNumberOfDoses = myview.findViewById(R.id.edtMedicamentNumberOfDoses);
                final EditText edtAdditionalInfo = myview.findViewById(R.id.edtMedicamentAdditionalInfo);
                Button submit = myview.findViewById(R.id.btnSubmitMedicamentToFirebase);

                edtMedicamentName.setText(medicament.getMedicamentName());
                edtMedicamentDose.setText(medicament.getMedicamentDose());
                edtMedicamentNumberOfDoses.setText(String.valueOf(medicament.getMedicamentNumberOfDoses()));
                edtAdditionalInfo.setText(medicament.getMedicamentAdditionalInfo());
                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Medicament updatedMedicament = new Medicament(
                                edtMedicamentName.getText().toString(),
                                edtMedicamentDose.getText().toString(),
                                edtAdditionalInfo.getText().toString(),
                                Integer.parseInt(edtMedicamentNumberOfDoses.getText().toString())
                        );
                        updatedMedicament.setMedicamentId(medicament.getMedicamentId());
                        medicamentRepository.update(updatedMedicament);

                        dialogPlus.dismiss();
                    }
                });
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Potwierdzenie usunięcia");
                builder.setMessage("Czy na pewno chcesz usunąć ten element?");

                builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ManuallyDeletedMedicament manuallyDeletedMedicament = new ManuallyDeletedMedicament();
                        manuallyDeletedMedicament.setMedicamentId(medicament.getMedicamentId());
                        manuallyDeletedMedicamentRepository.insert(manuallyDeletedMedicament);
                        medicamentRepository.delete(medicament);
                    }
                });

                builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicamentList.size();
    }

    static class myviewholder extends RecyclerView.ViewHolder {
        CircleImageView img;
        ImageView edit, delete, setNotification;
        TextView name, dose, numberOfDoses, additionalInfo;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtvMedicamentName);
            dose = itemView.findViewById(R.id.txtvMedicamentDose);
            numberOfDoses = itemView.findViewById(R.id.txtvMedicamentNumberOfDoses);
            additionalInfo = itemView.findViewById(R.id.txtvAdditionalInfo);
            edit = itemView.findViewById(R.id.btnEdit);
            delete = itemView.findViewById(R.id.btnDelete);
            setNotification = itemView.findViewById(R.id.btnBuy);
        }
    }
}