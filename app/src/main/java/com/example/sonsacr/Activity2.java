package com.example.sonsacr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Activity2 extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Button btnContactes, btnGrav, btnTrucades;
    ArrayList<String> contactes ;
    ArrayList<String> trucades ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        recyclerView = (RecyclerView) findViewById(R.id.rv);


        btnContactes = (Button) findViewById(R.id.Button1);
        btnGrav = (Button) findViewById(R.id.Button2);
        btnTrucades = (Button) findViewById(R.id.Button3);

        btnContactes.setOnClickListener(this);
        btnGrav.setOnClickListener(this);
        btnTrucades.setOnClickListener(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    public ArrayList<String> getContacts() {

        ArrayList<String> alContacts = new ArrayList<String>();
        ContentResolver cr = getApplicationContext().getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        alContacts.add(contactNumber);
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext());
        }
        return alContacts;
    }

    public ArrayList<String> getCallDetails() {

        ArrayList<String> sasTrucades = new ArrayList<String>();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sasTrucades.add("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir);
        }
        managedCursor.close();
        return sasTrucades;

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.Button1) {
            contactes = getContacts();
            mAdapter = new MyAdapter(contactes);
            recyclerView.setAdapter(mAdapter);
        }

        if (id == R.id.Button2) {

        }

        if (id == R.id.Button3) {
            trucades = getCallDetails();
            mAdapter = new MyAdapter(trucades);
            recyclerView.setAdapter(mAdapter);
        }
    }
}
