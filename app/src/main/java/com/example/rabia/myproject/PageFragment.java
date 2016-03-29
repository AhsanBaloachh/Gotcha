package com.example.rabia.myproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;

/**
 * Created by rabia on 17-Apr-15.
 */
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    ArrayList<Person> listContact;
    ArrayList<Person> orig;
    SwipeRefreshLayout swipeRefreshLayout;
    SwipeRefreshLayout swipeRefreshLayout2;
    FriendAdapter adpterHome ;
    FriendAdapter adpter2 ;
    boolean first=true;
    public static PageFragment newInstance(int page)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);

        listContact = GlobalVariables.getListContacts();
        final ArrayList<Person> friendList = new ArrayList<>();

//        Person p1 = new Person("Ahsan Shahid","Karim Block","abc@gmail.com","1",friendList);
//        Person p2=new Person("Zain Haider","Karim Block","abc@gmail.com","2",friendList);
//        Person p3=new Person("Rabia Athar","Karim Block","abc@gmail.com","3",friendList);
//        Person p4 = new Person("Amna Khan","Karim Block","abc@gmail.com","4",friendList);
//        Person p5=new Person("Ali Ahmed","Karim Block","abc@gmail.com","5",friendList);
//        Person p6=new Person("Hassan Daniyal","Karim Block","abc@gmail.com","6",friendList);
//        Person p7 = new Person("Asad Shezad","Karim Block","abc@gmail.com","7",friendList);
//        Person p8=new Person("Maeium Ali","Karim Block","abc@gmail.com","8",friendList);
//        Person p9=new Person("Wasim Daniyal","Karim Block","abc@gmail.com","9",friendList);
//        p1.set_lastSeen("11:53");
//        p2.set_lastSeen("4 Feb 2015");
//        p3.set_lastSeen("1 March 2015");
//        p4.set_lastSeen("5 Jan 2015");
//        p5.set_lastSeen("12:12");
//        p6.set_lastSeen("23 May 2015");
//        p7.set_lastSeen("14 Feb 2015");
//        p8.set_lastSeen("2 Jan 2015");
//        p9.set_lastSeen("67 Jan 2015");
//
//        listContact.add(p1);
//        listContact.add(p2);
//        listContact.add(p3);
//        listContact.add(p4);
//        listContact.add(p5);
//        listContact.add(p6);
//        listContact.add(p7);
//        listContact.add(p8);
//        listContact.add(p9);
        if(first) {

first =false;
        }
    }
    public static TextView place;
    public static GoogleMap map;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(mPage==1) {
            final View rootView = inflater.inflate(R.layout.home, container, false);
            GlobalVariables.setmapActivity(rootView);
            place=(TextView) rootView.findViewById(R.id.currentLocation);
           // map =  ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Setting Swipe to refresh
             swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
            swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
                                                         @Override
                                                         public void onRefresh() {
                                                             if(GlobalVariables.getConnectivity()) {
                                                                 Toast.makeText(rootView.getContext(), "Refreshing", Toast.LENGTH_LONG).show();
                                                                 (new AsyncTask<Void, Void, Void>() {
                                                                     @Override
                                                                     protected Void doInBackground(Void... params) {
                                                                         try {
                                                                             //Thread.sleep(2000);
                                                                             GlobalVariables.GenTrusted();
                                                                         } catch (Exception e) {
                                                                             e.printStackTrace();
                                                                         }

                                                                         return null;
                                                                     }

                                                                     @Override
                                                                     protected void onPostExecute(Void params) {
                                                                         super.onPostExecute(params);
                                                                         swipeRefreshLayout.setRefreshing(false);
                                                                         adpterHome.notifyDataSetChanged();
                                                                         Toast.makeText(rootView.getContext(), "List updated", Toast.LENGTH_LONG).show();

                                                                     }
                                                                 }).execute();
                                                             }
                                                             else
                                                             {
                                                                 Toast.makeText(rootView.getContext(), "Check you internet connection", Toast.LENGTH_LONG).show();
                                                                 swipeRefreshLayout.setRefreshing(false);
                                                             }

                                                         }
                                                     });


            //------

            ListView lv = (ListView) rootView.findViewById(R.id.trustedFriends);
            adpterHome =new FriendAdapter(getActivity(), R.layout.friends_row, GlobalVariables.getTrustedList(), true);
            lv.setAdapter(adpterHome);

            AdapterView.OnItemClickListener rowListener = new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView parent, View v, int position, long id)
                {
                    Context context = rootView.getContext();
                    Intent intent=new Intent(context,PersonDetails.class);
                    intent.putExtra("Name",GlobalVariables.getTrustedList().get(position).getName());
                    intent.putExtra("Place",GlobalVariables.getTrustedList().get(position).getLocation());
                   if(GlobalVariables.getTrustedList().get(position).getCordinates()!=null)
                   {
                       intent.putExtra("lati", GlobalVariables.getTrustedList().get(position).getCordinates().getLatitude());
                       intent.putExtra("longi", GlobalVariables.getTrustedList().get(position).getCordinates().getLongitude());
                   }
                    intent.putExtra("lastseen",GlobalVariables.getTrustedList().get(position).get_lastSeen());
                    startActivity(intent);
                }
            };
            lv.setOnItemClickListener(rowListener);


                //((Button) rootView.findViewById(R.id.directions)).setOnClickListener(new View.OnClickListener() {
            //    @Override
                //public void onClick(View v) {
                  //  adpterHome.notifyDataSetChanged();


                 //   String geoUri="http://maps.google.com/maps?addr="+source.latitude+","+source.longitude+ "&daddr="+destination.latitude+","+destination.longitude;
                   // Intent mapCall = new Intent (Intent.ACTION_VIEW, Uri.parse(geoUri));
                    //startActivity(mapCall);
