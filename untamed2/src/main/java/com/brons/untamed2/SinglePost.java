package com.brons.untamed2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class SinglePost extends ActionBarActivity{
    SharedPreferences prefs;
    String pid;
    ListView list;
    ArrayList<HashMap<String, String>> commentlist = new ArrayList<>();	//ArrayList of the posts
    Hashtable<String, String> font;			//Hashtable of the font, correlating the imported symbol and it's hex code
    Typeface typeFace;						//font for symbols (user icons)
    String has_voted;
    String hex;
    String dark_hex;



    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_post);

        typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/font.ttf");	//initializes typeface

        font = new Hashtable<>();	//implements hash table of all the characters in the font
        font.put("mask-alien6", "\ue000");
        font.put("mask-android11", "\ue001");
        font.put("mask-apple53", "\ue002");
        font.put("mask-bear10", "\ue003");
        font.put("mask-bear14", "\ue004");
        font.put("mask-brief1", "\ue005");
        font.put("mask-carrot5", "\ue006");
        font.put("mask-cheese6", "\ue007");
        font.put("mask-chicken2", "\ue008");
        font.put("mask-chimp", "\ue009");
        font.put("mask-clown", "\ue00a");
        font.put("mask-coffee17", "\ue00b");
        font.put("mask-cow11", "\ue00c");
        font.put("mask-creepy", "\ue00d");
        font.put("mask-dark20", "\ue00e");
        font.put("mask-donut4", "\ue00f");
        font.put("mask-drink24", "\ue010");
        font.put("mask-duck5", "\ue011");
        font.put("mask-emoticon", "\ue012");
        font.put("mask-emoticon117", "\ue013");
        font.put("mask-fish14", "\ue014");
        font.put("mask-fox2", "\ue015");
        font.put("mask-fried5", "\ue016");
        font.put("mask-head1", "\ue017");
        font.put("mask-icecream6", "\ue018");
        font.put("mask-joystick10", "\ue019");
        font.put("mask-lion", "\ue01a");
        font.put("mask-lion5", "\ue01b");
        font.put("mask-man82", "\ue01c");
        font.put("mask-pacman2", "\ue01d");
        font.put("mask-pirate", "\ue01e");
        font.put("mask-plump", "\ue01f");
        font.put("mask-rabbit6", "\ue020");
        font.put("mask-rocket48", "\ue021");
        font.put("mask-rocket61", "\ue022");
        font.put("mask-sail1", "\ue023");
        font.put("mask-sandwich", "\ue024");
        font.put("mask-saturn8", "\ue025");
        font.put("mask-sheep1", "\ue026");
        font.put("mask-skunk1", "\ue027");
        font.put("mask-sneaker", "\ue028");
        font.put("mask-snowflake3", "\ue029");
        font.put("mask-straight9", "\ue02a");
        font.put("mask-stripy", "\ue02b");
        font.put("mask-sun79", "\ue02c");
        font.put("mask-syringe", "\ue02d");
        font.put("mask-toad", "\ue02e");
        font.put("mask-tux", "\ue02f");
        font.put("mask-two28", "\ue030");
        font.put("mask-umbrella30", "\ue031");
        font.put("mask-vehicle3", "\ue032");
        font.put("mask-water34", "\ue033");



        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent intent = getIntent();
        HashMap<String, String> post = (HashMap<String, String>) intent.getSerializableExtra("post");

        hex = post.get("mask_hex");
        dark_hex = post.get("dark_color");
        has_voted = post.get("has_voted");
        pid = post.get("pid");

        setfabBackground(has_voted, hex, dark_hex);

        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_detail_main);
        ll.setBackgroundColor(Color.parseColor("#e7e7e7"));

      //  ImageButton send = (ImageButton) findViewById(R.id.btn_detail_add_comment);
      //  send.setColorFilter(Color.parseColor(dark_hex));


        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.vote);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Vote().execute(pid);
                setfabBackground(has_voted, hex, dark_hex);
                if (has_voted.equalsIgnoreCase("false"))
                    has_voted = "true";
                else
                    has_voted = "false";
            }
        });

        TextView text = (TextView) findViewById(R.id.text);
        text.setText(post.get("full_text"));
        text.setMovementMethod(new ScrollingMovementMethod());

        TextView time = (TextView) findViewById(R.id.time);

        time.setText(post.get("time_posted"));

        list = (ListView) findViewById(R.id.list_comments);


        ImageButton sendbutton = (ImageButton) findViewById(R.id.btn_detail_add_comment);

        sendbutton.setOnClickListener(new View.OnClickListener(){
            final CharSequence[] items = {"Me","Anonymous"};
            @Override
            public void onClick(View v) {
                EditText stuff = (EditText) findViewById(R.id.detail_comment);
                final String text = stuff.getText().toString();

                if (text.length() < 3) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Comment is too short!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SinglePost.this);
                    builder.setTitle("Who do you want to post as?");
                    builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            switch (item) {
                                case 0:
                                    new Comment().execute(pid, "me", text);
                                    break;
                                case 1:
                                    new Comment().execute(pid, "anon", text);
                                    break;
                            }

                        }

                    });

                    builder.create();
                }
            }
        });


        new JSONParse().execute(pid);
    }

    private void setfabBackground(String has_voted, String hex, String dark_hex){
        FloatingActionButton fab = (FloatingActionButton) findViewById (R.id.vote);
        if(has_voted.equalsIgnoreCase("false")){
            fab.setColorNormal(Color.parseColor("#e7e7e7"));
            fab.setColorPressed(Color.parseColor("#262626"));
        }
        else{
            fab.setColorNormal(Color.parseColor("#262626"));
            fab.setColorPressed(Color.parseColor("#e7e7e7"));        }
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;                //creates a new progress dialog to show the loading of messages

        @Override
        protected void onPreExecute() {            //before executing
            super.onPreExecute();
            pDialog = new ProgressDialog(SinglePost.this);    //creates and displays the progress dialog
            pDialog.setMessage("Getting comments...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {    //do in background while the process is executing
            JSONParser jParser = new JSONParser();                    //creates a new JSONParser object
            // Getting JSON from URL
            if (isNetworkAvailable()) {                                //as long as the network is available
                JSONObject json = null;                //creates a new JSONObject
                try {
                    String token = prefs.getString("access_token", null);
                    if (token != null)

                        json = jParser.getJSONFromUrl("https://beuntamed.com/app/api/2.0/posts/get_comments", token, args[0], false);
                } catch (IOException | JSONException e) {        //catches specific exceptions
                    return null;
                }

                return json;                                //returns the JSON object
            } else {
                JSONObject json;
                try {
                    json = new JSONObject(getStringProperty("json"));
                    return json;
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Error Loading Comments",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //TODO: implement proper offline handling. Currently does not handle disconnection from network

                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {

                pDialog.dismiss();
                if (json == null) {
                    runOnUiThread(new Runnable() {                //Toast message claiming Error with loading the data
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Cannot retrieve post. Please try again later or contact the admins",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }  else {
                    Log.i("option selected", json.toString());
                    JSONArray array;
                    JSONObject data;
                    data = json.getJSONObject("data");
                    array = data.getJSONArray("comments");

                    for(int i=0; i<array.length(); i++) {
                        JSONObject c = array.getJSONObject(i);

                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", c.getString("id"));
                        map.put("author", c.getString("author"));

                        String text = c.getString("text");
                        String tmp = text.replaceAll("<br>", "\n").replaceAll("&hellip;", "... ").replaceAll("&amp;", "&").replaceAll("&ndash;", "-").replaceAll("&quot;", "\"")
                                .replaceAll("&ldquo;", "\"").replaceAll("&rdquo;", "\"").replaceAll("&lsquo;", "\'").replaceAll("&rdquo;", "\'")
                                .replaceAll("&gt;", ">").replaceAll("&lt;", "<");

                        map.put("text", tmp);
                        map.put("has_voted", c.getString("has_voted"));
                        map.put("like_count", "Votes: "+ c.getString("like_count"));
                        map.put("is_author", c.getString("is_author"));
                        map.put("pid", c.getString("pid"));
                        map.put("created_time", c.getString("created_time"));
                        map.put("relative_time", c.getString("relative_time"));

                        JSONObject mask = null;
                        JSONObject author;
                        try{
                            mask = c.getJSONObject("mask");
                        }catch (JSONException ignored){}

                        if(mask!=null){
                            map.put("mask_class", font.get(mask.getString("class")));
                            map.put("mask_hex", mask.getString("color"));
                        }
                        else{
                            author = c.getJSONObject("author");
                            map.put("name", author.getString("name"));
                            map.put("photo", author.getString("photo"));

                        }

                        commentlist.add(map);



                    }

                    ListAdapter adapter = new SimpleAdapter(SinglePost.this, commentlist, R.layout.post_list_item_dark, (new String[] {"text", "like_count",
                            "mask_class", "mask_hex", "relative_time", "pid", "is_author",}),
                            (new int[] {R.id.text, R.id.likes, R.id.avatar, R.id.hexname, R.id.expires, R.id.pid, R.id.isauth})){


                        @Override
                        public View getView(int position, View convertView, ViewGroup parent){
                            View view = super.getView(position, convertView, parent);
                            String name = commentlist.get(position).get("name");
                            TextView text = (TextView) view.findViewById(R.id.avatar);        //gets textview for avatar
                            ImageView img = (ImageView) view.findViewById(R.id.avatarimg);
                            if(name!=null){
                                    text.setVisibility(View.GONE);
                                    img.setVisibility(View.VISIBLE);
                                    String photo = commentlist.get(position).get("photo");
                                    new DownloadImageTask(img).execute(photo);
                            }else {
                                TextView hex = (TextView) view.findViewById(R.id.hexname);        //hidden textview
                                String s = hex.getText().toString();                                //converts hex value to string
                                text.setTextColor(Color.parseColor(s));                            //sets the color to the given color
                                text.setTypeface(typeFace);                                        //sets the typeface to the typeface
                                text.setTextSize(40);                                            //sets size
                                text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);    //sets location
                                text.setBackgroundResource(R.drawable.iconcircle);                //gives icon a circle border
                                text.getBackground().setColorFilter(Color.parseColor(s), PorterDuff.Mode.MULTIPLY);    //sets background
                            }
                            TextView summText = (TextView) view.findViewById(R.id.text);		//sets textview content size

                            if(name!=null){
                                String temp = summText.getText().toString().replaceAll("\n","<br>");
                                Spanned bolded = Html.fromHtml("<b>"+name+"</b>: "+temp);
                                summText.setText(bolded);

                            }

                            summText.setTextSize(16);


                            return view;
                        }

                    };

                    list.setAdapter(adapter);
                    Helper.getListViewSize(list);
                }


            } catch (JSONException e) {
                Log.i("you hooped", "mr. hooped");
            }
        }

    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String getStringProperty(String key) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String res = null;
        if (sharedPreferences != null) {
            res = sharedPreferences.getString(key, null);
        }
        return res;
    }




    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Log.i("option selected", urls[0]);
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.i("option selected", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private class Vote extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {			//before executing
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {	//do in background while the process is executing
            JSONParser jParser = new JSONParser();					//creates a new JSONParser object
            // Getting JSON from URL
            if(isNetworkAvailable()){								//as long as the network is available
                JSONObject json = null;				//creates a new JSONObject
                try {
                    int cid = prefs.getInt("comid", -1);
                    String token = prefs.getString("access_token", null);
                    if(token!=null)

                        json = jParser.getJSONFromUrl("https://beuntamed.com/app/api/2.0/posts/add_vote", token, "post", args[0], false);
                } catch (IOException | JSONException e) {		//catches specific exceptions
                    return null;
                }

                return json;								//returns the JSON object
            }

            else{
                JSONObject json;
                try {
                    json = new JSONObject(getStringProperty("json"));
                    return json;
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Error Loading Confession",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //TODO: implement proper offline handling. Currently does not handle disconnection from network

                return null;
            }
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            if(json ==null){
                runOnUiThread(new Runnable() {				//Toast message claiming Error with loading the data
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Cannot retrieve post. Please try again later or contact the admins",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }else{

            }


        }}


    private class Comment extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {			//before executing
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {	//do in background while the process is executing
            JSONParser jParser = new JSONParser();					//creates a new JSONParser object
            // Getting JSON from URL
            if(isNetworkAvailable()){								//as long as the network is available
                JSONObject json = null;				//creates a new JSONObject
                try {
                    int cid = prefs.getInt("comid", -1);
                    String token = prefs.getString("access_token", null);
                    if(token!=null)

                        json = jParser.getJSONFromUrl("https://beuntamed.com/app/api/2.0/posts/add_comment", token, args[0] , args[1], args[2], false);
                } catch (IOException | JSONException e) {		//catches specific exceptions
                    return null;
                }

                return json;								//returns the JSON object
            }

            else{
                JSONObject json;
                try {
                    json = new JSONObject(getStringProperty("json"));
                    return json;
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Error Loading Confession",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //TODO: implement proper offline handling. Currently does not handle disconnection from network

                return null;
            }
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            if(json ==null){
                runOnUiThread(new Runnable() {				//Toast message claiming Error with loading the data
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Cannot retrieve post. Please try again later or contact the admins",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                commentlist.clear();
                new JSONParse().execute(pid);
            }


        }}

}
