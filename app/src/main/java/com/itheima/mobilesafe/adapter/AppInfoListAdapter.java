package com.itheima.mobilesafe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.ui.recycler_view.OnItemTouch;
import com.itheima.mobilesafe.utils.SystemInfoUtils;
import com.itheima.mobilesafe.utils.objects.AppInfo;

import java.util.Collections;
import java.util.List;

/**
 * Created by Catherine on 2016/8/19.
 * Soft-World Inc.
 * catherine919@soft-world.com.tw
 */
public class AppInfoListAdapter extends RecyclerView.Adapter<AppInfoListAdapter.MyViewHolder> implements OnItemTouch {

    private Context ctx;
    private List<AppInfo> appInfos;
    private OnItemClickLitener mOnItemClickLitener;
    private OnItemMoveListener mOnItemMoveListener;

    public AppInfoListAdapter(Context ctx, List<AppInfo> appInfos) {
        this.ctx = ctx;
        this.appInfos = appInfos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                ctx).inflate(R.layout.list_item_app_info, parent,
                false));
        return holder;
    }

    @Override
    public int getItemCount() {
        return appInfos.size();
    }

    public String getItemName(int position) {
        return appInfos.get(position).getName();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (mOnItemMoveListener != null)
            mOnItemMoveListener.onItemSwap(fromPosition, toPosition);
        Collections.swap(appInfos, fromPosition, toPosition);
        //非常重要，调用后Adapter才能知道发生了改变。
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        if (mOnItemMoveListener != null)
            mOnItemMoveListener.onItemSwipe(position);
        appInfos.remove(position);
        //非常重要，调用后Adapter才能知道发生了改变。
        notifyItemRemoved(position);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public interface OnItemMoveListener {
        void onItemSwap(int fromPosition, int toPosition);

        void onItemSwipe(int position);

    }

    /**
     * 注册监听器（交换位置、滑动删除）
     *
     * @param mOnItemMoveListener 监听器
     */
    public void setOnItemMoveLitener(OnItemMoveListener mOnItemMoveListener) {
        this.mOnItemMoveListener = mOnItemMoveListener;
    }

    /**
     * 注册监听器（点击、长按）
     *
     * @param mOnItemClickLitener 监听器
     */
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.iv_icon.setImageDrawable(appInfos.get(position).getIcon());
        holder.tv_name.setText(appInfos.get(position).getName());
        holder.tv_package_name.setText(appInfos.get(position).getPackageName());

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_icon;
        TextView tv_name;
        TextView tv_package_name;

        MyViewHolder(View view) {
            super(view);
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_package_name = (TextView) view.findViewById(R.id.tv_package_name);
        }
    }

}