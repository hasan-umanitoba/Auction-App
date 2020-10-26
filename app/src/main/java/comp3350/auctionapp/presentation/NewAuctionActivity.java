package comp3350.auctionapp.presentation;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import comp3350.auctionapp.R;
import comp3350.auctionapp.business.AccessAuctions;
import comp3350.auctionapp.business.AccessUsers;
import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.Dollar;
import comp3350.auctionapp.objects.Product;
import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;

/**
 * NewAuctionActivity
 * Activity that handles the creation of a new Auction
 */
public class NewAuctionActivity extends AppCompatActivity {
    //Components in Activity used by multiple methods
    private EditText title;
    private EditText description;

    //Business class instances
    private AccessAuctions accessAuction;
    private AccessUsers accessUsers;

    //Values needed to create Auctions
    private AuctionType auctionType;
    private String productImage;
    private int numDays;
    private String categoryChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_auction);

        //Creates business layer instances
        accessUsers = new AccessUsers();
        accessAuction = new AccessAuctions();

        title = findViewById(R.id.product_title);
        description = findViewById(R.id.auction_description);

        setupTitleWatcher();
        setupDescrWacher();
        setupDaysSpinner();
        setupCategorySpinner();

        //Sets values chosen by default
        productImage = "collectible.jpg";
        auctionType = AuctionType.ENGLISH;
    }

    //Verifies then creates a new Auction before adding it to the DB
    public void buttonAuctionCreateOnClick(View view) {
        boolean hasValidInput = validateOnClick();

        if (hasValidInput) {
            boolean newAuctionAdded = accessAuction.addAuction(createNewAuction());
            finishActivity(newAuctionAdded);
        }
    }

    //Updates image chosen by user
    public void onImageRadioClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radio_collectible_image:
                if (checked)
                    productImage = "collectible.jpg";
                break;
            case R.id.radio_electronic_image:
                if (checked)
                    productImage = "iphone.jpg";
                break;
            case R.id.radio_jewellery_image:
                if (checked)
                    productImage = "jewellery.jpg";
                break;
        }
    }

    //Updates category chosen by user
    public void onCategoryRadioClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if (view.getId() == R.id.radio_english && checked) {
            auctionType = AuctionType.ENGLISH;
        } else if (checked) {
            auctionType = AuctionType.SEALEDBID;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // Hides keyboard whenever user loses focus from the text input
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

    private void setupTitleWatcher() {
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String titleSoFar = editable.toString();

                if (titleSoFar.isEmpty()) {
                    title.setError("Need a title!");
                } else if (titleSoFar.length() > Product.MAX_NAME_LEN) {
                    title.setError("Shorten title");
                }

            }
        });
    }

    private void setupDescrWacher() {
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String descrSoFar = editable.toString();

                if (descrSoFar.isEmpty()) {
                    description.setError("Need a description!");
                } else if (descrSoFar.length() > Product.MAX_DESC_LENGTH) {
                    description.setError("Shorten description!");
                }
            }
        });
    }

    private ArrayAdapter<CharSequence> setupSpinnerAdapter(Spinner theSpinner, int spinnerArrId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, spinnerArrId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        theSpinner.setAdapter(adapter);
        return adapter;
    }

    private void setupDaysSpinner() {
        Spinner days = findViewById(R.id.spinner_days);
        ArrayAdapter<CharSequence> adapter = setupSpinnerAdapter(days, R.array.days_array);
        days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numDays = Integer.parseInt((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        numDays = Integer.parseInt((String) Objects.requireNonNull(adapter.getItem(0), "First item in day spinner is null!")); //Default num days
    }

    private void setupCategorySpinner() {
        Spinner category = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = setupSpinnerAdapter(category, R.array.categories_array);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryChosen = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        categoryChosen = (String) adapter.getItem(0); //Default category
    }

    private Dollar getAskingPrice() {
        EditText minAmount = findViewById(R.id.starting_bid);

        if (minAmount.getText().length() == 0) {
            return new Dollar("0.00");
        } else {
            return new Dollar(minAmount.getText().toString());
        }
    }

    private void finishActivity(Boolean newAuctionAdded) {
        String message;

        if (newAuctionAdded) {
            message = "Auction successfully created!";
            finish();
        } else {
            message = "Error! Please try again";
        }
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private Auction createNewAuction() {
        Category category = getProductCategory();
        Product newProduct = new Product(title.getText().toString(), description.getText().toString(), category, productImage);
        Dollar askingPrice = getAskingPrice();
        Calendar startDate = new GregorianCalendar();
        Calendar endDate = new GregorianCalendar();
        endDate.add(Calendar.DAY_OF_MONTH, numDays); //Adds selected number of days to today

        int auctionID = accessAuction.createAuctionID(newProduct, startDate, endDate, auctionType, askingPrice);
        return new Auction(auctionID, accessUsers.getSessionUser(), startDate, endDate, auctionType, newProduct, askingPrice);
    }

    private boolean validateOnClick() {
        boolean allOk = true;

        if (title.getError() != null || title.getText().length() == 0) {
            title.setError("Please enter an auction title");
            allOk = false;
        }

        if (description.getError() != null || description.getText().length() == 0) {
            description.setError("Please enter a description");
            allOk = false;
        }

        return allOk;
    }

    private Category getProductCategory() {
        Category auctionCategory;

        switch (categoryChosen) {
            case "Electronics":
                auctionCategory = Category.ELECTRONICS;
                break;
            case "Jewellery":
                auctionCategory = Category.JEWELLERY;
                break;
            default:
                auctionCategory = Category.COLLECTIBLES;
                break;
        }
        return auctionCategory;
    }
}


