package comp3350.auctionapp.presentation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import comp3350.auctionapp.R;
import comp3350.auctionapp.business.AccessRatings;
import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Rating;

public class RatingActivity extends AppCompatActivity {
    private RatingBar ratingBar; //Rating bar
    private EditText userReviewTxt;// Description of user review
    private TextView userToRate;
    private AccessRatings accessRatings;
    private Auction currentAuction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        accessRatings = new AccessRatings();
        if (savedInstanceState != null ){
             currentAuction = (Auction) savedInstanceState.getSerializable("auction");
        }
        else {
            Bundle extras = getIntent().getExtras();
            if (extras == null){
                currentAuction = null;
            }else {
                currentAuction = (Auction)extras.get("auction");
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        userToRate = findViewById(R.id.product_title);
        ratingBar =  findViewById(R.id.rating_bar);
        userReviewTxt =  findViewById(R.id.user_review);
        userReviewTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Rating.MAX_DESC_LENGTH)}); //Sets maximum length of review description
        ratingBar.setNumStars(Rating.MAX_RATING);//Setting number of rating stars ie 5
        ratingBar.setRating(Rating.MIN_RATING);// Setting initial rating to be minimum
        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() { //Setting rating to minimum if user changes the rating below minimum
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if (rating < Rating.MIN_RATING)
                    ratingBar.setRating(Rating.MIN_RATING);
            }
        });
        userToRate.setText("Rate Your Experience With: " + currentAuction.getSeller());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) { // Hides keyboard whenever user loses focus from the text input
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    public void buttonSubmitRating(View view){

        //submit  the rating
        if (currentAuction != null){
            Rating rating = new Rating(currentAuction.getListingID(),currentAuction.getSeller(),(int)ratingBar.getRating(),"hgfyh");
            String message;
            if (accessRatings.addRating(rating)) {
                message = "Rating successfully created!";
                finish();
            } else {
                message = "Can't add multiple ratings";
            }
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
            // go back to the main activity
            startActivity(new Intent(this,MainActivity.class));
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "This auction doesn't exist", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 1200);
            toast.show();
        }


    }
}