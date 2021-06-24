package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,MapView.MapViewEventListener, MapView.POIItemEventListener {
    Toolbar toolbar;
    String UserName;
    String UserEmail;
    String loginID;
    MapView mapView;
    ViewGroup mapViewContainer;
    RecyclerView recyclerView;
    SearchView searchView;
    private GoogleSignInClient mGoogleSignInClient;
    ArrayList<KaKao> regionlist = new ArrayList<>();
    Adapter adapter;

    private String SearchName;
    private double SearchLat=-1;
    private double SearchLng=-1;
    MapPOIItem marker = new MapPOIItem();
    ChatRoom chatroom =new ChatRoom();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        recyclerView = findViewById(R.id.map_recyclerview);


        adapter = new Adapter(regionlist, getApplicationContext(), searchView, recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        //카카오지도
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);


        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        //프래그먼트

        recyclerView = findViewById(R.id.map_recyclerview);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        //헤더 뷰의 객체 초기화
        ImageView loginImage = headerView.findViewById(R.id.loginImage);
        TextView navLoginID = headerView.findViewById(R.id.loginID);

        //ID와 이미지를 로그인된 ID와 이미지로 바꾸기
        navLoginID.setText("반갑습니다 " + UserName + "님");

        //로그인 ID가 있으면 커뮤니케이트 메뉴가 보이게 설정
        if(loginID != null) {
            navigationView.getMenu().findItem(R.id.communicate).setVisible(true);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        getToken(); // FCM 토큰
    }
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navLoginID = headerView.findViewById(R.id.loginID);
        TextView email=headerView.findViewById((R.id.loginStr));
        System.out.println("시작");
        if(account !=null){
            UserName=account.getDisplayName();
            UserEmail=account.getEmail();
            navLoginID.setText("반갑습니다 " + UserName + "님");
            email.setText(UserEmail);
            System.out.println(UserName);
            System.out.println(UserEmail);
        }
        else{
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    private static final String TAG ="";

                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        System.out.println(msg);
                    }
                });
    }
    private void signOut() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    //액션바 검색
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("아파트, 지역, 학교명");
        adapter.setText(searchView);
        final KaKao[] document1 = {null};
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchName= document1[0].getPlaceName();
                SearchLng=Double.parseDouble(document1[0].getX());
                SearchLat=Double.parseDouble(document1[0].getY());
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(SearchLat, SearchLng), true);
                MapPoint mapPoint= MapPoint.mapPointWithGeoCoord(SearchLat,SearchLng);
                marker.setItemName(SearchName);
                marker.setTag(10000);
                MapPoint mapPoint1 = MapPoint.mapPointWithGeoCoord(SearchLat, SearchLng);

                marker.setMapPoint(mapPoint1);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                //마커 드래그 가능하게 설정
                marker.setDraggable(true);
                mapView.addPOIItem(marker);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                recyclerView.setVisibility(View.VISIBLE);
                if (query.length() >= 1) {
                    regionlist.clear();
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    KaKaoRetrofit retrofit = new KaKaoRetrofit();
                    Call<CategoryResult> call = retrofit.kakaoapi.getSearchLocation(getString(R.string.restapi_key), query, 15);
                    call.enqueue(new Callback<CategoryResult>() {
                        @Override
                        public void onResponse(@NotNull Call<CategoryResult> call, @NotNull Response<CategoryResult> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                for (KaKao document : response.body().getDocuments()) {
                                    document1[0] =document;
                                    adapter.addItem(document);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<CategoryResult> call, Throwable t) {

                        }
                    });
                } else {
                    if (query.length() <= 0) {
                        recyclerView.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

  @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int selectedItemId = menuItem.getItemId();

      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      if (selectedItemId == R.id.nav_home) {
            Toast.makeText(this, "관심메뉴", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Arealist.class);
            startActivity(intent);
        } else if (selectedItemId == R.id.nav_gallery) {
            Toast.makeText(this, "공인 중개사 등록", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Register.class);
            intent.putExtra("UserName", UserName);
            intent.putExtra("Email",UserEmail);
            startActivity(intent);
        } else if (selectedItemId == R.id.nav_slideshow) {
            Toast.makeText(this, "채팅", Toast.LENGTH_SHORT).show();
            mapView.setVisibility(View.GONE);
            transaction.replace(R.id.container, chatroom).commit();
        } else if (selectedItemId == R.id.nav_tools) {
            Toast.makeText(this, "홈", Toast.LENGTH_SHORT).show();
            mapView.setVisibility(View.VISIBLE);
        }else if (selectedItemId == R.id.nav_logout) {
            Toast.makeText(this, "다섯 번째 메뉴 선택", Toast.LENGTH_SHORT).show();
            signOut();
        }

        //드로어 닫기
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    } //onNavigationItemSelected()



    //뒤로가기 버튼이 눌렸을때 작동하는 메서드
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) { // 드로어가 열려있다면
            drawerLayout.closeDrawer(GravityCompat.START);   // 드로어를 닫는다.
        } else {
            super.onBackPressed();
        }
    } //onBackPressed()

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}