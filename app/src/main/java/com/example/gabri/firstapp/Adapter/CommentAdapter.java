package com.example.gabri.firstapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabri.firstapp.Model.Comment;
import com.example.gabri.firstapp.R;

import java.util.List;

/**
 * Created by Gabri on 13/12/17.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.comment_row, null);
        TextView nome = (TextView)convertView.findViewById(R.id.username);
        TextView comment = (TextView)convertView.findViewById(R.id.comment);
        ImageView starOne=(ImageView) convertView.findViewById(R.id.star_one);
        ImageView starTwo=(ImageView) convertView.findViewById(R.id.star_two);
        ImageView starThree=(ImageView) convertView.findViewById(R.id.star_three);
        ImageView starFour=(ImageView) convertView.findViewById(R.id.star_four);
        ImageView starFive=(ImageView) convertView.findViewById(R.id.star_five);

        Comment c = getItem(position);
        nome.setText(c.getUsername());
        comment.setText(c.getReview());

        int rate=c.getRate();

        switch (rate){
            case 1: starOne.setImageResource(R.drawable.staron);
                break;
            case 2: starOne.setImageResource(R.drawable.staron);
                    starTwo.setImageResource(R.drawable.staron);
                break;
            case 3: starOne.setImageResource(R.drawable.staron);
                    starTwo.setImageResource(R.drawable.staron);
                    starThree.setImageResource(R.drawable.staron);
                break;
            case 4: starOne.setImageResource(R.drawable.staron);
                    starTwo.setImageResource(R.drawable.staron);
                    starThree.setImageResource(R.drawable.staron);
                    starFour.setImageResource(R.drawable.staron);
                break;
            case 5: starOne.setImageResource(R.drawable.staron);
                starTwo.setImageResource(R.drawable.staron);
                starThree.setImageResource(R.drawable.staron);
                starFour.setImageResource(R.drawable.staron);
                starFive.setImageResource(R.drawable.staron);
                break;
        }

        return convertView;
    }
}
