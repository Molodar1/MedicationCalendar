package pl.chmielewski.medicationcalendar;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterShow extends FirebaseRecyclerAdapter<Medicament, AdapterShow.myviewholder>
{
DatabaseReference databaseReference;
    public AdapterShow(@NonNull FirebaseRecyclerOptions<Medicament> options) {

        super(options);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Medicament")
                    .child(userId);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position, @NonNull Medicament medicament)
    {
        holder.name.setText(medicament.getMedicamentName());
        holder.dose.setText(medicament.getMedicamentDose());
        holder.additionalInfo.setText(medicament.getMedicamentAdditionalInfo());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(view.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialog_content))
                        .setExpanded(true,1100)
                        .create();

                View myview=dialogPlus.getHolderView();
                final EditText edtMedicamentName=myview.findViewById(R.id.edtMedicamentName);
                final EditText edtMedicamentDose=myview.findViewById(R.id.edtMedicamentDose);
                final EditText edtAdditionalInfo=myview.findViewById(R.id.edtMedicamentAdditionalInfo);
                Button submit=myview.findViewById(R.id.btnSubmitMedicamentToFirebase);

                edtMedicamentName.setText(medicament.getMedicamentName());
                edtMedicamentDose.setText(medicament.getMedicamentDose());
                edtAdditionalInfo.setText(medicament.getMedicamentAdditionalInfo());
                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("medicamentName",edtMedicamentName.getText().toString());
                        map.put("medicamentDose",edtMedicamentDose.getText().toString());
                        map.put("medicamentAdditionalInfo",edtAdditionalInfo.getText().toString());

                        databaseReference
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });


            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });
        holder.setNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medicineKey = getRef(position).getKey();
                Medicament medicineInfo = getItem(position);

                Intent intent = new Intent(view.getContext(), SetNotificationActivity.class);
//                intent.putExtra("medicineKey", medicineKey);
//              //  intent.putExtra("medicament", medicament);
//                view.getContext().startActivity(intent);
            }
        });
    } // End of OnBindViewMethod

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    static class myviewholder extends RecyclerView.ViewHolder
    { CircleImageView img;
        ImageView edit,delete, setNotification;
        TextView name,dose, additionalInfo;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
          //  img=(CircleImageView) itemView.findViewById(R.id.img1);
            name=(TextView)itemView.findViewById(R.id.txtvMedicamentName);
            dose =(TextView)itemView.findViewById(R.id.txtvMedicamentDose);
           additionalInfo =(TextView)itemView.findViewById(R.id.txtvAdditionalInfo);
            edit=(ImageView)itemView.findViewById(R.id.btnEdit);
            delete=(ImageView)itemView.findViewById(R.id.btnDelete);
            setNotification =(ImageView)itemView.findViewById(R.id.btnBuy);
        }
    }
}
