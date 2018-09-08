package com.fuj.enjoytv.activity.main.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.activity.chat_dtl.ChatDtlActivity;
import com.fuj.enjoytv.adapter.ChatAdapter;
import com.fuj.enjoytv.adapter.base.RVAdapter;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.model.chat.Chat;
import com.fuj.enjoytv.utils.AppManager;
import com.fuj.enjoytv.utils.JsonUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gang
 */
public class ChatFragment extends BaseFragment implements IChatContract.View {

    @Bind(R.id.tv_det_rv)
    RecyclerView mTVRV;

    private ChatAdapter mChatAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
        getData();
    }

    @Override
    protected void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.view_tv_det, container, false);
        ButterKnife.bind(this, root);
        initView();
    }

    @Override
    public void setPresenter(IChatContract.Presenter presenter) {

    }

    private void initAdapter() {
        mChatAdapter = new ChatAdapter(getContext(), new ArrayList<Chat>(), R.layout.item_swipe_chatlist);
    }

    @Override
    protected void initTitle(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initTitle(inflater, container, savedInstanceState);
        title = inflater.inflate(R.layout.title_chat, container, false);
    }

    private void initView() {
        mTVRV.setLayoutManager(new LinearLayoutManager(getContext()));
        setMarginTop(mTVRV);
        mTVRV.setAdapter(mChatAdapter);
        mChatAdapter.setOnItemClickListener(new RVAdapter.OnItemClickListener<Chat>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Chat chat, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppManager.BUNDLE_CHAT_DETAIL, chat);
                showActivity(ChatDtlActivity.class, bundle);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Chat chat, int position) {
                return false;
            }
        });
    }

    private void getData() {
        String content = JsonUtils.readJsonFile(getContext(), "chatlist");
        mChatAdapter.updateRecyclerView(JsonUtils.getObjectList(content, Chat.class));
    }
}