//                }
  //          });

            return rootView;
        }

        else
        {
        final View view = inflater.inflate(R.layout.fragment_page, container, false);

        Button btn = (Button) view.findViewById(R.id.sampleText);
        btn.setText("Fragment #" + String.valueOf( mPage) +" Tap to open Map");
        final View.OnClickListener listener=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast toast=Toast.makeText(view.getContext(), "Clicked", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent i=new Intent(view.getContext(),MapActivity.class);
                    startActivity(i);
                }
            };
        btn.setOnClickListener(listener);
            ListView lv = (ListView) view.findViewById(R.id.friendList);
            EditText searchText = (EditText) view.findViewById(R.id.search);

            orig = new ArrayList<>();
           GlobalVariables.SOrt(listContact);
            orig.addAll(listContact);

            searchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s==null)
                        listContact = orig;
                    if(s.length()==0)
                        listContact.addAll(orig);
                    listContact.clear();
                    for (int i=0 ; i<orig.size();i++)
                    {
                        if(orig.get(i).get_phoneNo().toLowerCase().contains(s.toString().toLowerCase()) ||orig.get(i).getName().toLowerCase().contains(s.toString().toLowerCase()) )
                        {
                            listContact.add(orig.get(i));
                        }
                    }
                    adpter2.notifyDataSetChanged();
                }
            });


           adpter2= new FriendAdapter(getActivity(), R.layout.friends_row, GlobalVariables.getListContacts(),false);
            lv.setAdapter(adpter2);

            swipeRefreshLayout2 = (SwipeRefreshLayout) view.findViewById(R.id.listRefresh);
            swipeRefreshLayout2.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                     if(GlobalVariables.getConnectivity())
                    {
                        Toast.makeText(view.getContext(),"Refreshing",Toast.LENGTH_LONG).show();

                        (new AsyncTask<Void,Void,Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            try {

                                        GlobalVariables.GenListContact(view.getContext());
                                         GlobalVariables.setAppUsers();
                            }
                            catch (Exception e){

                            }
                            //  GlobalVariables.GenTrusted();
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void params) {
                            super.onPostExecute(params);
                            adpter2.notifyDataSetChanged();
                            swipeRefreshLayout2.setRefreshing(false);
                          //  if(GlobalVariables.getConnectivity())

                            Toast.makeText(view.getContext(), "List updated", Toast.LENGTH_LONG).show();

                        }
                    }).execute();
                }
                    else {
                         Toast.makeText(view.getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                         swipeRefreshLayout2.setRefreshing(false);
                     }
                }
            });



            return view;

        }

    }
}

