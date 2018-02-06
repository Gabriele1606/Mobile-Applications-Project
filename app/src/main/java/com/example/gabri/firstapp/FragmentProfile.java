package com.example.gabri.firstapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.gabri.firstapp.Adapter.ReadLaterAdapter;
import com.example.gabri.firstapp.Controller.HomePage;
import com.example.gabri.firstapp.Model.Data;
import com.example.gabri.firstapp.Model.RSSFeed;
import com.example.gabri.firstapp.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by simon on 03/02/2018.
 */

public class FragmentProfile extends android.support.v4.app.Fragment {

    private View view;
    private List<RSSFeed> newsList = new ArrayList<RSSFeed>();
    private Context mContext;
    private RecyclerView recyclerView;
    private ReadLaterAdapter mAdapter;
    private final int PICK_IMAGE_REQUEST = 71;
    private User user;
    private boolean anotherUser = false;
    private Context context;
    ImageView editProfile;
    EditText editName;
    EditText editDescription;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profile_layout, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_readlater);
        mAdapter = new ReadLaterAdapter(newsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        this.mContext = container.getContext();
        editProfile = (ImageView) view.findViewById(R.id.edit_profile);
        editName = (EditText) view.findViewById(R.id.user_name);
        editDescription = view.findViewById(R.id.description_user);
        return view;
    }

    private void fillUserData(String id) {

        final TextView numberWish = (TextView) view.findViewById(R.id.number_wishlist_profile);
        final TextView numberNews = (TextView) view.findViewById(R.id.number_news_profile);
        final EditText textUsername = (EditText) view.findViewById(R.id.user_name);
        final EditText textDecription = (EditText) view.findViewById(R.id.description_user);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child(id).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = (String) dataSnapshot.getValue();
                textUsername.setText(username);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("users").child(id).child("description").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String description = (String) dataSnapshot.getValue();
                textDecription.setText(description);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("news").child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        newsList.clear();
                        for (DataSnapshot idSnapshot : dataSnapshot.getChildren()) {
                            RSSFeed tmp = new RSSFeed();
                            tmp.setDescription((String) idSnapshot.child("description").getValue());
                            tmp.setGuid((String) idSnapshot.child("guid").getValue());
                            tmp.setImageLink((String) idSnapshot.child("imageLink").getValue());
                            tmp.setPubdate((String) idSnapshot.child("pubdate").getValue());
                            tmp.setTitle((String) idSnapshot.child("title").getValue());
                            tmp.setIdForFirebase((String) idSnapshot.child("idForFirebase").getValue());

                            newsList.add(tmp);

                        }

                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        databaseReference.child("news").child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            numberNews.setText(Long.toString(dataSnapshot.getChildrenCount()));
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        databaseReference.child("game").child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            numberWish.setText(Long.toString(dataSnapshot.getChildrenCount()));
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    private void enableModify(boolean b) {


        if (!b) {
            editProfile.setVisibility(View.GONE);
            return;
        }
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animFadeOut = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_out);
                Animation animFadeIn = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);
                if (!editName.isEnabled()) {
                    animFadeOut.reset();
                    editProfile.clearAnimation();
                    editProfile.startAnimation(animFadeOut);
                    editName.setEnabled(true);
                    editDescription.setEnabled(true);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            editName.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                            editName.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                        }
                    }, 200);

                    editProfile.setImageResource(R.drawable.checked);
                    animFadeIn.reset();
                    editProfile.clearAnimation();
                    editProfile.startAnimation(animFadeIn);

                } else {
                    animFadeOut.reset();
                    editProfile.clearAnimation();
                    editProfile.startAnimation(animFadeOut);
                    editName.setEnabled(false);
                    editDescription.setEnabled(false);
                    onModified();
                    editProfile.setImageResource(R.drawable.user_edit);
                    animFadeIn.reset();
                    editProfile.clearAnimation();
                    editProfile.startAnimation(animFadeIn);
                }

            }
        });

    }

    @Override
    public void onResume() {
        ImageView profileImage = view.findViewById(R.id.user_profile_photo);

        if (!anotherUser) {
            enableModify(true);
            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chooseImage();
                }
            });
            setSwipe();
            loadImageProfile(Data.getUser().getId());
            fillUserData(Data.getUser().getId());
        } else {
            enableModify(false);
            loadImageProfile(user.getId());
            fillUserData(user.getId());
        }
        if (view.getContext() instanceof HomePage) {
            HomePage activity = (HomePage) view.getContext();
            activity.HighlightSection("Profile");
        }
        super.onResume();
    }


    public void setSwipe() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                RSSFeed tmp = newsList.get(position);
                Toast.makeText(view.getContext(), "News removed from your read more list", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseWishGame = FirebaseDatabase.getInstance().getReference("news");
                databaseWishGame.child(Data.getIdUserForRemoteDb()).child(tmp.getIdForFirebase()).removeValue();
                newsList.remove(position);
                mAdapter.notifyItemRemoved(position);


            }

            public static final float ALPHA_FULL = 1.0f;

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;

                    Paint p = new Paint();
                    Bitmap icon;

                    //color : right side (swiping towards left)
                    p.setARGB(210, 255, 20, 20);

                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);

                    //icon : left side (swiping towards right)
                    icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.garbage);
                    c.drawBitmap(icon,
                            (float) itemView.getRight() - convertDpToPx(16) - icon.getWidth(),
                            (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                            p);


                    // Fade out the view when it is swiped out of the parent
                    final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);

                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private int convertDpToPx(int dp) {
        return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void onModified() {
        User user = Data.getUser();
        DatabaseReference databaseUsers;
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        EditText userEdit = view.findViewById(R.id.user_name);
        Editable text = userEdit.getText();
        user.setUsername(text.toString());

        EditText descriptionEdit = getView().findViewById(R.id.description_user);
        Editable textDescription = descriptionEdit.getText();
        user.setDescription(textDescription.toString());

        databaseUsers.child(user.getId()).setValue(user);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    private static Bitmap addBorder(Bitmap resource, Context context) {
        int w = resource.getWidth();
        int h = resource.getHeight();
        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Bitmap.Config.ARGB_8888);
        Paint p = new Paint();
        p.setAntiAlias(true);
        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        c.drawBitmap(resource, 4, 4, p);
        p.setXfermode(null);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(ContextCompat.getColor(context, R.color.white));
        p.setStrokeWidth(15);
        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);
        return output;
    }

    public void setProfile(User user) {
        this.user = user;
        this.anotherUser = true;
    }

    @Override
    public void onAttach(Context context) {
        this.context=context;
        super.onAttach(context);
    }

    public void loadImageProfile(String id) {
        final ImageView imageProfile = view.findViewById(R.id.user_profile_photo);

        imageProfile.setBackgroundColor(getResources().getColor(R.color.transparent));

        Glide.with(view).asBitmap().load(R.drawable.avatar).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), addBorder(resource, getContext()));
                circularBitmapDrawable.setCircular(true);
                imageProfile.setImageDrawable(circularBitmapDrawable);
            }
        });
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("images/" + id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("URI :" + uri.toString());
                Glide.with(view).asBitmap().load(uri).apply(RequestOptions.circleCropTransform()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), addBorder(resource, getContext()));
                        circularBitmapDrawable.setCircular(true);
                        imageProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });
    }
}