package retrofit.cybersoft.testretrofit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by nagendra on 06/09/16.
 */
public class QuestionsAdapter extends BaseAdapter implements View.OnClickListener {
    private List<Question> questions;
    private Context mContext;
    public QuestionsAdapter(Context context,List<Question> list){
        mContext = context;
        questions = list;
    }
    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Question getItem(int position) {
        return questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).questionId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.question_list_item,null);
        }
        Question q= getItem(position);
        ((TextView)convertView.findViewById(R.id.text1)).setText(q.title);
        ImageView owner = (ImageView)convertView.findViewById(R.id.owner);
        Picasso.with(mContext).load(q.owner.profile_image).fit().centerInside().into(owner);
        owner.setTag(position);
        owner.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int position = (int)v.getTag();
        Intent i = new Intent(mContext, WebActivity.class);
        i.putExtra("URL",getItem(position).owner.link);
        mContext.startActivity(i);
    }
}
