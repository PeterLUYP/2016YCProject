package com.example.jiarou.sharelove;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.peter.focus.GlobalVariable;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class VenderListFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final static ArrayList<String> vendorTitleList = new ArrayList<>();
    ListView list;
    private MapView mapView;
    String zip_areas;
    String zip_number;
    Button search,home,game,focus,code,my;
    TextView noshop;
    TextView logout;
    String shop_list;
    private GoogleMap mMap;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final static String DB_URL = "https://vendor-5acbc.firebaseio.com/Vendors";
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState){
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        super.onCreate(savedInstanceState);
    }


    public static  VenderListFragment newInstance(){
        Bundle args = new Bundle();

        VenderListFragment fragment = new VenderListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public VenderListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener){
            mListener = (OnFragmentInteractionListener)context;
        }else{
            throw new ClassCastException(context.toString() + "must implement  OnFragmentInteractionListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_vender_list, container, false);
        /**
        Toolbar toolbar =(Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
         **/

        Toolbar my_toolbar= (Toolbar)view.findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(my_toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("首頁");
        setHasOptionsMenu(true);
        //取得searchActivuty的資料





        String get_area=getArguments().getString(zip_areas);

/**
        search =(Button) view.findViewById(R.id.search01);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(getActivity(), SearchActivity.class);

                startActivityForResult(intent, 2);

            }
        });
 **/

        home =(Button) view.findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(getActivity(), IndexActivity.class);

                startActivityForResult(intent,2);

            }


        });

        game =(Button) view.findViewById(R.id.game);
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(getActivity(), GameActivity.class);

                startActivityForResult(intent, 2);

            }
        });
       focus =(Button) view.findViewById(R.id.focus);
        focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(getActivity(), com.example.peter.focus.MainActivity.class);

                startActivityForResult(intent, 2);

            }
        });
      code =(Button) view.findViewById(R.id.code);
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(getActivity(), com.example.chiayi.lovecode.MainActivity.class);
                startActivityForResult(intent, 2);

            }
        });
       my =(Button) view.findViewById(R.id.my);
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent();
                intent.setClass(getActivity(), User_Activity.class);
                startActivityForResult(intent,2);

            }
        });


        //fragment加入地圖頁面
        mapView = (MapView) view.findViewById(R.id.map1);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();// needed to get the map to display immediately

        MapsInitializer.initialize(getActivity());

        mMap = mapView.getMap();
        LatLng nccu = new LatLng(24.9849998, 121.5761281);
        mMap.addMarker(new MarkerOptions().position(nccu).title("Marker in NCCU"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nccu));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        Firebase.setAndroidContext(getActivity());
        final Firebase myFirebaseRef = new Firebase("https://vendor-5acbc.firebaseio.com/Vendors");
        ChildEventListener childEventListener = myFirebaseRef.addChildEventListener(new ChildEventListener() {


            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String Latitude = dataSnapshot.child("Location/Latitude").getValue().toString();
                String Longitude = dataSnapshot.child("Location/Longitude").getValue().toString();


                double location_left = Double.parseDouble(Latitude);
                double location_right = Double.parseDouble( Longitude);
                String name =(String) dataSnapshot.child("Information/Name").getValue();
                LatLng cod = new LatLng( location_left, location_right);
                mMap.addMarker(new MarkerOptions().position(cod).title(name));


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        //測試取得得 zip_areas
       // Toast.makeText(getActivity(), zip_areas, Toast.LENGTH_SHORT).show();


        list = (ListView) view.findViewById(R.id.venderlist_view);
        connectToFirebase();


        return view;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflate) {
        inflate.inflate(R.menu.main_menu, menu);



                super.onCreateOptionsMenu(menu, inflate);


    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case  R.id.logout:
                if(AccessToken.getCurrentAccessToken() == null){
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), Login.class);
                    startActivity(intent);
                }else{
                    GlobalVariable globalVariable = (GlobalVariable) getActivity().getApplicationContext();
                    globalVariable.setUserId(AccessToken.getCurrentAccessToken().getUserId());
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), Login.class);
                            startActivity(intent);

                }

                break;
            case R.id.search:
                final Intent intent = new Intent();
                intent.setClass(getActivity(), SearchActivity.class);

                startActivityForResult(intent, 2);
                break;


        }
        return super.onOptionsItemSelected(item);
    }




    /**登出

     logout = (TextView)view.findViewById(R.id.logoutTextView);

     if(AccessToken.getCurrentAccessToken() == null){
     Intent intent = new Intent();
     intent.setClass(getActivity(), Login.class);
     startActivity(intent);
     }else{
     GlobalVariable globalVariable = (GlobalVariable) getActivity().getApplicationContext();
     globalVariable.setUserId(AccessToken.getCurrentAccessToken().getUserId());
     logout.setText("登出");
     logout.setOnClickListener(new View.OnClickListener() {
    @Override public void onClick(View v) {
    Intent intent = new Intent();
    intent.setClass(getActivity(), Login.class);
    startActivity(intent);
    }
    });
     }
     **/


    public void connectToFirebase() {

                final CustomAdapter adapter = new CustomAdapter(this.getActivity(), vendorTitleList);

                Firebase.setAndroidContext(this.getActivity());

                final Firebase vendor = new Firebase(DB_URL);


                vendor.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (zip_areas == null) {
                            String title = (String) dataSnapshot.child("Information").child("Name").getValue();

                            vendorTitleList.add(title);
                        } else {
                            zip_number = zip_areas.substring(1, 4);
                            if (dataSnapshot.child("Location/ZIP").getValue().toString().equals(zip_number)) {
                        vendorTitleList.add((String) dataSnapshot.child("Information/Name").getValue());
                        shop_list=(String) dataSnapshot.child("Information/Name").getValue();



                    }




                }

               list.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        vendorTitleList.clear();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


    }




    public class CustomAdapter extends BaseAdapter{

        Context c;

        ArrayList<String> vendorTitle;



        public CustomAdapter(Context context, ArrayList<String> vendorTitle){
            c = context;
            this.vendorTitle = vendorTitle;

        }

        @Override
        public int getCount() {
            return vendorTitle.size();
        }

        @Override
        public Object getItem(int position) {
            return vendorTitle.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View list;
            LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            list = inflater.inflate(R.layout.map_listview, null);

            TextView listTextView = (TextView)list.findViewById(R.id.MaplistTextView);


            final String title = vendorTitle.get(position);
            listTextView.setText(title);



            list.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    mListener.onFocusSelected(title);
                }
            });


            return list;
        }
    }




        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);


        void onFocusSelected(String vendorTitle);
    }

    public void getNavigation(String vendorTitle){
        String navigation = vendorTitle;
        final Firebase navigate = new Firebase(DB_URL);
        Query navigates = navigate.orderByChild("Information/Name").equalTo(navigation);

        navigates.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Double navLatitude = (Double)dataSnapshot.child("Location/Latitude").getValue();
                Double navLongitude = (Double)dataSnapshot.child("Location/Longitude").getValue();

                final LatLng navImp = new LatLng(navLatitude, navLongitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(navImp));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

      //fragment連接saerchactivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {//resultCode是剛剛妳A切換到B時設的resultCode
            case 2://當B傳回來的Intent的requestCode 等於當初A傳出去的話

                zip_areas =  data.getExtras().getString("name");
                Toast.makeText(getActivity(), zip_areas, Toast.LENGTH_SHORT).show();

                Bundle bundle=new Bundle();
                bundle.putString(zip_areas, "From Activity");
                //set Fragmentclass Arguments
                VenderListFragment fragobj=new  VenderListFragment();
                fragobj.setArguments(bundle);

                connectToFirebase();

                if(shop_list==null){
                    //textview
                    noshop = (TextView) getView().findViewById(R.id.noshop);
                    noshop.setText("尚無店家");
                }


                zip_number =zip_areas.substring(1,4);
                String  address= "台灣";
                String  address01= address+zip_areas;

                Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                //有問題

                List<Address> addressLocation = null;


                try {

                    addressLocation = geoCoder.getFromLocationName( address01, 1);


                } catch (IOException e) {
                    e.printStackTrace();
                }


                double latitude = addressLocation.get(0).getLatitude();
                double longitude = addressLocation.get(0).getLongitude();

                LatLng area_type = new LatLng(latitude, longitude);


                mMap.moveCamera(CameraUpdateFactory.newLatLng(area_type));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(14));


                /**
                 textview2 = (TextView) this.findViewById(R.id.textView2);
                 textview2.setText(zip_areas);




                 List<Address> addressLocation = null;
                 try {
                 addressLocation = geoCoder.getFromLocationName(zip_areas, 1);
                 } catch (IOException e) {
                 e.printStackTrace();
                 }
                 double latitude = addressLocation.get(0).getLatitude();
                 double longitude = addressLocation.get(0).getLongitude();

                 LatLng area_type = new LatLng(latitude, longitude);
                 mMap.moveCamera(CameraUpdateFactory.newLatLng(area_type));
                 mMap.moveCamera(CameraUpdateFactory.zoomTo(14));

                 ListView list = (ListView) findViewById(R.id.listView);
                 final ArrayAdapter<String> adapter =
                 new ArrayAdapter<String>(this,
                 android.R.layout.simple_list_item_1,
                 android.R.id.text1);
                 list.setAdapter(adapter);

                 final Firebase myFirebaseRef = new Firebase("https://vendor-5acbc.firebaseio.com/Vendors");


                 ChildEventListener childEventListener = myFirebaseRef.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.child("Location/ZIP").getValue().toString().equals(zip_number)) {
                adapter.add((String) dataSnapshot.child("Information/Name").getValue());
                int store_number;
                store_number = ((String) dataSnapshot.child("Information/Name").getValue()).length();
                if (store_number > 0) {
                Toast.makeText(MapsActivity.this, "目前有" + store_number + "筆", Toast.LENGTH_LONG).show();
                } else {
                Toast.makeText(MapsActivity.this, "您沒有選擇任何項目" + store_number, Toast.LENGTH_LONG).show();
                }
                }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
                });
               **/




                 break;

                 }
                 }

}
