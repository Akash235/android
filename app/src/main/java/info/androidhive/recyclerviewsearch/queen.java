package info.androidhive.recyclerviewsearch;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class queen extends AppCompatActivity {
    private Handler mHandler = new Handler();

    private final int SPEECH_RECOGNITION_CODE = 1;
    private TextView txtOutput;
    private ImageButton btnMicrophone;
    private Button chill;
    TextToSpeech t1;
    private TextToSpeech tts;
    Random r = new Random();
    String hate="";
    int i1 = r.nextInt(5 - 1) + 1;

    String person = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queen);





        //Intent myIntent = new Intent(MainActivity.this, MeAkash.class);
        //startService(myIntent);

        txtOutput = (TextView) findViewById(R.id.txt_output);
        btnMicrophone = (ImageButton) findViewById(R.id.btn_mic);
        chill =(Button)findViewById(R.id.button);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }


                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }

            private void speak(String text) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                }else{
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });



        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });



        btnMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();

                Thread t = new Thread(){
                    @Override
                    public void run() {
                        try {
                            Socket s = new Socket("192.168.0.192", 9009);
                            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                            dos.writeUTF(txtOutput.getText().toString());

                            String toSpeak = txtOutput.getText().toString();

                            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);


                            //read input stream
                            DataInputStream dis2 = new DataInputStream(s.getInputStream());
                            InputStreamReader disR2 = new InputStreamReader(dis2);
                            BufferedReader br = new BufferedReader(disR2);//create a BufferReader object for input

                            //print the input to the application screen


                            dis2.close();
                            s.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();

            }
        });



    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    txtOutput.setText(text);

                    recognition(text);


                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            startSpeechToText();

                        }
                    }, 2000);





                    // send data to the server

                }
                break;
            }
        }
    }



    private void recognition(String text) {



        switch (text){
            case "hello":
                String sentense = "hi darling:Hello mam:Hey:HI:mam";
                String[] list = sentense.split(":");

                speak(list[i1]);
                break;
            case "hi":
                String sentense1 = "hello mam:whats going on dear:how was your day today:what can i do mam";
                String[] list1 = sentense1.split(":");

                speak(list1[i1]);
                break;
            case "how you doing":

                String sentense2 = "Fine:Wonderful what about you mam:exhausted:marvelous:nothing like before";
                String[] list2 = sentense2.split(":");


                speak(list2[i1]);
                break;
            case "good morning":
                String sentense3 = "Morning:Morning mam:today is wonderful day:its early:Good morning";
                String[] list3 = sentense3.split(":");

                speak(list3[i1]);
                break;
            case "who are you":
                speak("I am Artificial intelligence made by Akash Sir");
                break;
            case "queen":
                String sentense4 = "Yes mam:is their anything i can do for you:Yes:its early:anything for you mam";
                String[] list4 = sentense4.split(":");

                speak(list4[i1]);
                break;
            case "what are you doing":
                speak("now i am running system Sir");
                break;
            case "how are you":
                speak("i am fine ");
                break;
            case "what is time":
                String currentdt = DateFormat.getDateInstance().format(new Date());

                speak("current time is " + currentdt);
                break;
            case "Akash":


                speak("Akash sir is smart devoloper who is technicial in shopforidea company");
                break;

            case "status":
                speak("I am working properly Sir ");
                break;
            case "status of company":
                speak("Sir search engin optimization is not working properly but the good news is company have some project to work on ");
                break;
            case "task of Shubham":
                speak("Shubham you will complete the timetable project ");
                break;

            case "task of Adarsh":
                speak("Adarsh remember talkd website clone it completely ");
                break;


            case "task of Rishikesh":
                speak("Rushikesh Akash sir will tell your project");
                break;


            case "I need to eat something":
                speak("Sir Openning nearest restaurants for you");

                Uri gmmIntentUri = Uri.parse("geo:0,0?q=restaurants");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        speak("did you like the locations Sir");

                    }
                }, 5000);



                break;


            default:
                String test = "Mirzya";
                String[] words = text.split(" ");

                emotion(words);

                for (int i=0;i<words.length;i++){
                    if(words[i].equals("play")){
                        for (int j=0;j<words.length;j++) {
                            if(words[j].equals("song")){
                                Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
                                intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS,
                                        MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE);
                                intent.putExtra(MediaStore.EXTRA_MEDIA_ALBUM, test);
                                intent.putExtra(SearchManager.QUERY, test);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }


                            }
                            speak("what song you want to play");

                        }

                    }


                    if(words[i].equals("laptop")){
                        for (int j=0;j<words.length;j++) {
                            if(words[j].equals("hack")){
                                speak("On my way sir please tell me laptops ip address");

                                new MaterialDialog.Builder(queen.this)
                                        .title("shopforidea input test")
                                        .content("Enter your password")
                                        .inputType(InputType.TYPE_CLASS_TEXT)
                                        .input("password is 1 to 6", "1to6", new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                                // Do something

                                                String t = input.toString();
                                                fetchContacts(t);

                                                new MaterialDialog.Builder(queen.this)
                                                        .title("Access granted")
                                                        .content(input)
                                                        .show();


                                            }
                                        }).show();



                            }


                        }

                    }
