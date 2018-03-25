package com.example.jayesh.jumpcontatcts;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class viewContacts extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("UserName"));
        ;
        setSupportActionBar(toolbar);
        ImageButton logOut = (ImageButton) findViewById(R.id.logOut);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(viewContacts.this, MainActivity.class));
            }
        });

        mFirestore = FirebaseFirestore.getInstance();
        mFirestore.collection(getIntent().getStringExtra("UserId")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<DocumentSnapshot> result = task.getResult().getDocuments();
                final ListView contactView = (ListView) findViewById(R.id.contactList);
                contactView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return result.size();
                    }

                    @Override
                    public Object getItem(int i) {
                        return result.get(i);
                    }

                    @Override
                    public long getItemId(int i) {
                        return i;
                    }

                    @Override
                    public View getView(int i, View view, ViewGroup viewGroup) {

                        view = getLayoutInflater().inflate(R.layout.contact_list_view, null);
                        final DocumentSnapshot snapshot = result.get(i);
                        final int index = i;
                        ((TextView) view.findViewById(R.id.name)).setText(snapshot.get("name").toString());
                        ((TextView) view.findViewById(R.id.number)).setText(snapshot.get("contactNo").toString());
                        ((ImageButton) view.findViewById(R.id.info)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Dialog d = new Dialog(view.getContext());
                                d.setContentView(R.layout.layout_add_contact);
                                d.show();
                                Button delete = ((Button) d.findViewById(R.id.dismiss));
                                delete.setText("DELETE");
                                final DocumentSnapshot dShot = result.get(index);
                                final EditText name = (EditText) d.findViewById(R.id.contactName);
                                final EditText contactNo = (EditText) d.findViewById(R.id.contactNo);
                                name.setText(dShot.get("name").toString());
                                contactNo.setText(dShot.get("contactNo").toString());
                                delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        mFirestore.collection(getIntent().getStringExtra("UserId")).document(dShot.getId()).delete();
                                        d.dismiss();
                                        recreate();
                                    }
                                });
                                ((Button) d.findViewById(R.id.pickLocation)).setText("CHANGE LOCATION");
                                ((Button) d.findViewById(R.id.pickLocation)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        try {
                                            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                                            Intent intent = intentBuilder.build(viewContacts.this);
                                            // Start the intent by requesting a result,
                                            // identified by a request code.
                                            startActivityForResult(intent,1);

                                        } catch (GooglePlayServicesRepairableException e) {
                                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
                                        } catch (GooglePlayServicesNotAvailableException e) {
                                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
                                        }
                                    }
                                });
                                Button update = ((Button) d.findViewById(R.id.addContact));
                                update.setText("UPDATE");
                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if(name.getText().toString().trim().equals("") && contactNo.getText().toString().trim().equals(""))
                                        {
                                            name.setError("atleast 1 required");
                                            contactNo.setError("atleast 1 required");
                                        }
                                        else {

                                            DocumentReference userId = mFirestore.collection(getIntent().getStringExtra("UserId")).document(dShot.getId());
                                            userId.update("name", name.getText().toString());
                                            userId.update("contactNo", contactNo.getText().toString());
                                            if (lat != 0.00 || lon != 0.00) {
                                                userId.update("latitude", lat);
                                                userId.update("longitude", lon);
                                            }
                                            Toast.makeText(getApplicationContext(), "Contact Updated succesfully", Toast.LENGTH_SHORT).show();
                                            d.dismiss();
                                            recreate();
                                        }
                                    }
                                });
                            }
                        });
                        ((ImageButton) view.findViewById(R.id.locationButton)).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                String strUri = "http://maps.google.com/maps?q=loc:" + snapshot.get("latitude") + "," + snapshot.get("longitude");
                                startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri)));
                            }
                        });
                        return view;
                    }
                });
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lat=0;
                lon=0;
                final Dialog d = new Dialog(view.getContext());
                d.setContentView(R.layout.layout_add_contact);
                d.show();
                ((Button) d.findViewById(R.id.dismiss)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                ((Button) d.findViewById(R.id.addContact)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EditText name = (EditText) d.findViewById(R.id.contactName);
                        EditText contactNo = (EditText) d.findViewById(R.id.contactNo);
                        if(name.getText().toString().trim().equals("") && contactNo.getText().toString().trim().equals(""))
                        {
                            name.setError("atleast 1 required");
                            contactNo.setError("atleast 1 required");
                        }
                        else {
                            mFirestore.collection(getIntent().getStringExtra("UserId")).add(new Contacts(contactNo.getText().toString(), name.getText().toString(), lat, lon));
                            Toast.makeText(getApplicationContext(), "Contact added succesfully", Toast.LENGTH_SHORT).show();
                            d.dismiss();

                            recreate();
                        }
                    }
                });
                ((Button) d.findViewById(R.id.pickLocation)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                            Intent intent = intentBuilder.build(viewContacts.this);
                            // Start the intent by requesting a result,
                            // identified by a request code.
                            startActivityForResult(intent,1);

                        } catch (GooglePlayServicesRepairableException e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
                        } catch (GooglePlayServicesNotAvailableException e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        });
    }
     double lat =0;
     double lon =0;
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){

        Log.d("aayaaa","--------------");
            if ( requestCode==1 && resultCode == Activity.RESULT_OK) {
                // The user has selected a place. Extract the name and address.
                final Place place = PlacePicker.getPlace(data, this);
                Log.d("Location : ",place.getLatLng().toString());
                lat=place.getLatLng().latitude;
                lon=place.getLatLng().longitude;


            }
            else {
                Log.d("Location ","---------------");
                super.onActivityResult(requestCode, resultCode, data);
            }

        }


}
