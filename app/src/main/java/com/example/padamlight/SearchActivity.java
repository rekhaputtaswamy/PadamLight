package com.example.padamlight;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.padamlight.utils.Toolbox;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.spinner_from)
    Spinner mSpinnerFrom;
    @Bind(R.id.spinner_to)
    Spinner mSpinnerTo;
    @Bind(R.id.button_search)
    Button mButtonSearch;
    @Bind(R.id.drawerlayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav)
    NavigationView navigationView;

    private ActionBarDrawerToggle toggle;
    private MapActionsDelegate mapDelegate;
    private HashMap<String, Suggestion> mFromList;

    /*
        Constants FROM lat lng
     */
    static LatLng PADAM = new LatLng(48.8609, 2.349299999999971);
    static LatLng TAO = new LatLng(47.9022, 1.9040499999999838);
    static LatLng FLEXIGO = new LatLng(48.8598, 2.0212400000000343);
    static LatLng LA_NAVETTE = new LatLng(48.8783804, 2.590549);
    static LatLng ILEVIA = new LatLng(50.632, 3.05749000000003);
    static LatLng NIGHT_BUS = new LatLng(45.4077, 11.873399999999947);
    static LatLng FREE2MOVE = new LatLng(33.5951, -7.618780000000015);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding UI elements defined below
        ButterKnife.bind(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);

        initializeTextViews();
        initSpinners();
        initMap();
    }

    private void initializeTextViews() {
        mButtonSearch.setText(R.string.button_search);
    }

    private void initMap() {
        /*
            Instanciate MapFragment to get the map on the page
         */
        MapFragment mapFragment = MapFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_map, mapFragment)
                .commitAllowingStateLoss();

        //
        this.mapDelegate = mapFragment;
    }

    /**
     * Initialize spinners from and to
     */
    private void initSpinners() {
        List<String> fromList = Toolbox.formatHashmapToList(initFromHashmap());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fromList);
        mSpinnerFrom.setAdapter(adapter);

        ArrayAdapter<String> to_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fromList);
        mSpinnerTo.setAdapter(to_adapter);
    }

    /**
     * Using Hashmap to initialize FROM suggestion list
     */
    private HashMap<String, Suggestion> initFromHashmap() {
        mFromList = new HashMap<>();
        mFromList.put("Padam", new Suggestion(PADAM));
        mFromList.put("Tao Résa'Est", new Suggestion(TAO));
        mFromList.put("Flexigo", new Suggestion(FLEXIGO));
        mFromList.put("La Navette", new Suggestion(LA_NAVETTE));
        mFromList.put("Ilévia", new Suggestion(ILEVIA));
        mFromList.put("Night Bus", new Suggestion(NIGHT_BUS));
        mFromList.put("Free2Move", new Suggestion(FREE2MOVE));
        return mFromList;
    }

    /**
     * Define what to do after the button click interaction
     */
    @OnClick(R.id.button_search)
    void onClickSearch() {

        /*
            Retrieve selection of "From" spinner
         */
        String selectedFrom = String.valueOf(mSpinnerFrom.getSelectedItem());
        if (selectedFrom != null || !selectedFrom.isEmpty()) {
            mapDelegate.clearMap();
            Suggestion selectedFromSuggestion = mFromList.get(selectedFrom);
            mapDelegate.updateMarker(MarkerType.PICKUP, selectedFrom, selectedFromSuggestion.getLatLng());
            mapDelegate.updateMap(selectedFromSuggestion.getLatLng());
        }

        String selectedTo = String.valueOf(mSpinnerTo.getSelectedItem());
        if(selectedTo != null || !selectedTo.isEmpty()) {
            Suggestion selectedToSuggestion = mFromList.get(selectedTo);
            mapDelegate.updateMarker(MarkerType.DROPOFF, selectedTo, selectedToSuggestion.getLatLng());
            mapDelegate.updateMap(selectedToSuggestion.getLatLng());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.search_map:
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
                break;
            case R.id.resume:
                Intent intent = new Intent(SearchActivity.this, PropositionsActivity.class);
                startActivity(intent);
                break;
            default:
                return true;
        }
        return true;
    }
}


