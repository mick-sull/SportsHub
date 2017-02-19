package com.cit.michael.sportshub.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.activities.Activity_Profile;
import com.squareup.picasso.Picasso;

/**
 * Created by micha on 14/02/2017.
 */

public class ProfileViewFragment extends DialogFragment {
    private ImageView profileImage;
    private ImageButton mMessage, mAdd, viewProfile;
    private String imageUrl, uID;



    public ProfileViewFragment(String imageUrl, String uID ){
        this.imageUrl = imageUrl;
        this.uID = uID;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.profile_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        profileImage = (ImageView)dialog.findViewById(R.id.imgDialogUserProfile);
        Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.img_circle_placeholder).resize(300,300).transform(new CircleTransform()).into(profileImage);


        dialog.findViewById(R.id.imgBtnMessage).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Toast.makeText(getContext(), "Send Message to", Toast.LENGTH_SHORT).show();
            }

        });

        dialog.findViewById(R.id.imgBtnViewProfile).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Toast.makeText(getContext(), "Opening Profile", Toast.LENGTH_SHORT).show();
                Log.d("TEST123", "View Profile");
                Intent intent = new Intent(getContext(), Activity_Profile.class);
                intent.putExtra("user_id", uID);
                startActivity(intent);

            }

        });

        dialog.findViewById(R.id.imgBtnAddUser).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Toast.makeText(getContext(), "Send Message to", Toast.LENGTH_SHORT).show();
            }

        });
        
        return dialog;

    }

}



