package com.example.blath.around.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.Operations;
import com.example.blath.around.commons.Utils.app.AroundAppHandles;
import com.example.blath.around.events.GetChatUsersListEvent;
import com.example.blath.around.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import de.greenrobot.event.EventBus;

public class HomeMessageFragment extends Fragment {
    ListView chatUserList;
    ArrayList<String> usernames;
    ArrayList<String> userIDs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_message, container, false);
        chatUserList = (ListView) view.findViewById(R.id.chat_user_list);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(AroundAppHandles.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        Operations.getChatUsersList(sharedPreferences.getString(User.KEY_USER_ID, "1111111"));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void onEventMainThread(GetChatUsersListEvent result) {
//        mView.findViewById(R.id.progress_overlay_container).setVisibility(View.GONE);
        try {
            JSONObject chatUsersListJson = new JSONObject(result.getResponseObject());
            extractUsers(chatUsersListJson);
        } catch (Exception e) {

        }
    }

    private void extractUsers(JSONObject chatUserListJson){
        int totalChatUsers = 0;
        usernames = new ArrayList<>();
        userIDs = new ArrayList<>();
        Iterator usersListIterator = chatUserListJson.keys();
        while(usersListIterator.hasNext()){
            String userID = usersListIterator.next().toString();
            userIDs.add(userID);
            try {
                JSONObject chatUser = chatUserListJson.getJSONObject(userID);
                usernames.add(chatUser.getString("name"));
                totalChatUsers++;
            }catch (JSONException jsonException){

            }
        }

        if(totalChatUsers > 0){
            chatUserList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, usernames));
        }else{

        }
    }
}
