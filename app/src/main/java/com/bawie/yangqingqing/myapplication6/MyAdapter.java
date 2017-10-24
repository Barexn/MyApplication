package com.bawie.yangqingqing.myapplication6;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<MyBean.DataBean> list;
    private int lastAnimatedPosition=-1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private int itemCount=0;

    public MyAdapter(Context context, List<MyBean.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context,R.layout.activity_item,null);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    private void runEnterAnimation(View view,int position){
        if (animationsLocked) return;
        if (position > lastAnimatedPosition) {//lastAnimatedPosition是int类型变量，一开始为-1，这两行代码确保了recycleview滚动式回收利用视图时不会出现不连续的效果
            lastAnimatedPosition = position;
            view.setTranslationY(500);//相对于原始位置下方400
            view.setAlpha(0.f);//完全透明
            //每个item项两个动画，从透明到不透明，从下方移动到原来的位置
            //并且根据item的位置设置延迟的时间，达到一个接着一个的效果
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)//根据item的位置设置延迟时间，达到依次动画一个接一个进行的效果
                    .setInterpolator(new DecelerateInterpolator(0.5f))//设置动画效果为在动画开始的地方快然后慢
                    .setDuration(700)
                    .setListener(new AnimatorListenerAdapter() {

                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;//确保仅屏幕一开始能够显示的item项才开启动画，也就是说屏幕下方还没有显示的item项滑动时是没有动画效果
                        }
                    })
                    .start();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        runEnterAnimation(holder.itemView,position);

        if (holder instanceof  MyViewHolder){
            MyViewHolder myViewHolder= (MyViewHolder) holder;
            ImageLoader.getInstance().displayImage(list.get(position).getUserImg(),myViewHolder.iv,MyApplication.options());
            myViewHolder.tv.setText(list.get(position).getUserName());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView tv;
        private final ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}
