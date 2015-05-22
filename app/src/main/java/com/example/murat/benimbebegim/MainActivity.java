package com.example.murat.benimbebegim;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.DiaryDatabase;
import com.example.murat.benimbebegim.adapters.ListViewAdapterForDiaries;
import com.example.murat.benimbebegim.model.AddDiary;
import com.example.murat.benimbebegim.model.EditDiary;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Fragment {

    ListView lv;
    ListViewAdapterForDiaries adapter;
    ArrayList<HashMap<String, String>> diary_list;
    String diary_titles[],diary_messages[],diary_paths[],diary_day_names[],day[],month[],year[];
    int count=0;
    int diary_id[];

    public static final String PREFS_NAME = "MyPrefsFile";
    String baby_id;

    private String outputFile = null;
    private Button start,stop,play;
    int position;
    TextView txtInfo;
    File mediaFile;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static MainActivity newInstance() {
        return new MainActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.diary_list, container, false);
        ActionBar ab = getActivity().getActionBar();
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        txtInfo = (TextView) rootView.findViewById(R.id.txtInfo);
        lv = (ListView) rootView.findViewById(R.id.listDiaries);
        registerForContextMenu(lv);


        setHasOptionsMenu(true);

        return rootView;
    }

    public void onResume()
    {   //neden onResume metodu kullandığımı ders içinde anlattım.
        super.onResume();
        //Get babyid from SP
        SharedPreferences pref;
        pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        baby_id = pref.getString("baby_id", null);
        Log.i("Baby_id : ",baby_id);
        DiaryDatabase db = new DiaryDatabase(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
        diary_list = db.diaries(baby_id);//kitap listesini alıyoruz
        Log.i("Diary List: ",diary_list.toString());

        if(diary_list.size()==0){//kitap listesi boşsa
                txtInfo.setText(getResources().getString(R.string.no_record) + "\n" +
                getResources().getString(R.string.add_item));
        }
        else{
            txtInfo.setVisibility(View.INVISIBLE);
            diary_titles    = new String[diary_list.size()];
            diary_messages  = new String[diary_list.size()];
            diary_paths     = new String[diary_list.size()];
            diary_id        = new int[diary_list.size()];
            diary_day_names = new String[diary_list.size()];
            day             = new String[diary_list.size()];
            month           = new String[diary_list.size()];
            year            = new String[diary_list.size()];
            for(int i=0;i<diary_list.size();i++){
                diary_titles[i]   = diary_list.get(i).get(DiaryDatabase.DIARY_TITLE);
                diary_messages[i] = diary_list.get(i).get(DiaryDatabase.DIARY_MESSAGE);
                diary_paths[i]    = diary_list.get(i).get(DiaryDatabase.DIARY_AUDIO_PATH);
                day[i]            = diary_list.get(i).get(DiaryDatabase.DIARY_DAY);
                month[i]          = diary_list.get(i).get(DiaryDatabase.DIARY_MONTH);
                year[i]           = diary_list.get(i).get(DiaryDatabase.DIARY_YEAR);
                diary_day_names[i]= diary_list.get(i).get(DiaryDatabase.DIARY_DAY_NAME);
                diary_id[i]       = Integer.parseInt(diary_list.get(i).get(DiaryDatabase.DIARY_ID));
            }

            count = db.getRowCount(baby_id);
            Log.i("Count: ",String.valueOf(count));
            adapter = new ListViewAdapterForDiaries(getActivity().getApplicationContext(),day,month,year,diary_day_names,diary_titles,diary_messages,diary_paths,count);
            lv.setAdapter(adapter);
        }

    }
    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }
    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v,
                                    final ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(getResources().getString(R.string.operations));
        menu.setHeaderIcon(R.drawable.icon_menu_32_diary);
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_diary, menu);
    }
    public MenuInflater getMenuInflater() {
        return new MenuInflater(getActivity().getApplicationContext());
    }
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        // TODO Auto-generated method stub
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        position = diary_id[info.position];

        switch (item.getItemId()) {
            case R.id.delete_context:
                setDialog();
                break;
            case R.id.edit_context:
                Intent intent = new Intent(getActivity().getApplicationContext(), EditDiary.class);
                intent.putExtra("id", position);
                startActivity(intent);
                break;
            case R.id.cancel_context:
                Intent intentMain = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intentMain
                );

            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void setDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity().getApplicationContext());

        // set title
        alertDialogBuilder.setTitle(getResources().getString(R.string.deleting_record) + "!!!");
        alertDialogBuilder.setIcon(R.drawable.icon_warning_32_diary);

        // set dialog message
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.are_you_sure))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.confirm),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        deleteRecord();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void deleteRecord() {
        DiaryDatabase db = new DiaryDatabase(getActivity().getApplicationContext());
        db.deleteDiary(position);
        Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.delete_record), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        startActivity(intent);//bu id li kitabı sildik ve Anasayfaya döndük
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                AddDiary();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void AddDiary() {
        Intent i = new Intent(getActivity().getApplicationContext(), AddDiary.class);
        startActivity(i);
    }

}
