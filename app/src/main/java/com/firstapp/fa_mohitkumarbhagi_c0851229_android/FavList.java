package com.firstapp.fa_mohitkumarbhagi_c0851229_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.List;

public class FavList extends Fragment implements AdapterView.OnItemClickListener {

    DatabaseHelper databaseHelper;

    List<AddressBean> addressBeanList;

    CustomAdapter ca;

    ListView lv;

    MyAdapter ma;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fav_list, container, false);

        lv = v.findViewById(R.id.lv);

        databaseHelper=new DatabaseHelper(getActivity());

        addressBeanList=databaseHelper.getAllFavPlaces();
        ca=new CustomAdapter(getActivity(), addressBeanList);
        lv.setAdapter(ca);

        SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(lv),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {

                                AddressBean ab = addressBeanList.get(position);

                                databaseHelper.deleteFavPlace(ab.getId());

                                ca.remove(position);
                            }
                        });

        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {

                    touchListener.undoPendingDismiss();

                    AddressBean ab = addressBeanList.get(position);

                    Intent intent = new Intent(getActivity(), UpdatefavPlace.class);
                    intent.putExtra("id", String.valueOf(ab.getId()));
                    intent.putExtra("placename", ab.getPlacename());
                    intent.putExtra("address", ab.getAddress());
                    intent.putExtra("latitude", ab.getLatitude());
                    intent.putExtra("longitude", ab.getLongitude());
                    intent.putExtra("visited", ab.getVisited());
                    intent.putExtra("createdon", ab.getCreatedon());
                    startActivity(intent);

                } else {
                    AddressBean ab = addressBeanList.get(position);

                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("id", String.valueOf(ab.getId()));
                    intent.putExtra("placename", ab.getPlacename());
                    intent.putExtra("address", ab.getAddress());
                    intent.putExtra("latitude", ab.getLatitude());
                    intent.putExtra("longitude", ab.getLongitude());
                    intent.putExtra("visited", ab.getVisited());
                    intent.putExtra("createdon", ab.getCreatedon());
                    startActivity(intent);
                }
            }
        });

//        lv.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        AddressBean ab = addressBeanList.get(i);

        Intent intent = new Intent(getActivity(), MapsActivity.class);
        intent.putExtra("id", String.valueOf(ab.getId()));
        intent.putExtra("placename", ab.getPlacename());
        intent.putExtra("address", ab.getAddress());
        intent.putExtra("latitude", ab.getLatitude());
        intent.putExtra("longitude", ab.getLongitude());
        intent.putExtra("visited", ab.getVisited());
        intent.putExtra("createdon", ab.getCreatedon());
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();

        addressBeanList=databaseHelper.getAllFavPlaces();
        ca=new CustomAdapter(getActivity(), addressBeanList);
        lv.setAdapter(ca);

        SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(lv),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {

                                AddressBean ab = addressBeanList.get(position);

                                databaseHelper.deleteFavPlace(ab.getId());

                                ca.remove(position);
                            }
                        });

        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {

                    touchListener.undoPendingDismiss();

                    AddressBean ab = addressBeanList.get(position);

                    Intent intent = new Intent(getActivity(), UpdatefavPlace.class);
                    intent.putExtra("id", String.valueOf(ab.getId()));
                    intent.putExtra("placename", ab.getPlacename());
                    intent.putExtra("address", ab.getAddress());
                    intent.putExtra("latitude", ab.getLatitude());
                    intent.putExtra("longitude", ab.getLongitude());
                    intent.putExtra("visited", ab.getVisited());
                    intent.putExtra("createdon", ab.getCreatedon());
                    startActivity(intent);

                } else {
                    AddressBean ab = addressBeanList.get(position);

                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("id", String.valueOf(ab.getId()));
                    intent.putExtra("placename", ab.getPlacename());
                    intent.putExtra("address", ab.getAddress());
                    intent.putExtra("latitude", ab.getLatitude());
                    intent.putExtra("longitude", ab.getLongitude());
                    intent.putExtra("visited", ab.getVisited());
                    intent.putExtra("createdon", ab.getCreatedon());
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}

