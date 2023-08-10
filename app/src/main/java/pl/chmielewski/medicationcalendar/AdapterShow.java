package pl.chmielewski.medicationcalendar;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterShow extends FirebaseRecyclerAdapter<Medicament, AdapterShow.myviewholder>
{

    public AdapterShow(@NonNull FirebaseRecyclerOptions<Medicament> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position, @NonNull Medicament medicineInfo)
    {
        holder.name.setText(medicineInfo.getMedicamentName());
        holder.kind.setText(medicineInfo.getMedicamentDose());
        holder.cost.setText(medicineInfo.getMedicamentFrequencyTimeMeasure());//String.valueOf(
        holder.inside.setText(medicineInfo.getMedicamentAdditionalInfo());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(view.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialog_content))
                        .setExpanded(true,1100)
                        .create();

                View myview=dialogPlus.getHolderView();
                final EditText name=myview.findViewById(R.id.uimgurl);
                final EditText kind=myview.findViewById(R.id.uname);
                final EditText cost=myview.findViewById(R.id.ucourse);
                final EditText inside=myview.findViewById(R.id.uemail);
                final EditText box =myview.findViewById(R.id.ubox);
                Button submit=myview.findViewById(R.id.btnSubmitMedicamentToFirebase);

                name.setText(medicineInfo.getMedicamentName());
                kind.setText(medicineInfo.getMedicamentFrequencyTimeMeasure());
                cost.setText(medicineInfo.getMedicamentDose());
                inside.setText(medicineInfo.getMedicamentAdditionalInfo());
                box.setText(String.valueOf(medicineInfo.getMedicamentDosingFrequency()));
                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("medicineName",name.getText().toString());
                        map.put("medicineKind",kind.getText().toString());
                        map.put("medicineCost",cost.getText().toString());
                        map.put("medicineInside",inside.getText().toString());
                        map.put("medicineBoxes", Integer.parseInt(box.getText().toString()));

                        FirebaseDatabase.getInstance().getReference().child("MedicineInfo")
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
                        FirebaseDatabase.getInstance().getReference().child("MedicineInfo")
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
        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medicineKey = getRef(position).getKey();
                Medicament medicineInfo = getItem(position);

//                Intent intent = new Intent(view.getContext(), MedicineShop.class);
//                intent.putExtra("medicineKey", medicineKey);
//              //  intent.putExtra("medicineInfo", medicineInfo);
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
        ImageView edit,delete,buy;
        TextView name,kind,cost,inside,availability,box;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
          //  img=(CircleImageView) itemView.findViewById(R.id.img1);
            name=(TextView)itemView.findViewById(R.id.medicineNameShow);
            kind=(TextView)itemView.findViewById(R.id.medicineKindShow);
           cost=(TextView)itemView.findViewById(R.id.medicineCostShow);
            inside=(TextView)itemView.findViewById(R.id.medicineInsideShow);
            //box=(TextView)itemView.findViewById(R.id.medicineBoxesShow);
            availability = itemView.findViewById(R.id.medicineAvailabilityShow); // Inicjalizujemy availability
            edit=(ImageView)itemView.findViewById(R.id.btnEdit);
            delete=(ImageView)itemView.findViewById(R.id.btnDelete);
            buy=(ImageView)itemView.findViewById(R.id.btnBuy);
        }
    }
}