//
//                    contexual sentense



                    if(Character.isUpperCase(words[i].charAt(0))){
                        person = words[i];
                        for (int j=0;j<words.length;j++) {
                            if(words[j].equals("hack")){
                                speak("On my way sir please tell me laptops ip address");




                            }


                        }

                    }

                    if(words[i].equals("")){
                        for (int j=0;j<words.length;j++) {
                            if(words[j].equals("hack")){
                                speak("On my way sir please tell me laptops ip address");




                            }


                        }

                    }




                    else if(words[i].equals("send")){
                        for (int j=0;j<words.length;j++) {
                            if(words[j].equals("sms")){
                                String message="hello";
                                Intent intent4 = new Intent(Intent.ACTION_SEND);
                                intent4.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
                                intent4.putExtra("sms_body", message);
                                if (intent4.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent4);

                                }

                            }


                            else if (words[j].equals("email")){

                                String address = text.substring(text.indexOf("to"));
                                String[] addresses=address.split(" ",2);


                                Intent intent5 = new Intent(Intent.ACTION_SEND);
                                intent5.setType("*/*");
                                intent5.putExtra(Intent.EXTRA_EMAIL, addresses[1]);
                                if (intent5.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent5);
                                }

                            }


                        }

                        speak("what you want to send");

                    }
                    else if (words[i].equals("search")) {
                        String address =text.substring(text.indexOf("search"));
                        String[] address1=address.split(" ",2);
                        Intent intent7=new Intent(Intent.ACTION_WEB_SEARCH);
                        intent7.putExtra(SearchManager.QUERY, address1[1]);
                        startActivity(intent7);


                    } else if (words[i].equals("take")) {
                        final int REQUEST_IMAGE_CAPTURE = 1;
                        final Uri mLocationForPhotos = null;
                        String targetFilename = "akash";
                        for (int j = 0; j < words.length; j++) {
                            if (words[j].equals("photo")) {
                                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (intent1.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(intent1, REQUEST_IMAGE_CAPTURE);
                                }


                            } else if (words[j].equals("video")) {
                                Intent intent2 = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
                                if (intent2.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(intent2, REQUEST_IMAGE_CAPTURE);
                                }

                            }


                        }


                    } else if (words[i].equals("timer")) {
                        float seconds = 30;
                        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                                .putExtra(AlarmClock.EXTRA_MESSAGE, "hello")
                                .putExtra(AlarmClock.EXTRA_LENGTH, seconds)
                                .putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }


                    } else if (words[i].equals("call")) {
                        final int REQUEST_SELECT_CONTACT = 1;
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_SELECT_CONTACT);

                        }


                    }


                }



                break;

        }


    }

    private void emotion(String[] words) {
        String sad = "abandoned\n" +
                "achy\n" +
                "afraid\n" +
                "agitated\n" +
                "agony\n" +
                "alone\n" +
                "anguish\n" +
                "antisocial\n" +
                "anxious\n" +
                "breakdown\n" +
                "brittle\n" +
                "broken\n" +
                "catatonic\n" +
                "consumed\n" +
                "crisis\n" +
                "crushed\n" +
                "crying\n" +
                "defeated\n" +
                "defensive\n" +
                "dejected\n" +
                "demoralized\n" +
                "desolate\n" +
                "despair\n" +
                "desperate\n" +
                "despondent\n" +
                "devastated\n" +
                "discontented\n" +
                "disheartened\n" +
                "dismal\n" +
                "distractable\n" +
                "distraught\n" +
                "distressed\n" +
                "doomed\n" +
                "dreadful\n" +
                "dreary\n" +
                "edgy\n" +
                "emotional\n" +
                "empty\n" +
                "excluded\n" +
                "exhausted\n" +
                "exposed\n" +
                "fatalistic\n" +
                "forlorn\n" +
                "fragile\n" +
                "freaking\n" +
                "gloomy\n" +
                "grouchy\n" +
                "helpless\n" +
                "hopeless\n" +
                "hurt\n" +
                "inadequate\n" +
                "inconsolable\n" +
                "injured\n" +
                "insecure\n" +
                "irrational\n" +
                "irritable\n" +
                "isolated\n" +
                "lonely\n" +
                "lousy\n" +
                "low\n" +
                "melancholy\n" +
                "miserable\n" +
                "moody\n" +
                "morbid\n" +
                "needy\n" +
                "nervous\n" +
                "nightmarish\n" +
                "oppressed\n" +
                "overwhelmed\n" +
                "pain\n" +
                "paranoid\n" +
                "pessimistic\n" +
                "reckless\n" +
                "rejected\n" +
                "resigned\n" +
                "sadness\n" +
                "self-conscious\n" +
                "self-disgust\n" +
                "shattered\n" +
                "sobbing\n" +
                "sorrowful\n" +
                "suffering\n" +
                "suicidal\n" +
                "tearful\n" +
                "touchy\n" +
                "trapped\n" +
                "uneasy\n" +
                "unhappy\n" +
                "unhinged\n" +
                "unpredictable\n" +
                "upset\n" +
                "vulnerable\n" +
                "wailing\n" +
                "weak\n" +
                "weepy\n" +
                "withdrawn\n" +
                "woeful\n" +
                "wounded\n" +
                "wretched\n";
        String hate = "abusive\n" +
                "adulterous\n" +
                "alcoholic\n" +
                "angry\n" +
                "annoying\n" +
                "argumentative\n" +
                "arrogant\n" +
                "at fault\n" +
                "atrocious\n" +
                "awful\n" +
                "backstabbing\n" +
                "bad\n" +
                "bat-shit crazy\n" +
                "beyond reproach\n" +
                "bitchy\n" +
                "bitter\n" +
                "boring\n" +
                "brainless\n" +
                "calculating\n" +
                "careless\n" +
                "caught\n" +
                "caught red-handed\n" +
                "caught-in-the-act\n" +
                "chauvinistic\n" +
                "cheap\n" +
                "cheating\n" +
                "childish\n" +
                "cold\n" +
                "cold-hearted\n" +
                "common\n" +
                "complicated\n" +
                "confrontational\n" +
                "conniving\n" +
                "contemptible\n" +
                "controlling\n" +
                "corrupt\n" +
                "cowardly\n" +
                "crappy\n" +
                "crazed\n" +
                "crazy\n" +
                "creepy\n" +
                "criminal\n" +
                "cruel\n" +
                "cruel-hearted\n" +
                "crummy\n" +
                "crushing\n" +
                "cursed\n" +
                "deceitful\n" +
                "deceiving\n" +
                "deplorable\n" +
                "depressing\n" +
                "dickish\n" +
                "dimwitted\n" +
                "dirty\n" +
                "disappointing\n" +
                "disgraceful\n" +
                "disgusting\n" +
                "dishonest\n" +
                "distressed\n" +
                "disturbed\n" +
                "disturbing\n" +
                "double-crossing\n" +
                "drunk\n" +
                "dull\n" +
                "dumb\n" +
                "eccentric\n" +
                "egotistical\n" +
                "embarrassing\n" +
                "embittered\n" +
                "emotional\n" +
                "empty\n" +
                "evil\n" +
                "exploiting\n" +
                "f'ed up\n" +
                "fake\n" +
                "false\n" +
                "fat\n" +
                "flawed\n" +
                "foolish\n" +
                "forgetful\n" +
                "freak\n" +
                "freeloading\n" +
                "friendless\n" +
                "fugly\n" +
                "full of rage\n" +
                "furious\n" +
                "gold-digging\n" +
                "gossipy\n" +
                "greedy\n" +
                "gross\n" +
                "grouchy\n" +
                "guilty\n" +
                "halfwitted\n" +
                "harmful\n" +
                "hate\n" +
                "hateful\n" +
                "heartbreaking\n" +
                "heinous\n" +
                "hellish\n" +
                "hideous\n" +
                "horrible\n" +
                "humiliating\n" +
                "hurtful\n" +
                "hurting\n" +
                "idiotic\n" +
                "ignorant\n" +
                "ill-tempered\n" +
                "immature\n" +
                "immoral\n" +
                "impatient\n" +
                "in denial\n" +
                "in the wrong\n" +
                "inadequate\n" +
                "inappropriate\n" +
                "inexcusable\n" +
                "infuriated\n" +
                "insane\n" +
                "insecure\n" +
                "insensitive\n" +
                "insincere\n" +
                "irate\n" +
                "irrational\n" +
                "irresponsible\n" +
                "irritating\n" +
                "jealous\n" +
                "lame\n" +
                "lazy\n" +
                "livid\n" +
                "loose\n" +
                "lost\n" +
                "lousy\n" +
                "low\n" +
                "lowlife\n" +
                "lying\n" +
                "mad\n" +
                "malicious\n" +
                "maniacal\n" +
                "manipulating\n" +
                "manipulative\n" +
                "mean\n" +
                "mental\n" +
                "miserable\n" +
                "mistaken\n" +
                "moody\n" +
                "moronic\n" +
                "narrow-minded\n" +
                "nasty\n" +
                "naughty\n" +
                "nauseating\n" +
                "no-good\n" +
                "obnoxious\n" +
                "offensive\n" +
                "out-of-control\n" +
                "out-of-line\n" +
                "outraged\n" +
                "painful\n" +
                "pathetic\n" +
                "pea-brained\n" +
                "pissed\n" +
                "pissed off\n" +
                "pointless\n" +
                "promiscuous\n" +
                "psycho\n" +
                "pushy\n" +
                "rabid\n" +
                "racist\n" +
                "reckless\n" +
                "reprehensible\n" +
                "repulsive\n" +
                "resentful\n" +
                "ridiculous\n" +
                "rotten\n" +
                "rude\n" +
                "sad\n" +
                "saddened\n" +
                "sadistic\n" +
                "scared\n" +
                "screwed-up\n" +
                "self-absorbed\n" +
                "self-centered\n" +
                "self-consumed\n" +
                "self-entitled\n" +
                "self-inflated\n" +
                "selfish\n" +
                "shady\n" +
                "shallow\n" +
                "shameful\n" +
                "shameless\n" +
                "shitty\n" +
                "sick\n" +
                "sickened\n" +
                "silly\n" +
                "sleazy\n" +
                "slutty\n" +
                "smelly\n" +
                "smutty\n" +
                "sneaky\n" +
                "sorry\n" +
                "spiteful\n" +
                "spoiled\n" +
                "stealing\n" +
                "stinky\n" +
                "stupid\n" +
                "superficial\n" +
                "swindling\n" +
                "tasteless\n" +
                "terrible\n" +
                "territorial\n" +
                "thick\n" +
                "thieving\n" +
                "thoughtless\n" +
                "ticked off\n" +
                "tiny-dick\n" +
                "trashy\n" +
                "troubled\n" +
                "twisted\n" +
                "two-dimensional\n" +
                "two-faced\n" +
                "ugly\n" +
                "unacceptable\n" +
                "unapologetic\n" +
                "undependable\n" +
                "underhanded\n" +
                "unethical\n" +
                "unfair\n" +
                "unforgiving\n" +
                "ungrateful\n" +
                "unhappy\n" +
                "unjustifiable\n" +
                "unlovable\n" +
                "unreliable\n" +
                "unthoughtful\n" +
                "untrue\n" +
                "untruthful\n" +
                "unworthy\n" +
                "useless\n" +
                "vacuous\n" +
                "vengeful\n" +
                "verbally abusive\n" +
                "vindictive\n" +
                "violent\n" +
                "weak\n" +
                "weird\n" +
                "whiny\n" +
                "white trash\n" +
                "wicked\n" +
                "witless\n" +
                "worthless\n" +
                "wrapped up in yourself\n" +
                "wretched\n" +
                "wrong";
        String happy="amused\n" +
                "beaming\n" +
                "blissful\n" +
                "blithe\n" +
                "buoyant\n" +
                "carefree\n" +
                "cheerful\n" +
                "cheery\n" +
                "chipper\n" +
                "chirpy\n" +
                "content\n" +
                "contented\n" +
                "delighted\n" +
                "ebullient\n" +
                "ecstatic\n" +
                "elated\n" +
                "enraptured\n" +
                "euphoric\n" +
                "exhilarated\n" +
                "exultant\n" +
                "funny\n" +
                "glad\n" +
                "gleeful\n" +
                "gratified\n" +
                "grinning\n" +
                "happy\n" +
                "in a good mood\n" +
                "in good spirits\n" +
                "in seventh heaven\n" +
                "invigorated\n" +
                "jocular\n" +
                "jolly\n" +
                "jovial\n" +
                "joyful\n" +
                "joyous\n" +
                "jubilant\n" +
                "lighthearted\n" +
                "merry\n" +
                "mirthful\n" +
                "never been better\n" +
                "on cloud nine\n" +
                "on top of the world\n" +
                "optomistic\n" +
                "overjoyed\n" +
                "over-the-moon\n" +
                "pleased\n" +
                "radiant\n" +
                "rapturous\n" +
                "satisfied\n" +
                "smiling\n" +
                "sunny\n" +
                "thrilled\n" +
                "tickled pink\n" +
                "untroubled\n" +
                "upbeat\n" +
                "walking on air\n";
        String[] hate1 = hate.split("\n");
        String[] sad1 = sad.split("\n");
        String[] happy1 = happy.split("\n");





        for(int i=0;i<words.length;i++){
            for (int j=0;j<hate1.length;j++){
                if (words[i].equals(hate1[j])){
                    String res = "But why:Why:why is that:Why";
                    String[] res1 = res.split(":");
                    speak(res1[i1]);

                }
            }

            for (int k=0;k<sad1.length;k++){
                if (words[i].equals(sad1[k])){
                    String res = "But why:Why:why is that:Why";
                    String[] res1 = res.split(":");
                    speak(res1[i1]);

                }
            }

            for (int l=0;l<happy1.length;l++){
                if (words[i].equals(happy1[l])){
                    String res = "But why:Why:why is that:Why";
                    String[] res1 = res.split(":");
                    speak(res1[i1]);

                }
            }


        }

    }

    private void speak(String s) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    private void fetchContacts(String t) {
        String URL = "http://"+t+":3000/hack";

        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            String ret = "";

                            try {
                                FileInputStream fileIn=openFileInput("myText.txt");

                                InputStreamReader InputRead=new InputStreamReader(fileIn);
                                BufferedReader bufferedReader = new BufferedReader(InputRead);
                                String receiveString = "";
                                StringBuilder stringBuilder = new StringBuilder();
                                while ((receiveString = bufferedReader.readLine()) != null){
                                    stringBuilder.append(receiveString);

                                }
                                InputRead.close();

                                Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
//
                        List<Contact> items = new Gson().fromJson(response.toString(), new TypeToken<List<Contact>>() {
                        }.getType());

                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();


                        // adding contacts to contacts list


                        try {
                            FileOutputStream fileOut= openFileOutput("myText.txt",MODE_PRIVATE);
                            OutputStreamWriter outputWriter=new OutputStreamWriter(fileOut);
                            outputWriter.write(response.toString());
                            outputWriter.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json



                Toast.makeText(getApplicationContext(), "You are now offline to go online reload again ", Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }


}
