
package com.cit.michael.sportshub.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.Event;
import com.cit.michael.sportshub.model.Location;
import com.cit.michael.sportshub.model.Search;
import com.cit.michael.sportshub.model.Sport;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestEvent;
import com.cit.michael.sportshub.rest.model.RestLocation;
import com.cit.michael.sportshub.rest.model.RestSport;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Activity_Search_Events extends AppCompatActivity {
    @BindView(R.id.txtEventSearchStart) TextView txtEventSearchStart;
    @BindView(R.id.txtEventSearchFinish) TextView txtEventSearchFinish;
    @BindView(R.id.txtSearchLocationSE) ImageView txtSearchLocationSE;
    @BindView(R.id.txtDisplayLocation) TextView txtDisplayLocation;
    @BindView(R.id.txtSearchSport) TextView txtSearchSport;
    @BindView(R.id.btnSearch) TextView btnSearch;
    public List<Sport> listOfSports;
    public List<Location> listOfLocations;
    public int selectedLocationId = 0;
    public int selectedSportId = 0;
    NetworkService service;
    private static TextView searchDate;
    public static String dbDate;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__search__events);
        ButterKnife.bind(this);

        listOfLocations = new ArrayList<Location>();
        listOfSports = new ArrayList<Sport>();

        service = RestClient.getSportsHubApiClient();
        searchDate = (TextView) findViewById(R.id.txtSearchEventDate);
        getData();

        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar1);
        rangeSeekbar.setMinValue(6);
        rangeSeekbar.setMaxValue(24);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                if(minValue.intValue()<10){
                    txtEventSearchStart.setText("0" + String.valueOf(minValue) + ":00");
                }
                else {
                    txtEventSearchStart.setText(String.valueOf(minValue) + ":00");
                }
                txtEventSearchFinish.setText(String.valueOf(maxValue) + ":00");
            }
        });

        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                if(minValue.intValue()<10){
                    txtEventSearchStart.setText("0" + String.valueOf(minValue) + ":00");
                }
                else {
                    txtEventSearchStart.setText(String.valueOf(minValue) + ":00");
                }
                txtEventSearchFinish.setText(String.valueOf(maxValue) + ":00");
            }
        });

    }

    private void getData() {
        service.getSport()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestSport>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RestSport restSport) {
                        listOfSports = restSport.getSport();
                    }

                });

        service.getLocation()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestLocation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RestLocation restLocation) {
                        if (restLocation.getLocation().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Error: Couldnt retrieve data...", Toast.LENGTH_SHORT).show();
                        } else {
                            listOfLocations = restLocation.getLocation();
                        }
                    }
                });
    }

    @OnClick(R.id.txtSearchLocationSE)
    public void dialogChooseLocation(View view) {
        // TODO submit data to server...
        dialogChooseLocation();
    }
    @OnClick(R.id.txtSearchSport)
    public void dialogChooseSport(View view) {
        // TODO submit data to server...
        dialogChooseSport();
    }


    @OnClick(R.id.btnSearch)
    public void search(View view) {
        // TODO submit data to server...
        searchEvent();
    }

    private void searchEvent() {
        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Searching...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Search nSearch = new Search(selectedLocationId,dbDate,txtEventSearchStart.getText().toString(),txtEventSearchFinish.getText().toString(), selectedSportId);

        service.searchEvent(nSearch).enqueue(new Callback<RestEvent>(){
            @Override
            public void onResponse(Call<RestEvent> call, Response<RestEvent> response) {
                List<Event> result = response.body().getEvent();
                if(!result.isEmpty()){
                    dialog.dismiss();
                    Log.d("EVENT", "SEARCH RESULTS SIZE: " + result.size());
                    Intent intent = new Intent(getApplicationContext(),Activity_Search_Results.class);
                    intent.putParcelableArrayListExtra("searchResults", (ArrayList<? extends Parcelable>) result);
                    startActivity(intent);

                }
                else{
                    dialog.dismiss();
                    Toast.makeText(Activity_Search_Events.this, "No events found!", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<RestEvent> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void dialogChooseLocation() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
//        Log.d("ERR", "List of location size: " + listOfLocations.size());
        if(listOfLocations.isEmpty()){
            //Toast.makeText(Activity_Search_Events.this, "Error: Couldnt not retrieve data...", Toast.LENGTH_SHORT).show();
            getLocationData();
        }
        else {
            for (int i = 0; i < listOfLocations.size(); i++) {
                adapter.add(listOfLocations.get(i).getLocationName());
            }

            AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light));
            builder.setTitle("Locations");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    txtDisplayLocation.setText(listOfLocations.get(item).getLocationName());
                    selectedLocationId = listOfLocations.get(item).getLocationId();
                }
            });
            builder.show();
        }
    }
    private void dialogChooseSport() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
        if(listOfSports.isEmpty()){
            getSportData();
        }
        else {
            for (int i = 0; i < listOfSports.size(); i++) {
                adapter.add(listOfSports.get(i).getSportName());
            }

            AlertDialog.Builder builder = new AlertDialog.Builder( new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light));
            builder.setTitle("Sports");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    txtSearchSport.setText(listOfSports.get(item).getSportName());
                    selectedSportId = listOfSports.get(item).getSportId();
                }
            });
            builder.show();
        }
    }
    private void getLocationData() {

        service.getLocation()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestLocation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RestLocation restLocation) {
                        if (restLocation.getLocation().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Error: Couldnt retrieve data...", Toast.LENGTH_SHORT).show();
                        } else {
                            listOfLocations = restLocation.getLocation();
                        }
                    }
                });

    }

    private void getSportData() {
        service.getSport()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestSport>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RestSport restSport) {
                        listOfSports = restSport.getSport();
                    }

                });
    }

    public void btnDate(View v) {

        DialogFragment newFragment = new SearchDatePickerFragment();
        // newFragment.show(getSupportFragmentManager(), "datePicker");
        newFragment.show(getFragmentManager(), "Date Picker");
    }
    public static class SearchDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

/*
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMinDate(new Date().getTime());
*/

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String displayDate;
            if(monthOfYear<9){
                if(dayOfMonth<10){
                    dbDate = year + "-" + "0" +(monthOfYear + 1) + "-" +  "0" + dayOfMonth;
                    displayDate = "0" + dayOfMonth + "-" + "0" +(monthOfYear + 1) + "-" + year;
                }
                else {
                    dbDate =year + "-" + "0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    displayDate = dayOfMonth + "-" + "0" + (monthOfYear + 1) + "-" + year;
                }
            }else{

                dbDate =year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                displayDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" +year;
            }
            //set_date.setText(displayDate);


           // searchDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            searchDate.setText(displayDate);
            //searchDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
/*            sYear = year;
            sMonth = monthOfYear;
            sDay = dayOfMonth;*/
        }
    }


}
