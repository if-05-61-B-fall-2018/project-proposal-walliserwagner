package smartshoppinglist.at.smartshoppinglist.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.support.v4.app.ListFragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import smartshoppinglist.at.smartshoppinglist.AddItemPopUp;
import smartshoppinglist.at.smartshoppinglist.CreateItemPopUp;
import smartshoppinglist.at.smartshoppinglist.activitys.MainActivity;
import smartshoppinglist.at.smartshoppinglist.R;
import smartshoppinglist.at.smartshoppinglist.objects.Item;
import smartshoppinglist.at.smartshoppinglist.objects.ItemContainer;
import smartshoppinglist.at.smartshoppinglist.objects.ItemList;


import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SearchFragment extends ListFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private List<String> itemNames;
    private ItemList items;
    private ArrayAdapter<String> mAdapter;
    private Context mContext;
    private String text;
    private PopupWindow createItemPopUp;
    private PopupWindow addItemPopUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
        items = ((MainActivity)getActivity()).getItems();
        itemNames = items.getItemNames();
        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, itemNames);
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView listView, View v, int position, long id) {
        String itemName = (String) listView.getAdapter().getItem(position);
        final Item item = items.FindItemByName(itemName);
        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.fragment_search_ll);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        View popup = inflater.inflate(R.layout.add_item_popup, null);

        addItemPopUp = new PopupWindow(
                popup,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        if(Build.VERSION.SDK_INT>=21){
            addItemPopUp.setElevation(5.0f);
        }
        TextView name = popup.findViewById(R.id.add_item_popup_name);
        name.setText(item.getName());
        final EditText count = popup.findViewById(R.id.add_item_popup_count);
        count.setText("1");
        final EditText unit = popup.findViewById(R.id.add_item_popup_unit);
        unit.setText(item.getDefaultUnit());
        Button add = popup.findViewById(R.id.add_item_popup_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ItemContainer itemContainer = new ItemContainer(item, Integer.parseInt(count.getText().toString()),unit.getText().toString());
                    ((MainActivity)getActivity()).getShoppinglist().addItem(itemContainer);
                    getActivity().onBackPressed();
                    getFragmentManager().popBackStack();
                }catch (Exception e){}
            }
        });
        addItemPopUp.setTouchable(true);
        addItemPopUp.setFocusable(true);
        addItemPopUp.showAtLocation(ll, Gravity.CENTER,0,-500);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_search, container, false);
        final ListView listView = (ListView) layout.findViewById(android.R.id.list);
        Button emptyTextView = (Button) layout.findViewById(R.id.searchfragment_create_item);
        listView.setEmptyView(emptyTextView);

        emptyTextView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {

                LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.fragment_search_ll);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                View popup = inflater.inflate(R.layout.create_item_popup, null);

                createItemPopUp = new PopupWindow(
                        popup,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT
                );
                if(Build.VERSION.SDK_INT>=21){
                    createItemPopUp.setElevation(5.0f);
                }

                final AutoCompleteTextView category = (AutoCompleteTextView) popup.findViewById(R.id.autocomplete_category);

                String[] categories = (((MainActivity)getActivity()).getItemCategorys().getNames().toArray(new String[0]));
                String itemName = text;
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, categories);
                category.setAdapter(adapter);

                final Item item = new Item(itemName);
                EditText name = popup.findViewById(R.id.create_item_popup_name);
                name.setText(item.getName());
                final EditText count = popup.findViewById(R.id.create_item_popup_count);
                count.setText("1");
                final EditText unit = popup.findViewById(R.id.create_item_popup_unit);
                unit.setText(item.getDefaultUnit());
                category.setOnTouchListener(new View.OnTouchListener(){
                    @Override
                    public boolean onTouch(View v, MotionEvent event){
                        category.showDropDown();
                        return false;
                    }
                });
                Button add = popup.findViewById(R.id.create_item_popup_add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            item.setCategory(category.getText().toString());
                            ItemContainer itemContainer = new ItemContainer(item, Integer.parseInt(count.getText().toString()), unit.getText().toString());
                            ((MainActivity)getActivity()).getShoppinglist().addItem(itemContainer);
                            getActivity().onBackPressed();
                            getFragmentManager().popBackStack();
                        } catch (Exception e) {
                        }
                    }
                });
                createItemPopUp.setTouchable(true);
                createItemPopUp.setFocusable(true);
                createItemPopUp.showAtLocation(ll, Gravity.CENTER,0,-500);
            }
        });
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        View view = getActivity().findViewById(R.id.fab);
        view.setVisibility(View.INVISIBLE);
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Suchen");
        searchView.setIconified(false);
        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = (ImageView) searchView.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }

        });
    }
    public void onBackPressed()
    {
        if(createItemPopUp != null && createItemPopUp.isShowing()){
            createItemPopUp.dismiss();
        }
        else if(addItemPopUp != null && addItemPopUp.isShowing()){
            addItemPopUp.dismiss();
        }
        else{
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }

        List<String> filteredValues = new ArrayList<String>(itemNames);
        for (String value : itemNames) {
            if (!value.toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }

        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, filteredValues);
        setListAdapter(mAdapter);
        text = newText;
        return false;
    }

    public void resetSearch() {
        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, itemNames);
        setListAdapter(mAdapter);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    public interface OnItem1SelectedListener {
        void OnItem1SelectedListener(String item);
    }

}