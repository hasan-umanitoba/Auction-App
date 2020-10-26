package comp3350.auctionapp.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.GregorianCalendar;

import comp3350.auctionapp.R;
import comp3350.auctionapp.business.AccessBids;
import comp3350.auctionapp.business.AccessUsers;
import comp3350.auctionapp.business.Calculate;
import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.Dollar;
import comp3350.auctionapp.objects.enums.AuctionType;

public class BidActivity extends AppCompatActivity {
    private final String FIXED_AMOUNT_1 = "50"; // Suggested prices shown at the bottom of Screen
    private final String FIXED_AMOUNT_2 = "100";
    private final String FIXED_AMOUNT_3 = "500";
    private Auction auction;// Auction object
    private EditText bidAmount; // Amount entered by user
    private AccessBids accessBids;
    private AccessUsers accessUsers;
    private String popupMessage;
    private Bid highestBid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bid);
        accessUsers = new AccessUsers();
        accessBids = new AccessBids();
        bidAmount = findViewById(R.id.amount_txt); // Bid amount entered by user

        TextView productTxt = findViewById(R.id.product_title); // Product text at the top
        TextView fixedAmountFirst = findViewById(R.id.basic_bid_1);  // Implemented this to make setup onClick listener
        TextView fixedAmountSecond = findViewById(R.id.basic_bid_2);  // Listens to click to update bid amount when clicked.
        TextView fixedAmountThird = findViewById(R.id.basic_bid_3);
        TextView numBidsTxt = findViewById(R.id.total_bids);
        TextView timeLeftTxt = findViewById(R.id.time_left);

        Serializable extra = getIntent().getSerializableExtra("auction");

        if (extra != null) {
            auction = (Auction) getIntent().getSerializableExtra("auction"); // Gets auction information (will be used in future to when submitting bid )
        }

        if (extra == null || auction == null) {
            throw new NullPointerException("No valid auction object!");
        }

        int numBids = accessBids.getAuctionBids(auction.getListingID()).size();
        highestBid = accessBids.getHighestBid(auction.getListingID()); //Gets highest bid for comparison

        String numBidsString = numBids == 1 ? numBids + " bid" : numBids + " bids";
        numBidsTxt.setText(numBidsString); //Sets number of bids
        productTxt.setText(auction.getProductName()); //Sets product name
        Button submitBidButton = findViewById(R.id.SubmitBid);  // Finds submit bid button
        timeLeftTxt.setText(Calculate.formatTimeDifference(new GregorianCalendar(), auction.getEndDate()));//Sets time left for the auction

        submitBidButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String bidAmt = bidAmount.getText().toString();
                verifyBid(bidAmt);
                Toast toast = Toast.makeText(getApplicationContext(),
                        popupMessage, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 1200);
                toast.show();
                finish();
            }
        });

        fixedAmountFirst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bidAmount.setText(FIXED_AMOUNT_1);  //Updates bid amount when clicked
            }
        });
        fixedAmountSecond.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bidAmount.setText(FIXED_AMOUNT_2); //Updates bid amount when clicked
            }
        });

        fixedAmountThird.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bidAmount.setText(FIXED_AMOUNT_3); //Updates bid amount when clicked
            }
        });
    }

    protected void verifyBid(String bidAmt) {
        if (bidAmt.length() > 0 && !bidAmt.startsWith("0")) {
            Bid newBid = new Bid(accessUsers.getSessionUser(), new Dollar(bidAmt), auction.getListingID(), new GregorianCalendar()); //Makes a new bid object
            Dollar userBidAmt = new Dollar(bidAmt);

            if (((auction.getAuctionType() == AuctionType.ENGLISH && highestBid != null) && (newBid.compareBidAmounts(highestBid) <= 0))) {
                popupMessage = "bid must be higher than current highest bid";
            } else if ((auction.getAuctionType() == AuctionType.ENGLISH && (auction.compareMinBidAmount(userBidAmt) >= 0))) {
                popupMessage = "Enter bid amount higher than the asking amount";
            } else if (userBidAmt.compareTo(new Dollar("0")) == 1 && accessBids.addBid(newBid)) {
                Intent newBidIntent = new Intent(getApplicationContext(), IndivAuctionActivity.class);//reloads the individual auction activity with updated number of bids
                newBidIntent.putExtra("auction", auction);
                startActivity(newBidIntent);
                popupMessage = "bid submitted";
            } else {
                popupMessage = "Select a different bid amount"; //When the user enters same bid amount twice.
            }

        } else { //if the bid amount entered is empty displays a popup
            popupMessage = "bid amount cannot be empty";
        }
    }

}