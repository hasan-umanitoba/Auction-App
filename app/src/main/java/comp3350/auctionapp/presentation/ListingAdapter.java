package comp3350.auctionapp.presentation;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.GregorianCalendar;
import java.util.List;

import comp3350.auctionapp.R;
import comp3350.auctionapp.business.AccessBids;
import comp3350.auctionapp.business.Calculate;
import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Bid;
import comp3350.auctionapp.objects.enums.AuctionType;

//Displays single item within view
public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {
    List<Auction> auctionList;
    private OnAuctionListener myOnAuctionListener;
    private AccessBids accessBids;
    Context theContext;

    //Adapter binds view holder to data; determines content to be displayed
    public ListingAdapter(Context context, List<Auction> auctions, OnAuctionListener myOnAuctionListener) {
        this.auctionList = auctions;
        this.myOnAuctionListener = myOnAuctionListener;
        this.theContext = context;
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.auction_listing, parent, false);

        accessBids = new AccessBids();

        return new ListingViewHolder(view, myOnAuctionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Auction currentItem = auctionList.get(position);
        String listingFileName = currentItem.getProductImage();
        int numBids = accessBids.getAuctionBids(currentItem.getListingID()).size();
        Bitmap image;

        holder.titleTxt.setText(currentItem.getProductName());
        holder.numBidsTxt.setText(numBids == 1 ? numBids + " bid" : numBids + " bids");
        Bid highestBid = accessBids.getHighestBid(currentItem.getListingID());
        if (highestBid != null && currentItem.getAuctionType() == AuctionType.ENGLISH) {
            holder.topBidTxt.setText(highestBid.toString());
        } else {
            holder.topBidTxt.setText("Asking for: " + currentItem.minBidAmountToString());
        }
        holder.sellerTxt.setText(currentItem.getSeller());

        GregorianCalendar today = new GregorianCalendar();

        if (currentItem.compareEndDateTo(today) < 0) {
            holder.timeLeftTxt.setText(R.string.closed_msg);
        } else if (currentItem.compareStartDateTo(today) > 0) {
            holder.timeLeftTxt.setText(R.string.coming_soon_msg);
        } else {
            holder.timeLeftTxt.setText(Calculate.formatTimeDifference(today, currentItem.getEndDate()));
        }


        if (listingFileName != null) {
            image = getBitmapFromAssets("product_images" + "/" + currentItem.getProductImage());

            if (image != null) {
                holder.productPic.setImageBitmap(image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return auctionList.size();
    }

    public class ListingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTxt;
        TextView numBidsTxt;
        TextView topBidTxt;
        TextView sellerTxt;
        ImageView productPic;
        OnAuctionListener onAuctionListener;
        TextView timeLeftTxt;

        public ListingViewHolder(@NonNull View itemView, OnAuctionListener onAuctionListener) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.listing_title);
            numBidsTxt = itemView.findViewById(R.id.num_bids);
            topBidTxt = itemView.findViewById(R.id.top_bid);
            sellerTxt = itemView.findViewById(R.id.seller);
            timeLeftTxt = itemView.findViewById(R.id.time_remaining);
            productPic = itemView.findViewById(R.id.listing_pic);
            this.onAuctionListener = onAuctionListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onAuctionListener.onAuctionClick(getAdapterPosition());
        }
    }

    public interface OnAuctionListener {
        void onAuctionClick(int position);
    }

    private Bitmap getBitmapFromAssets(String fileName) {
        AssetManager assetManager = theContext.getAssets();
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