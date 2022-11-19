package com.teapink.damselindistress.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teapink.damselindistress.R;
import com.teapink.damselindistress.adapters.ContactListAdapter;
import com.teapink.damselindistress.models.Contact;
import com.teapink.damselindistress.models.User;
import com.teapink.damselindistress.utilities.RecyclerViewClickListener;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EmergencyContactsActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private static final int CONTACT_PICKER_RESULT = 1001;
    private User user;
    private ContactListAdapter contactListAdapter;
    private ArrayList<Contact> contactArrayList;
    private SharedPreferences sp;
    DataBaseAdapter db;
    String currentUser;
    String stuff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        db=new DataBaseAdapter(this);
        user = new User();
        contactArrayList = new ArrayList<>();

        RecyclerView emergencyContactsList = (RecyclerView) findViewById(R.id.emergencyContactsList);
        ArrayList<Contact> contactList = new ArrayList<Contact>();

      //  Bundle bundle = getIntent().getExtras();
      //  stuff=bundle.getString("stuff");


        sp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        stuff=sp.getString("stuff","defaultStringIfNothingFound");
        // retrieve user information
      //  SharedPreferences.Editor pedit= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
     //   pedit.putString("stuff",stuff);
     //   pedit.apply();

        getEmergencyContacts(stuff);

        // setting the contacts recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        contactListAdapter = new ContactListAdapter(getApplicationContext(), contactArrayList);
        emergencyContactsList.setLayoutManager(layoutManager);
        emergencyContactsList.setAdapter(contactListAdapter);
        // ask whether the contact is to be deleted on list item click
        emergencyContactsList.addOnItemTouchListener(new RecyclerViewClickListener(getApplicationContext(),
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                        alertDialogBuilder.setTitle(R.string.remove_contact_dialog_title);
                        alertDialogBuilder.setMessage(R.string.remove_contact_dialog_message);
                        alertDialogBuilder.setPositiveButton(R.string.remove_contact_dialog_positive_button_text,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Contact c=contactArrayList.get(position);
                                        String ph=c.getPhone();
                                        db.open();
                                        db.deleteEntry(stuff,ph);
                                        Toast.makeText(getApplicationContext(), "Delete : "+c.getName()+"-"+ph, Toast.LENGTH_LONG).show();

                                        contactArrayList.remove(position);
                                        contactListAdapter.notifyDataSetChanged();
                                    }
                                });
                        alertDialogBuilder.setNegativeButton(R.string.remove_contact_dialog_negative_button_text,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // do nothing
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contactArrayList.size() < 5) {
                    doLaunchContactPicker(view);
                } else {
                    Snackbar.make(view, R.string.emergency_contact_max_limit_error, Snackbar.LENGTH_LONG).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void getEmergencyContacts(String userPhone) {


        try {
            db.open();
            Cursor cr=db.DisplayData(userPhone);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String no=cr.getString(cr.getColumnIndex("P_NAME"));
                        String name=cr.getString(cr.getColumnIndex("P_NO"));
                        Contact contact = new Contact(no, name);
                        contactArrayList.add(contact);


                    }while (cr.moveToNext());
                }else{
                    Toast.makeText(getApplicationContext(), "No Data to show", Toast.LENGTH_LONG).show();
                }
            }
            contactListAdapter.notifyDataSetChanged();
            cr.close();
            db.close();
        }catch (Exception e){

        }













     /*   ArrayList<Contact> emergencyContactList = new ArrayList<>();

        emergencyContactList=db.getAllContacts(userPhone);
        // add to shared preference
        Gson gson = new Gson();
        String jsonArrayList = gson.toJson(emergencyContactList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("contact_list", jsonArrayList);
        editor.apply();

      /*  final ArrayList<Contact> emergencyContactList = new ArrayList<>();
        DatabaseReference databaseRef;
        databaseRef = FirebaseDatabase.getInstance().getReference().child("emergencyList").child(userPhone);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = postSnapshot.getValue(Contact.class);
                    emergencyContactList.add(contact);
                }
                // add to shared preference
                Gson gson = new Gson();
                String jsonArrayList = gson.toJson(emergencyContactList);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("contact_list", jsonArrayList);
                editor.apply();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Firebase Error: " + databaseError.getMessage());
            }
        });
        */
    }

    private void doLaunchContactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;
                    String phone, name;
                    try {
                        Uri result = data.getData();
                        // get the contact id from the Uri
                        String id = result.getLastPathSegment();
                        // query for contact
                        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id + "" }, null);

                        if (cursor != null) {
                            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                            // let's just get the first number
                            if (cursor.moveToFirst()) {
                                phone = cursor.getString(phoneIndex);
                                phone = phone.replaceAll("\\s", "");    // remove all spaces
                                name = cursor.getString(nameIndex);
                                 Contact contact = new Contact(name, phone);
                                int flag = 0;
                                for (Contact c : contactArrayList) {
                                    if (c.getPhone().equals(contact.getPhone())) {  // check if contact already exists in list
                                        Toast.makeText(getApplicationContext(),
                                                R.string.emergency_contact_exists_error, Toast.LENGTH_SHORT).show();
                                        flag = 1;
                                        break;
                                    }
                                }
                                if (flag == 0) {
                                    db.open();
                                    db.insertProduct(stuff,name,phone);
                                    Toast.makeText(getApplicationContext(),stuff+","+name+","+phone, Toast.LENGTH_SHORT).show();

                                    contactArrayList.add(contact);
                                    contactListAdapter.notifyDataSetChanged();
                                }
                                Log.d(TAG, "Retrieved Contact: Phone: " + phone + " Name: " + name);

                            } else {
                                Log.d(TAG, "No results");
                            }
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "Failed to get phone data: " + e.getMessage());
                        Toast.makeText(getApplicationContext(),
                                R.string.emergency_contact_fetch_error, Toast.LENGTH_SHORT).show();
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    R.string.emergency_contact_fetch_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        } else {
            Log.d(TAG, "Activity Result Not OK");
        }
    }

    private void addContactListToDB(ArrayList<Contact> contactArrayList) {
        // Firebase DB
      /*  DatabaseReference databaseRef;
        databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.child("emergencyList").child(user.getPhone()).removeValue();    // delete existing contact list
        for (Contact contact : contactArrayList)    // add contact list
            databaseRef.child("emergencyList").child(user.getPhone()).push().setValue(contact);
*/
        // shared preference
        Gson gson = new Gson();
        String jsonArrayList = gson.toJson(contactArrayList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("contact_list", jsonArrayList);
        editor.apply();

    }

    @Override
    protected void onPause() {
        addContactListToDB(contactArrayList);
        super.onPause();
    }
}
