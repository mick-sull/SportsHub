/*
package com.cit.michael.sportshub.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.ui.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

import static com.facebook.FacebookSdk.getApplicationContext;


*/
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag_Profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag_Profile#newInstance} factory method to
 * create an instance of this fragment.
 *//*

public class Frag_Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseAuth auth;
    ImageView profilePictureView;
    TextView username;
    Button btnLogout;
    @BindView(R.id.btnRecentEvents) Button btnRecentEvents;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Frag_Profile() {
        // Required empty public constructor

    }

    */
/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frag_Profile.
     *//*

    // TODO: Rename and change types and number of parameters
    public static Frag_Profile newInstance(String param1, String param2) {
        Frag_Profile fragment = new Frag_Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        username = (TextView) v.findViewById(R.id.userName);
        Log.d("USER", auth.getInstance().getCurrentUser().getDisplayName() + "");
        Log.d("USER", auth.getInstance().getCurrentUser().getDisplayName() + "");

        username.setText(auth.getInstance().getCurrentUser().getDisplayName() + "");

        profilePictureView = (ImageView) v.findViewById(R.id.profilePictureView);
        profilePictureView.setImageURI(auth.getCurrentUser().getPhotoUrl());


        for (UserInfo user : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {
            if (user.getProviderId().equals("facebook.com")) {
                //System.out.println("User is signed in with Facebook");
                Log.d("USER Provider", user.getProviderId());
                String uri = auth.getCurrentUser().getPhotoUrl().toString();
                Picasso.with(getApplicationContext())
                        .load(uri)
                        .placeholder(R.drawable.img_circle_placeholder)
                        .resize(400, 400)
*/
/*                .placeholder(R.drawable.img_circle_placeholder)

                .centerCrop()*//*

                        .transform(new CircleTransform())
                        .into(profilePictureView);
            }
        }
        return v;

    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    */
/**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
*/
