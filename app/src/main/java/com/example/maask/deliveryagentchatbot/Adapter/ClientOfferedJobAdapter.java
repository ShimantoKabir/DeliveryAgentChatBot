package com.example.maask.deliveryagentchatbot.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.maask.deliveryagentchatbot.ClientOfferedJobActivity;
import com.example.maask.deliveryagentchatbot.PojoClass.AppliedDeliveryManInfo;
import com.example.maask.deliveryagentchatbot.PojoClass.ClientOfferedJob;
import com.example.maask.deliveryagentchatbot.PojoClass.ManageJob;
import com.example.maask.deliveryagentchatbot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Maask on 8/12/2018.
 */

public class ClientOfferedJobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // database credential ...



    private OnLocationIconClickListener locationIconClickListener;
    private OnApplyClickListener applyClickListener;

    // location interface
    public interface OnLocationIconClickListener{
        void onLocationClick(String location);
    }

    public void setOnLocationClickListener(OnLocationIconClickListener onLocationIconClickListener){
        locationIconClickListener = onLocationIconClickListener;
    }

    // apply interface
    public interface OnApplyClickListener{
        void onApplyClick(ManageJob manageJob);
    }

    public void setOnApplyClickListener(OnApplyClickListener onApplyClickListener){
        applyClickListener = onApplyClickListener;
    }

    private ArrayList<ClientOfferedJob> clientOfferedJobs;
    private Context context;

    public ClientOfferedJobAdapter(ArrayList<ClientOfferedJob> clientOfferedJobs, Context context) {
        this.clientOfferedJobs = clientOfferedJobs;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder = null;
        View v = inflater.inflate(R.layout.client_offered_job,parent,false);
        viewHolder = new ClientOfferedJobViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ClientOfferedJobViewHolder clientOfferedJobViewHolder = (ClientOfferedJobViewHolder) holder;

        clientOfferedJobViewHolder.deliveryStatus.setText("Delivered : "+clientOfferedJobs.get(position).getDelivered());
        clientOfferedJobViewHolder.productDescription.setText("Description : "+clientOfferedJobs.get(position).getProductDescription());
        clientOfferedJobViewHolder.productAttribute.setText("Attribute : "+clientOfferedJobs.get(position).getProductAttributes());
        clientOfferedJobViewHolder.productType.setText("Type : "+clientOfferedJobs.get(position).getProductType());
        clientOfferedJobViewHolder.productWeight.setText("Weight : "+String.valueOf(clientOfferedJobs.get(position).getUnitWeight()));
        clientOfferedJobViewHolder.publishDate.setText("Publish Date : "+clientOfferedJobs.get(position).getPublishData());

        if (context instanceof ClientOfferedJobActivity){
            clientOfferedJobViewHolder.applyJobBT.setVisibility(View.GONE);
            clientOfferedJobViewHolder.editDeleteJobLL.setVisibility(View.VISIBLE);
        }else {

            FirebaseAuth auth = FirebaseAuth.getInstance();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            final FirebaseUser currentUser = auth.getCurrentUser();

            databaseReference.child("clientOfferedJob").child(clientOfferedJobs.get(position).getParentKey()).child("AppliedDeliveryManInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot admSnapShot : dataSnapshot.getChildren()) {

                        AppliedDeliveryManInfo appliedDeliveryManInfo = admSnapShot.getValue(AppliedDeliveryManInfo.class);
                        if (appliedDeliveryManInfo.getDeliveryManId().equals(currentUser.getUid())){
                            Log.e("appDeliveryManInfo: ",appliedDeliveryManInfo.getDeliveryManId());
                            clientOfferedJobViewHolder.applyJobBT.setText("Decline");
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            clientOfferedJobViewHolder.applyJobBT.setVisibility(View.VISIBLE);
            clientOfferedJobViewHolder.editDeleteJobLL.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return clientOfferedJobs.size();
    }

    private class ClientOfferedJobViewHolder extends RecyclerView.ViewHolder {

        TextView productDescription,productAttribute,productWeight,deliveryStatus,productType,publishDate;
        ImageView editJobIV,deleteJobIV,startEndPosIV;
        Button applyJobBT;

        LinearLayout editDeleteJobLL;

        public ClientOfferedJobViewHolder(View v) {
            super(v);

            productDescription = v.findViewById(R.id.product_description);
            productAttribute = v.findViewById(R.id.product_attribute);
            productType = v.findViewById(R.id.product_type);
            productWeight = v.findViewById(R.id.product_weight);
            deliveryStatus = v.findViewById(R.id.delivery_status);
            publishDate = v.findViewById(R.id.publish_date);

            editJobIV = v.findViewById(R.id.edit_job_iv);
            deleteJobIV = v.findViewById(R.id.delete_job_iv);
            startEndPosIV = v.findViewById(R.id.start_end_pos_iv);
            editDeleteJobLL = v.findViewById(R.id.edit_delete_job_ll);
            applyJobBT = v.findViewById(R.id.apply_job_bt);

            startEndPosIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (locationIconClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            locationIconClickListener.onLocationClick(clientOfferedJobs.get(position).getStartAndEndLatLon());
                        }
                    }
                }
            });

            applyJobBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (applyClickListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){

                            ManageJob manageJob  = new ManageJob(clientOfferedJobs.get(position).getParentKey(),applyJobBT.getText().toString());
                            applyClickListener.onApplyClick(manageJob);

                        }
                    }
                }
            });

        }
    }
}
