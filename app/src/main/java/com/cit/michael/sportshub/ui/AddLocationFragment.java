package com.cit.michael.sportshub.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.Location;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by micha on 04/04/2017.
 */

public class AddLocationFragment extends DialogFragment implements Validator.ValidationListener {
    Dialog dialog;
    Context ctx;
    NetworkService service;
    private FirebaseAuth auth;
    double latitude, longitude;
    AddLocationtDialogListener mListener;

    public Validator validator;
    @NotEmpty
    @BindView(R.id.txtLocationName)
    EditText txtLocationName;
    @NotEmpty
    @BindView(R.id.txtAddress1)
    EditText txtAddress1;
    @NotEmpty
    @BindView(R.id.txtAddress2)
    EditText txtAddress2;
    @BindView(R.id.txtAddress3)
    EditText txtAddress3;
    @BindView(R.id.txtLatitude)
    EditText txtLatitude;

    @BindView(R.id.txtLongitude)
    EditText txtLongitude;

    public AddLocationFragment(Context ctx){
        this.ctx = ctx;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        service = RestClient.getSportsHubApiClient();
        validator = new Validator(this);
        validator.setValidationListener(this);
        dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.fragment_add_location);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this, dialog);
        return dialog;
    }

    @OnClick(R.id.txtGetCoordinates)
    public void getCoordinates() {

        hideSoftKeyboard();
        Geocoder geocoder = new Geocoder(ctx);
        List<Address> addresses= null;
        String addEntered = txtLocationName.getText() + ", " +  txtAddress1.getText() + ", " + txtAddress2.getText() + ", " + txtAddress3.getText();
        try {
            addresses = geocoder.getFromLocationName(addEntered, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses.size() > 0) {
            latitude= addresses.get(0).getLatitude();
            txtLatitude.setText(Double.toString(latitude));
            longitude= addresses.get(0).getLongitude();
            txtLongitude.setText(Double.toString(longitude));
        }

    }

    @OnClick(R.id.txtAddLocationCancel)
    public void cancel() {
        dialog.dismiss();

    }

    @OnClick(R.id.txtAddLocationDone)
    public void addLocationDone() {
        validator.validate();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddLocationtDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }


    @Override
    public void onValidationSucceeded() {
        hideSoftKeyboard();
        final Location newLocation = new Location(null,txtLocationName.getText().toString(),longitude,latitude,txtAddress1.getText().toString(),txtAddress2.getText().toString(),txtAddress3.getText().toString(),auth.getCurrentUser().getUid());
        service.createLocation(newLocation).enqueue(new Callback<RestLocation>() {
            @Override
            public void onResponse(Call<RestLocation> call, Response<RestLocation> response) {
                    if(!response.body().getMessage().isEmpty()){
                        newLocation.setLocationId(Integer.parseInt(response.body().getMessage()));

                        mListener.dialogListener(newLocation);
                        dismiss();
                    }
            }
            @Override
            public void onFailure(Call<RestLocation> call, Throwable t) {

            }
        });

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(ctx);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
            }
        }
    }
    public interface AddLocationtDialogListener {
        void dialogListener(Location location);
    }

/*    public void onDismiss(DialogInterface dialogInterface)
    {
        //Toast.makeText(ctx, "ON_Dismiss", Toast.LENGTH_SHORT).show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }*/

    protected void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
