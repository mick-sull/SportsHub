package com.cit.michael.sportshub.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.chat.ui.Activity_Chat;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestUsers;
import com.facebook.FacebookSdk;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Main extends AppCompatActivity implements Fragment_Profile.OnFragmentInteractionListener, Frag_Group.OnFragmentInteractionListener, Fragment_Friends_List.OnFragmentInteractionListener {

    @BindView(com.cit.michael.sportshub.R.id.btnOrganizeEvent) Button btnOrganizeEvent;
    @BindView(R.id.btnNearby) Button btnNearby;
    @BindView(R.id.btnMyEvents) Button btnMyEvents;
    @BindView(R.id.btnRecentEvents) Button btnRecentEvents;
    @BindView(R.id.btnChat) Button btnChat;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private FirebaseAuth auth;
    String newUser;
    NetworkService service;
    FirebaseInstanceId mFirebaseInstanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseInstanceId = FirebaseInstanceId.getInstance();

        String newUser = getIntent().getStringExtra("user");

        if(newUser.equals("new")){
            addUserToDatabase();
        }

        auth.getCurrentUser().getUid();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1,false);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Log.d("Name ", "" + auth.getCurrentUser().getDisplayName());
        Log.d("Name PHOTO ", "" + auth.getCurrentUser().getPhotoUrl());
        Log.d("Name getEmail", "" + auth.getCurrentUser().getEmail());
        Log.d("User token: ", "" +   mFirebaseInstanceId.getToken().toString() );
        Log.d("User token: ", "" +   auth.getCurrentUser().getUid() );

    }

    private void addUserToDatabase() {
        User newUser = new User(auth.getCurrentUser().getUid(),auth.getCurrentUser().getDisplayName(), auth.getCurrentUser().getPhotoUrl().toString(), null, null, mFirebaseInstanceId.getToken().toString());

        service.addUser(newUser).enqueue(new Callback<RestUsers>() {
            @Override
            public void onResponse(Call<RestUsers> call, Response<RestUsers> response) {

            }

            @Override
            public void onFailure(Call<RestUsers> call, Throwable t) {

            }
        });
        //auth.getCurrentUser().
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.logOut) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //user logged out
                            Log.d("AUTH", "User Logged Out");
                            finish();
                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ButterKnife.bind(this, rootView);



/*            Button btnOrganizeEvent = (Button) rootView.findViewById(R.id.btnOrganizeEvent);
            Button btnNearby = (Button) rootView.findViewById(R.id.btnNearby);
            Button btnMyEvents = (Button) rootView.findViewById(R.id.btnMyEvents);
            Button btnRecentEvents = (Button) rootView.findViewById(R.id.btnRecentEvents);*/

            return rootView;
        }
        @OnClick(R.id.btnOrganizeEvent)
        public void submit(View rootView) {
            // TODO submit data to server...
            Intent intent = new Intent(rootView.getContext(),Activity_Organize_Event.class);
            startActivity(intent);
        }

        @OnClick(R.id.btnNearby)
        public void search(View rootView) {
            // TODO submit data to server...
            Intent intent = new Intent(rootView.getContext(),Activity_Search_Events.class);
            startActivity(intent);
        }

        @OnClick(R.id.btnChat)
        public void chat(View rootView) {
            // TODO submit data to server...
            Intent intent = new Intent(rootView.getContext(),Activity_Chat.class);
            startActivity(intent);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position == 0){
                return Frag_Group.newInstance(null,null);
            }

            if(position == 2){
                //return Frag_Profile.newInstance(null,null);
                return Fragment_Profile.newInstance(auth.getCurrentUser().getUid(),null);
            }
            if(position == 3){
                //return Frag_Profile.newInstance(null,null);
                return Fragment_Friends_List.newInstance();
            }
   /*         if(position == 3){
                //return Frag_Profile.newInstance(null,null);
                return Frag_Friends_List.newInstance(4);
            }*/
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Groups";
                case 1:
                    return "Home";
                case 2:
                    return "Profile";
                case 3:
                    return "Friends";
            }
            return null;
        }
    }
}
