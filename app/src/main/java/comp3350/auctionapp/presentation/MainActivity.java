package comp3350.auctionapp.presentation;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;
import java.util.List;

import comp3350.auctionapp.R;
import comp3350.auctionapp.application.Services;
import comp3350.auctionapp.business.AccessAuctions;
import comp3350.auctionapp.business.AccessUsers;
import comp3350.auctionapp.objects.Auction;
import comp3350.auctionapp.objects.enums.AuctionType;
import comp3350.auctionapp.objects.enums.Category;

public class MainActivity extends AppCompatActivity implements ListingAdapter.OnAuctionListener {
    private AccessAuctions accessAuctions;
    private List<Auction> displayedAuctions;
    private DrawerLayout drawer;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private int page;
    private TextView usernameHeader;
    private AccessUsers accessUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        copyDatabaseToDevice();
        Services.createDataAccess();

        accessAuctions = new AccessAuctions();
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.listings);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        setupNavView();
        usernameHeader = navigationView.getHeaderView(0).findViewById(R.id.header_username);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  //Sets toolbar

        //Sets up drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Default is home
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.home);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Accessors
        accessAuctions = new AccessAuctions();
        accessUsers = new AccessUsers();
        displayedAuctions = accessAuctions.getOpenAuctions(new GregorianCalendar());

        updateRecyclerView(displayedAuctions); //Initial auctions to show

        usernameHeader.setText(String.format(getString(R.string.nav_header_user), accessUsers.getSessionUser()));
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Services.shutDown();
    }

    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];
            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onAuctionClick(int position) {
        Intent intent;
        if (page == R.string.won_page) {
            intent = new Intent(this, RatingActivity.class);
        } else {
            intent = new Intent(this, IndivAuctionActivity.class);
        }
        intent.putExtra("auction", displayedAuctions.get(position));
        startActivity(intent);
    }

    private Category getCategory(int Page) {
        switch (page) {
            case R.string.electronics_page:
                return (Category.ELECTRONICS);

            case R.string.collectible_page:
                return (Category.COLLECTIBLES);

            case R.string.jewellery_page:
                return (Category.JEWELLERY);

            default:
                return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_both:
                item.setChecked(true);
                updateRecyclerView(filterCat(getCategory(page)));
                break;
            case R.id.show_english:
                item.setChecked(true);
                filterType(AuctionType.ENGLISH);
                break;
            case R.id.show_sealed_bid:
                item.setChecked(true);
                filterType(AuctionType.SEALEDBID);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        //Keeps options menu open after selection is made
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        item.setActionView(new View(this));
        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return false;
            }
        });
        return false;
    }

    // This method is located here in the sample project. So, it shall be here in ours.
    private void copyDatabaseToDevice() {
        final String DB_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {
            assetNames = assetManager.list(DB_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }

            copyAssetsToDirectory(assetNames, dataDirectory);

            Services.setDBPathName(dataDirectory.toString() + "/" + Services.dbName);

        } catch (IOException ioe) {
            Messages.warning(this, "Unable to access application data: " + ioe.getMessage());
        }
    }

    private void updateRecyclerView(List<Auction> newAuctions) {
        ListingAdapter listingAdapter = new ListingAdapter(getApplicationContext(), newAuctions, this);
        displayedAuctions = newAuctions;
        recyclerView.setAdapter(listingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Auction> filterCat(Category category) {
        return accessAuctions.getOpenAuctions(new GregorianCalendar(), category, null);
    }

    private List<Auction> filterWonAuctions() {
        return accessAuctions.getWonAuctions(new GregorianCalendar(), accessUsers.getSessionUser());
    }

    public void filterType(AuctionType type) {
        Category category = getCategory(page);
        List<Auction> filteredAuctions = accessAuctions.getOpenAuctions(new GregorianCalendar(), category, type);

        updateRecyclerView(filteredAuctions);
    }

    private void setupNavView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.new_auction:
                        Intent myIntent = new Intent(MainActivity.this, NewAuctionActivity.class);
                        startActivity(myIntent);
                        return false; //Will not be selected
                    case R.id.collectibles_filter:
                        page = R.string.collectible_page;
                        resetSelectedAuctionType();
                        updateRecyclerView(filterCat(Category.COLLECTIBLES));
                        break;
                    case R.id.jewellery_filter:
                        page = R.string.jewellery_page;
                        resetSelectedAuctionType();
                        updateRecyclerView(filterCat(Category.JEWELLERY));
                        break;
                    case R.id.electronics_filter:
                        page = R.string.electronics_page;
                        resetSelectedAuctionType();
                        updateRecyclerView(filterCat(Category.ELECTRONICS));
                        break;
                    case R.id.home:
                        page = R.string.home_page;
                        resetSelectedAuctionType();
                        updateRecyclerView(accessAuctions.getOpenAuctions(new GregorianCalendar()));
                        break;
                    case R.id.won_auction:
                        page = R.string.won_page;
                        resetSelectedAuctionType();
                        updateRecyclerView(filterWonAuctions());
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    //Will update the options menu to reflect that all auction types are being shown
    private void resetSelectedAuctionType() {
        toolbar.getMenu().findItem(R.id.show_both).setChecked(true);
    }
}