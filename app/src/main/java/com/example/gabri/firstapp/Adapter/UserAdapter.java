
package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gabri.firstapp.Boxart;
import com.example.gabri.firstapp.DBQuery;
import com.example.gabri.firstapp.FragmentGameDetail;

import com.example.gabri.firstapp.FragmentNewsDetail;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.Game;

import com.example.gabri.firstapp.Model.User;
import com.example.gabri.firstapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.raizlabs.android.dbflow.config.FlowManager.getContext;

/**
 * Created by Gabri
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImage;
        private TextView description;
        private TextView userName;
        private View view;


        public ViewHolder(View view) {
            super(view);
            userImage = (ImageView) view.findViewById(R.id.user_image_2);
            userName = (TextView) view.findViewById(R.id.user_name_2);
            userName = (TextView) view.findViewById(R.id.user_description_2);
            this.view=view;

        }

    }


    public UserAdapter(List<User> userList) {
        this.userList=userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = userList.get(position);
        if (user != null) {
            holder.userName.setText(user.getUsername());
            holder.userName.setText(user.getDescription());

            //Glide.with(getContext()).load("http://thegamesdb.net/banners/" + boxart.getThumb()).into(holder.coverImage);

           /* holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean TWOPANELS = Data.getData().getHomePageActivity().getResources().getBoolean(R.bool.has_two_panes);
                    Bundle bundle = new Bundle();
                    bundle.putString("IDFIREBASE", game.getIdForFirebase());
                    bundle.putSerializable("REALGAMEOBJECT", game);


                    FragmentGameDetail fragmentGameDetail = new FragmentGameDetail();
                    fragmentGameDetail.setArguments(bundle);
                    //FINAL SOLUTION
                    Fragment fragmentById = Data.getData().getHomePageActivity().getSupportFragmentManager().findFragmentById(R.id.mainframeLayout);
                    FragmentTransaction transaction = Data.getData().getHomePageActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainframeLayout, fragmentGameDetail, "GameDetail");
                    transaction.addToBackStack("TABLAYOUT");
                    transaction.commit();

                }
            });*/

        }
    }



    @Override
    public int getItemCount() {
        return userList.size();
    }


}
