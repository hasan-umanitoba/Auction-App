package comp3350.auctionapp.presentation;

import android.app.Activity;
import android.app.AlertDialog;

import comp3350.auctionapp.R;

public class Messages {

    public static void warning(Activity owner, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(owner).create();

        alertDialog.setTitle(owner.getString(R.string.warning));
        alertDialog.setMessage(message);

        alertDialog.show();
    }
}
