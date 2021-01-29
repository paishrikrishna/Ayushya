package com.example.ayushya;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_MAGENTA;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {
    double Lati,Lon;
    int c;
    private Marker[] mmn = new Marker[9999];

    private GoogleMap mMap;
    public static final String EXTRA_TEXT ="com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_NUMBER ="com.example.application.example.EXTRA_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }

        assert locationManager != null;
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        assert null != location;
        onLocationChanged(location);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        final Intent intent = getIntent();
        final String access = intent.getStringExtra(option_list.EXTRA_TEXT);
        final String number = intent.getStringExtra(option_list.EXTRA_NUMBER);
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        // Add a marker in Sydney and move the camera
        CameraPosition.Builder builder = CameraPosition.builder();
        builder.target(new LatLng(Lati, Lon));
        builder.zoom(26);
        builder.bearing(0);
        builder.tilt(45);
        CameraPosition googlePlex = builder
                .build();

        LatLng curr = new LatLng(Lati,Lon);
        mMap.addMarker(new MarkerOptions().position(curr).title("").icon(BitmapDescriptorFactory.defaultMarker(HUE_YELLOW)));
       final DatabaseReference re;
       re = FirebaseDatabase.getInstance().getReference().child(access);
       re.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               int count = (int) dataSnapshot.getChildrenCount();
               c = count;
               for(int i = 1;i<=count;i++){
                   String la =  dataSnapshot.child(String.valueOf(i)).child("location").child("lat").getValue().toString();
                   String lo =  dataSnapshot.child(String.valueOf(i)).child("location").child("long").getValue().toString();
                   String name = dataSnapshot.child(String.valueOf(i)).child("name").getValue().toString();

                   if(!la.equals("o/f")&&!lo.equals("o/f")) {
                       LatLng curr = new LatLng(Double.parseDouble(la), Double.parseDouble(lo));
                       mmn[i] = mMap.addMarker(new MarkerOptions().position(curr).title(name).icon(BitmapDescriptorFactory.defaultMarker(HUE_GREEN)));

                       if(access.equals("patients")){
                           String danger = dataSnapshot.child(String.valueOf(i)).child("disease").getValue().toString();
                           mmn[i].setSnippet(danger);
                       }
                       else{
                           mmn[i].setSnippet("Doctor");
                       }

                   }
                   mmn[i].showInfoWindow();
                   if(access.equals("patients")) {
                       String danger = dataSnapshot.child(String.valueOf(i)).child("disease").getValue().toString();
                       String doc = dataSnapshot.child(String.valueOf(i)).child("doctor").getValue().toString();

                       if(danger.equals("COMMON COLD")){
                           mmn[i].setSnippet(danger);
                           mmn[i].setIcon(BitmapDescriptorFactory.defaultMarker(HUE_BLUE));
                       }
                       if(!doc.equals(number)){
                           mmn[i].remove();
                       }
                   }

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex));


    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onLocationChanged(Location location) {
        double loca =   location.getLongitude();
        double lon  =   location.getLatitude();
        Lati = lon;
        Lon = loca;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final Intent intent = getIntent();
        final String user = intent.getStringExtra(option_list.EXTRA_TEXT);
        if(user.equals("patients")){
            for(int i=1;i<=c;i++){
                if(marker.equals(mmn[i])){
                    mmn[i].setIcon(BitmapDescriptorFactory.defaultMarker(HUE_MAGENTA));
                    open_recore(user, String.valueOf(i));
                }
            }
        }
        return false;
    }

    public void open_recore(String user, String i) {
        Intent intent = new Intent(this,tests.class);
        intent.putExtra(EXTRA_TEXT,"   Previous Check UP   ");
        intent.putExtra(EXTRA_NUMBER,i);
        startActivity(intent);
    }
}
