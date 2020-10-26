package comp3350.auctionapp.presentation;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

import comp3350.auctionapp.R;
import comp3350.auctionapp.business.AccessBids;
import comp3350.auctionapp.business.AccessRatings;
import comp3350.auctionapp.business.AccessUsers;
import comp3350.auctionapp.business.Calculate;
import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.enums.AuctionType;

public class IndivAuctionActivity extends AppCompatActivity {
    private TextView productTxt;
    private TextView descriptionTxt;
    private TextView bidTxt;
    private TextView numBidsTxt;
    private TextView usernameTxt;
    private TextView timeLeftTxt;
    private TextView auctionTypeTxt;
    private TextView ratings;
    private AccessBids accessBids;
    private Auction auction;
    private AccessRatings accessRatings;
    private int numBids;

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indiv_auction);

        accessRatings = new AccessRatings();
        productTxt = findViewById(R.id.product_name);
        descriptionTxt = findViewById(R.id.auction_description);
        bidTxt = findViewById(R.id.bid_amount);
        numBidsTxt = findViewById(R.id.num_bids);
        usernameTxt = findViewById(R.id.username);
        timeLeftTxt = findViewById(R.id.time_left);
        auctionTypeTxt = findViewById(R.id.auction_type_label);
        ratings = findViewById(R.id.rating);
    }

    @Override
    protected void onStart() {
        super.onStart();

        accessBids = new AccessBids();
        numBids = 0;

        //retrieve proper auction if there is one
        Serializable extra = getIntent().getSerializableExtra("auction");

        if (extra != null) {
            auction = (Auction) getIntent().getSerializableExtra("auction");
        }

        if (extra == null || auction == null) {
            throw new NullPointerException("No valid auction object!");
        }

        //everything needed to ensure fields get filled in correctly
        Bid highestBid = accessBids.getHighestBid(auction.getListingID());
        if (highestBid != null && auction.getAuctionType() == AuctionType.ENGLISH) {
            bidTxt.setText(highestBid.toString());
        } else {
            bidTxt.setText("Asking for: " + auction.minBidAmountToString());
        }

        List<Bid> auctionsBids = accessBids.getAuctionBids(auction.getListingID());
        numBids = auctionsBids.size();

        GregorianCalendar today = new GregorianCalendar();


        //fill in all the fields properly
        numBidsTxt.setText(numBids == 1 ? numBids + " bid" : numBids + " bids");
        productTxt.setText(auction.getProductName());
        descriptionTxt.setText(auction.getProductDescription());
        usernameTxt.setText(auction.getSeller());
        // fill in the rating
        double averageRating = (Calculate.ratingAverage(accessRatings.getRatings(auction.getSeller())));
        String aveRat = averageRating + " STARS";
        ratings.setText(aveRat);
        if (auction.compareEndDateTo(today) < 0) {
            timeLeftTxt.setText(R.string.closed_msg);
        } else {
            timeLeftTxt.setText(Calculate.formatTimeDifference(today, auction.getEndDate()));
        }
        auctionTypeTxt.setText(auction.getAuctionType().name() + " auction");

        Button button = findViewById(R.id.bid_button);
        AccessUsers accessUsers = new AccessUsers();

        if (auction.compareStartDateTo(today) > 0 ||
                auction.compareEndDateTo(today) < 0 ||
                accessUsers.getSessionUser().equals(auction.getSeller())) {
            button.setEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent newBidIntent = new Intent(getApplicationContext(), BidActivity.class);
                newBidIntent.putExtra("auction", auction);
                startActivity(newBidIntent);
            }
        });

        String picturePath = "product_images/" + auction.getProductImage();
        Bitmap picture = getBitmapFromAssets(picturePath);

        if (picture != null) {
            ImageView auctionImage = findViewById(R.id.auction_image);

            auctionImage.setImageBitmap(picture);
        }
    }

    private Bitmap getBitmapFromAssets(String fileName) {
        AssetManager assetManager = getApplicationContext().getAssets();
        InputStream in;
        Bitmap bitmap = null;

        try {
            in = assetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace(); //Won't crash app if this occurs
        }
        return bitmap;
    }
}
