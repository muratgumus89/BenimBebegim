package com.example.murat.benimbebegim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.murat.benimbebegim.adapters.ListViewAdapterForMoreEvents;

public class ActivityMoreEvents extends Fragment {
    String[] favName;
    int[] upLogo;
    ListView list;
    ListViewAdapterForMoreEvents adapter;

    String classes[] = {"ActivityHealth",
            "ActivityMedicine",
            "ActivityVaccination",
            "ActivityHygiene",
            "ActivityTeeth"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_moreevents, container,
                false);
        // Generate sample data
        favName = getResources().getStringArray(R.array.MoreEvents);


        upLogo = new int[] {R.drawable.ic_health_bullet,R.drawable.ic_medicine_bullet,R.drawable.ic_vaccination_bullet,
                R.drawable.ic_hygiene_bullet,R.drawable.ic_teeth_bullet};

        // Locate the ListView in fragmenttab1.xml
        list = (ListView) rootView.findViewById(R.id.listMoreEvents);

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapterForMoreEvents(getActivity(), favName, upLogo,null,null);
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
        // Capture clicks on ListView items
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Class ourClass = Class.forName("com.example.murat.benimbebegim." + classes[position]);
                    Intent intent = new Intent(getActivity(), ourClass);
                    startActivity(intent);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getActivity(), classes[position] + " aktivitesi yok", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
 
}