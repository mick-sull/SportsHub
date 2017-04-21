package com.cit.michael.sportshub.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cit.michael.sportshub.R;
import com.cit.michael.sportshub.model.Review;
import com.cit.michael.sportshub.model.User;
import com.cit.michael.sportshub.rest.NetworkService;
import com.cit.michael.sportshub.rest.RestClient;
import com.cit.michael.sportshub.rest.model.RestReview;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cit.michael.sportshub.Constants.REVIEW_ABSENT;
import static com.cit.michael.sportshub.Constants.REVIEW_ATTENDED;

/**
 * Created by micha on 20/04/2017.
 */

public class ReviewFragment extends DialogFragment {
    User user;
    Context ctx;
    private String imageUrl, uID, eventID;
    private ImageView profileImage;
    NetworkService service;
    Review review;
    RadioGroup rgReview;
    private int startAttendValue;

    public ReviewFragment(User user, String eventID, Context ctx) {
        this.user = user;
        this.imageUrl = user.getUserProfileUrl();
        this.uID = user.getUserId();
        this.ctx = ctx;
        this.eventID = eventID;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.fragment_review);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        profileImage = (ImageView)dialog.findViewById(R.id.imgDialogReview);
        rgReview = (RadioGroup) dialog.findViewById(R.id.rgReview);
        Picasso.with(getContext()).load(imageUrl).placeholder(R.drawable.img_circle_placeholder).resize(300,300).transform(new CircleTransform()).into(profileImage);
        service = RestClient.getSportsHubApiClient();

        service.getReview(eventID, uID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RestReview>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RestReview restReview) {
                        review = restReview.getReview().get(0);
                        startAttendValue = review.getAppearance();
                        displayRG();
                    }

                });

        rgReview.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(rgReview.getCheckedRadioButtonId()){
                    case R.id.rbAttended:
                        review.setAppearance(REVIEW_ATTENDED);
                        break;
                    case R.id.rbAbsent:
                        review.setAppearance(REVIEW_ABSENT);
                        break;
                }
            }
        });

        ButterKnife.bind(this, dialog);


        return dialog;
    }

    private void displayRG() {
        if(review.getAppearance() == 1){
            rgReview.check(R.id.rbAttended);
        }
        else{
            rgReview.check(R.id.rbAbsent);
        }
    }


    @OnClick(R.id.txtReviewDone)
    public void cancelRequest(){
            if(review.getAppearance() != startAttendValue){
                service.updateReview(review).enqueue(new Callback<RestReview>() {
                    @Override
                    public void success(Result<RestReview> result) {
                        Toast.makeText(ctx, result.data.getMessage()+ "",  Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });
            }
            else{
                Toast.makeText(ctx, "Review updated", Toast.LENGTH_SHORT).show();
            }
    dismiss();
    }
}
