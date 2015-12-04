package com.swooby.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment
        extends Fragment
{
    public static final String TAG = "MainActivityFragment";

    private TextView mTextViewContact;

    public MainActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mTextViewContact = (TextView) view.findViewById(R.id.textViewContact);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pickContact();
            }
        });
    }

    private static final int PICK_CONTACT_REQUEST = 1;

    public void pickContact()
    {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_CONTACT_REQUEST)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Uri contactUri = data.getData();
                String[] projection = { Phone.DISPLAY_NAME, Phone.NUMBER };

                Cursor cursor = getContext().getContentResolver().query(contactUri, projection, null, null, null);
                if (cursor == null)
                {
                    Log.w(TAG, "Unexpected null cursor");
                    return;
                }

                if (!cursor.moveToFirst())
                {
                    Log.w(TAG, "Unexpected empty cursor");
                    return;
                }

                String contactDisplayName = "UNKNOWN";
                String contactPhoneNumber = "UNKNOWN";

                int column;

                column = cursor.getColumnIndex(Phone.DISPLAY_NAME);
                if (column != -1)
                {
                    contactDisplayName = cursor.getString(column);
                }
                Log.i(TAG, "contactDisplayName=" + contactDisplayName);

                column = cursor.getColumnIndex(Phone.NUMBER);
                if (column != -1)
                {
                    contactPhoneNumber = cursor.getString(column);
                }
                Log.i(TAG, "contactPhoneNumber=" + contactPhoneNumber);

                cursor.close();

                String text = "DISPLAY_NAME=" + contactDisplayName + '\n' +
                              "NUMBER=" + contactPhoneNumber;
                mTextViewContact.setText(text);
            }
        }
    }
}
