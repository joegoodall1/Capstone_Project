package com.getstrength.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Joe on 10/03/2016.
 */
public class HistoryFragment extends ListFragment implements AdapterView.OnItemClickListener {

    class StringDateComparator implements Comparator<String> {
        SimpleDateFormat dateFormat = new SimpleDateFormat(EditorActivity.DATE_FORMAT);

        public int compare(String lhs, String rhs) {
            try {
                return dateFormat.parse(lhs).compareTo(dateFormat.parse(rhs));
            } catch (Exception exception) {
                return 0;
            }
        }
    }

    private List<String> mExercises;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ExerciseStore.initialise(getContext());
        refresh();
    }

    private void refresh() {
        mExercises = ExerciseStore.getInstance().getExerciseDates();
        Collections.sort(mExercises, new StringDateComparator());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mExercises);
        ListView listView = (ListView) super.getView().findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(super.getContext(), "Item clicked: " + mExercises.get(position), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), EditorActivity.class);
        intent.putExtra("date_string", mExercises.get(position));
        startActivity(intent);

    }
}