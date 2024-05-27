package com.example.project_2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_2.Models.AccountDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment extends Fragment {
    private View ContactsView;
    private RecyclerView myContactsList;
    private SearchView searchView;
    private DatabaseReference UsersRef;
    ArrayList<AccountDB> modelList;
    AccountDB accountDB;

    private final String accountType="Herbalist Account";

    private FirebaseAuth mAuth;
    private String currentUserID="";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ContactsView = inflater.inflate(R.layout.fragment_groups, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("users");

        myContactsList = (RecyclerView) ContactsView.findViewById(R.id.contacts_list);
        myContactsList.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView=ContactsView.findViewById(R.id.search_herbalist);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                accountDB=new AccountDB();
                accountDB.setAccountDBS(modelList);
                if(accountDB.searchHerbalist(query).isEmpty()){

                    Toast.makeText(getContext(),getString(R.string.searchherbalist),Toast.LENGTH_LONG).show();
                    herbalistAdapter myadapter=new herbalistAdapter(accountDB.searchHerbalist(query),getContext());
                    myContactsList.setAdapter(myadapter);
                }
                else {
                    herbalistAdapter myadapter=new herbalistAdapter(accountDB.searchHerbalist(query),getContext());
                    myContactsList.setAdapter(myadapter);
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                accountDB=new AccountDB();
                accountDB.setAccountDBS(modelList);
                if(accountDB.searchHerbalist(newText).isEmpty()){
                    Toast.makeText(getContext(),getString(R.string.searchherbalist),Toast.LENGTH_LONG).show();
                    herbalistAdapter myadapter=new herbalistAdapter(accountDB.searchHerbalist(newText),getContext());
                    myContactsList.setAdapter(myadapter);
                }
                else {
                    herbalistAdapter myadapter=new herbalistAdapter(accountDB.searchHerbalist(newText),getContext());
                    myContactsList.setAdapter(myadapter);
                }

                return false;
            }
        });


        return ContactsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //userDB userProfile=snapshot.getValue(userDB.class);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    modelList=new ArrayList<>();
                    AccountDB user;

                    for(DataSnapshot ds:snapshot.getChildren()){
                        user=ds.getValue(AccountDB.class);
                        if(user.getAccountType() !=null&& user.getId()!=null&&currentUserID!=null){
                            if(user.getAccountType().equals(accountType)&& !user.getId().equals(currentUserID)) {
                                modelList.add(ds.getValue(AccountDB.class));
                            }
                        }
                    }

                    herbalistAdapter myadapter=new herbalistAdapter(modelList,getContext());
                    myContactsList.setAdapter(myadapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}