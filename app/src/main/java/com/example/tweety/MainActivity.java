package com.example.tweety;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.InputType;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUST_CALL = 1;
    public final static String simSlotName[] = {
            "extra_asus_dial_use_dualsim",
            "com.android.phone.extra.slot",
            "slot",
            "simslot",
            "sim_slot",
            "subscription",
            "Subscription",
            "phone",
            "com.android.phone.DialingMode",
            "simSlot",
            "slot_id",
            "simId",
            "simnum",
            "phone_type",
            "slotId",
            "slotIdx"
    };
    static final String syriatel_key = "1";
    static final String mtn_key = "2";
    private static final int PICK_CONTACT = 1;
    static SharedPreferences syriatel_mPrefs;
    static SharedPreferences mtn_mPrefs;
    static SharedPreferences preferences;
    public String Syriatel_code = "";
    public String Mtn_code = "";
    public String switch_sim = "";
    AlertDialog alertDialog1;
    CharSequence[] values = {" Sim 1 : Syriatel " + "\n" + " Sim 2 : MTN", " Sim 1 : MTN " + "\n" + " Sim 2 : Syriatel"};
    EditText number;
    EditText balance;
    Button clean_all;
    String reply_num="";
    String replay_balance="";
    ImageView contacts;
    ImageView send;

    private Context mContext = MainActivity.this;

    private static final int REQUEST = 112;
    int x = 0;

    @SuppressLint("WrongConstant")
    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number = (EditText) findViewById(R.id.number);
        balance = (EditText) findViewById(R.id.rsed);
        clean_all = (Button) findViewById(R.id.clear_all);

        contacts = (ImageView) findViewById(R.id.contact);
        contacts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectFromContacts();
            }
        });

        send = (ImageView) findViewById(R.id.send);
        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterSend();
            }
        });

        preferences = getSharedPreferences("p", 0);
        x = preferences.getInt("key", 0);
        if (x == 1) {
            switch_sim = "sim1s_sim2m";
        } else if (x == 2) {
            switch_sim = "sim1m_sim2s";
        }

        syriatel_mPrefs = getSharedPreferences(Syriatel_code, 0);
        Syriatel_code = syriatel_mPrefs.getString(syriatel_key, "");

        mtn_mPrefs = getSharedPreferences(Mtn_code, 0);
        Mtn_code = mtn_mPrefs.getString(mtn_key, "");

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_CONTACTS};
            if (!hasPermissions(mContext, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST);
            } else {
            }
        } else {
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.syriatel_sim) {

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint(R.string.enter_syriatel_password);
            input.requestFocus();
            final AlertDialog sy = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.syriatel_password)
                    .setMessage("\n" )
                    .setPositiveButton(R.string.Save , null)
                    .setNegativeButton(R.string.Cancel , null)
                    .setView(input)
                    .show();
            final InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            Button p = sy.getButton(AlertDialog.BUTTON_POSITIVE);
            p.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Syriatel_code = input.getText().toString();
                    if (Syriatel_code.length() == 0) {
                        Toast.makeText(MainActivity.this, R.string.enter_password, Toast.LENGTH_SHORT).show();
                    } else {
                        syriatel_mPrefs.edit().putString(syriatel_key, Syriatel_code).apply();
                        sy.dismiss();
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        Toast.makeText(MainActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Button n = sy.getButton(AlertDialog.BUTTON_NEGATIVE);
            n.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    sy.cancel();
                }
            });



            }




        if (id == R.id.mtn_sim) {
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint(R.string.enter_mtn_password);
            input.requestFocus();
            final AlertDialog mt = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.MTN_password)
                    .setMessage("\n" )
                    .setPositiveButton(R.string.Save, null)
                    .setNegativeButton(R.string.Cancel , null)
                    .setView(input)
                    .show();
            final InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            Button p = mt.getButton(AlertDialog.BUTTON_POSITIVE);
            p.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Mtn_code = input.getText().toString();
                    if (Mtn_code.length() == 0) {
                        Toast.makeText(MainActivity.this, R.string.enter_password, Toast.LENGTH_SHORT).show();

                    } else {
                        mtn_mPrefs.edit().putString(mtn_key, Mtn_code).apply();
                        mt.dismiss();
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        Toast.makeText(MainActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Button n = mt.getButton(AlertDialog.BUTTON_NEGATIVE);
            n.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    mt.cancel();
                }
            });

        }

        if (id == R.id.sims_position) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.sims_position);

            builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            x = 1;
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("key", x).apply();
                            editor.commit();
                            switch_sim = "sim1s_sim2m";
                            Toast.makeText(MainActivity.this, " Sim 1 : Syriatel  Sim 2 : MTN ", Toast.LENGTH_LONG).show();
                            break;
                        case 1:
                            x = 2;
                            SharedPreferences.Editor edito = preferences.edit();
                            edito.putInt("key", x).apply();
                            edito.commit();
                            switch_sim = "sim1m_sim2s";
                            Toast.makeText(MainActivity.this, " Sim 1 : MTN  Sim 2 : Syriatel ", Toast.LENGTH_LONG).show();
                            break;
                    }


                    alertDialog1.dismiss();

                }
            });

            alertDialog1 = builder.create();
            alertDialog1.show();
        }

        if (id == R.id.clear_data) {
            syriatel_mPrefs.edit().clear().apply();
            mtn_mPrefs.edit().clear().apply();
            preferences.edit().clear().apply();
            x = 0;
            final AlertDialog.Builder clear = new AlertDialog.Builder(MainActivity.this);
            clear.setTitle(R.string.clear_data);
            clear.setMessage(R.string.clear_data_ok);
            clear.setPositiveButton(R.string.del, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            })
                    .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            clear.show();
        }

        if (id == R.id.about) {
            final AlertDialog.Builder about = new AlertDialog.Builder(MainActivity.this);
            about.setTitle(R.string.about);
            about.setMessage(R.string.about_c);
            about.show();
        }

        if (id == R.id.exit) {
            System.exit(0);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Syriatel_sim1() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUST_CALL);

        } else {
            TelecomManager telecomManager = (TelecomManager) this.getSystemService(Context.TELECOM_SERVICE);
            List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

            String nember = number.getText().toString();
            String rs = balance.getText().toString();
            if (x == 0) {
                Toast.makeText(MainActivity.this, R.string.dont_put_sim_position, Toast.LENGTH_SHORT).show();
            } else if (Syriatel_code == "") {
                Toast.makeText(MainActivity.this, R.string.dont_put_sim, Toast.LENGTH_SHORT).show();
            } else if (nember.length() == 0) {
                Toast.makeText(MainActivity.this, R.string.number, Toast.LENGTH_SHORT).show();
            } else if (rs.length() == 0) {
                Toast.makeText(MainActivity.this, R.string.rsed, Toast.LENGTH_SHORT).show();
            } else {
                String n = "*150*1*" + Syriatel_code + "*1*" + rs + "*" + nember + "*" + nember + "#";
                Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("simSlot", 0);
                intent.putExtra("Cdma_Supp", true);
                int simselected = 0;
                if (simselected == 0) {   //0 for sim1
                    for (String s : simSlotName)
                        intent.putExtra(s, 0); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 0)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(0));
                }
                intent.setData(Uri.parse(Uri.parse("tel:") + Uri.encode(n)));
                startActivity(intent);
                number.setText("");
                balance.setText("");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Syriatel_sim2() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUST_CALL);

        } else {
            TelecomManager telecomManager = (TelecomManager) this.getSystemService(Context.TELECOM_SERVICE);
            List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

            String nember = number.getText().toString();
            String rs = balance.getText().toString();
            if (x == 0) {
                Toast.makeText(MainActivity.this, R.string.dont_put_sim_position, Toast.LENGTH_SHORT).show();
            } else if (Syriatel_code == "") {
                Toast.makeText(MainActivity.this, R.string.dont_put_sim, Toast.LENGTH_SHORT).show();
            } else if (nember.length() == 0) {
                Toast.makeText(MainActivity.this, R.string.number, Toast.LENGTH_SHORT).show();
            } else if (rs.length() == 0) {
                Toast.makeText(MainActivity.this, R.string.rsed, Toast.LENGTH_SHORT).show();
            } else {
                String n = "*150*1*" + Syriatel_code + "*1*" + rs + "*" + nember + "*" + nember + "#";
                Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("simSlot", 1);
                intent.putExtra("Cdma_Supp", true);
                int simselected = 1;
                if (simselected == 1) {   //0 for sim1
                    for (String s : simSlotName)
                        intent.putExtra(s, 1); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 1)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(1));
                }
                intent.setData(Uri.parse(Uri.parse("tel:") + Uri.encode(n)));
                startActivity(intent);
                number.setText("");
                balance.setText("");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Mtn_sim2() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUST_CALL);
        } else {
            TelecomManager telecomManager = (TelecomManager) this.getSystemService(Context.TELECOM_SERVICE);
            List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

            String num = number.getText().toString();
            String rs = balance.getText().toString();
            if (x == 0) {
                Toast.makeText(MainActivity.this, R.string.dont_put_sim_position, Toast.LENGTH_SHORT).show();
            } else if (Syriatel_code == "") {
                Toast.makeText(MainActivity.this, R.string.dont_put_sim, Toast.LENGTH_SHORT).show();
            } else if (num.length() == 0) {
                Toast.makeText(MainActivity.this, R.string.number, Toast.LENGTH_SHORT).show();
            } else if (rs.length() == 0) {
                Toast.makeText(MainActivity.this, R.string.rsed, Toast.LENGTH_SHORT).show();
            } else {

                String n = "*150*" + Mtn_code + "*" + num + "*" + rs + "#";
                Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("simSlot", 1);
                intent.putExtra("Cdma_Supp", true);
                int simselected = 1;
                if (simselected == 1) {   //0 for sim1
                    for (String s : simSlotName)
                        intent.putExtra(s, 1); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 1)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(1));
                }

                intent.setData(Uri.parse(Uri.parse("tel:") + Uri.encode(n)));

                startActivity(intent);
                number.setText("");
                balance.setText("");
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Mtn_sim1() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUST_CALL);
        } else {
            TelecomManager telecomManager = (TelecomManager) this.getSystemService(Context.TELECOM_SERVICE);
            List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

            String num = number.getText().toString();
            String rs = balance.getText().toString();
            if (x == 0) {
                Toast.makeText(MainActivity.this, R.string.dont_put_sim_position, Toast.LENGTH_SHORT).show();
            } else if (Syriatel_code == "") {
                Toast.makeText(MainActivity.this, R.string.dont_put_sim, Toast.LENGTH_SHORT).show();
            } else if (num.length() == 0) {
                Toast.makeText(MainActivity.this, R.string.number, Toast.LENGTH_SHORT).show();
            } else if (rs.length() == 0) {
                Toast.makeText(MainActivity.this, R.string.rsed, Toast.LENGTH_SHORT).show();
            } else {

                String n = "*150*" + Mtn_code + "*" + num + "*" + rs + "#";
                Intent intent = new Intent(Intent.ACTION_CALL).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("simSlot", 0);
                intent.putExtra("Cdma_Supp", true);
                int simselected = 0;
                if (simselected == 0) {   //0 for sim1
                    for (String s : simSlotName)
                        intent.putExtra(s, 0); //0 or 1 according to sim.......

                    if (phoneAccountHandleList != null && phoneAccountHandleList.size() > 0)
                        intent.putExtra("android.telecom.extra.PHONE_ACCOUNT_HANDLE", phoneAccountHandleList.get(0));
                }

                intent.setData(Uri.parse(Uri.parse("tel:") + Uri.encode(n)));

                startActivity(intent);
                number.setText("");
                balance.setText("");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void FilterSend() {
        String num = number.getText().toString();
        String rs = balance.getText().toString();
        if (x == 0) {
            Toast.makeText(MainActivity.this, R.string.dont_put_sim_position, Toast.LENGTH_SHORT).show();
        } else if (num.length() == 0) {
            Toast.makeText(MainActivity.this, R.string.number, Toast.LENGTH_SHORT).show();
        } else if (rs.length() == 0) {
            Toast.makeText(MainActivity.this, R.string.rsed, Toast.LENGTH_SHORT).show();
        }
        else {
            String current0 = String.valueOf(num.charAt(0));
            String current1 = String.valueOf(num.charAt(1));
            if(Integer.parseInt(current0) == 0 && Integer.parseInt(current1) == 9) {
                Send();
            }
            else {
                CodeSend();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Send() {
        String num = number.getText().toString();

         if (num.length() != 10 ) {
            Toast.makeText(MainActivity.this, R.string.enter_the_correct_number , Toast.LENGTH_SHORT).show();
        }
         else {
            String numb = number.getText().toString();
            reply_num = number.getText().toString();
            replay_balance = balance.getText().toString();
            String current = String.valueOf(numb.charAt(2));
            if (switch_sim == "sim1s_sim2m") {
                if (Integer.parseInt(current) == 4 || Integer.parseInt(current) == 5 || Integer.parseInt(current) == 6) {
                    Mtn_sim2();
                } else {
                    Syriatel_sim1();
                }
            } else if (switch_sim == "sim1m_sim2s") {
                if (num.charAt(2) == 4 || num.charAt(2) == 5 || num.charAt(2) == 6) {
                    Mtn_sim1();
                } else {
                    Syriatel_sim2();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void CodeSend() {
        String num = number.getText().toString();
        String numb = number.getText().toString();
        reply_num = number.getText().toString();
        replay_balance = balance.getText().toString();
        String current0 = String.valueOf(numb.charAt(0));
        String current1 = String.valueOf(numb.charAt(1));

        if (switch_sim == "sim1s_sim2m") {
            if (Integer.parseInt(current0) == 2 && Integer.parseInt(current1) == 6 && num.length() >= 9) {
                Mtn_sim2();
            } else {
                Syriatel_sim1();
            }
        } else if (switch_sim == "sim1m_sim2s") {
            if (Integer.parseInt(current0) == 2 && Integer.parseInt(current1) == 6 && num.length() >= 9) {
                Mtn_sim1();
            } else {
                Syriatel_sim2();
            }
        }
    }

    public void replay(View v) {
        number.setText(reply_num);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void replay_process(View view) {

        if (reply_num == null || reply_num.length() == 0) {
            Toast.makeText(MainActivity.this, R.string.null_replay_process, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, reply_num, Toast.LENGTH_SHORT).show();
            number.setText(reply_num);
            balance.setText(replay_balance);

            final AlertDialog.Builder replay_process = new AlertDialog.Builder(MainActivity.this);
            replay_process.setTitle(R.string.replay_process);
            replay_process.setMessage(getResources().getString(R.string.the_number) +" "+ reply_num + "\n" + getResources().getString(R.string.the_balance) +" "+ replay_balance );
            replay_process.setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String current = String.valueOf(reply_num.charAt(2));
                    if (switch_sim == "sim1s_sim2m") {
                        if (Integer.parseInt(current) == 4 || Integer.parseInt(current) == 5 || Integer.parseInt(current) == 6) {
                            Mtn_sim2();
                        } else {
                            Syriatel_sim1();
                        }
                    } else if (switch_sim == "sim1m_sim2s") {
                        if (Integer.parseInt(current) == 4 || Integer.parseInt(current) == 5 || Integer.parseInt(current) == 6) {
                            Mtn_sim1();
                        } else {
                            Syriatel_sim2();
                        }
                    }
                }
            })
                    .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            number.setText("");
                            balance.setText("");
                            dialogInterface.cancel();
                        }
                    });
            replay_process.show();
        }
    }



    public void SelectFromContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
        onActivityResult(PICK_CONTACT, PICK_CONTACT, intent);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("ContactsH", "ResOK");
                Uri contactData = data.getData();
                Cursor contact = getContentResolver().query(contactData, null, null, null, null);

                if (contact.moveToFirst()) {
                    String name = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // TODO Whatever you want to do with the selected contact's name.

                    ContentResolver cr = getContentResolver();
                    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                            "DISPLAY_NAME = '" + name + "'", null, null);
                    if (cursor.moveToFirst()) {
                        String contactId =
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        //
                        //  Get all phone numbers.
                        //
                        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (phones.moveToNext()) {
                            String num = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            num = num.replaceAll("\\s+","");

                            if (num.length() == 13) {
                                num = num.substring(4);
                                num = "0" + num;
                            }

                            if (num.length() == 12) {
                                num = num.substring(3);
                                num = "0" + num;
                            }

                            if (num.length() == 9) {
                                num = "0" + num;
                            }

                            number.setText(num);

                            int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                            switch (type) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    // do something with the Home number here...
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    // do something with the Mobile number here...
                                    // Log.d("ContactsH", number);
                                    //this.callByNumber(number);
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    // do something with the Work number here...
                                    break;
                            }
                        }
                        phones.close();
                    }
                    cursor.close();
                }
            }
        } else {
            Log.d("ContactsH", "Canceled");
        }
    }

    public void clean_all(View v) {
        number.setText("");
        balance.setText("");
    }
}
