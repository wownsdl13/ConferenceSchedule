package util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.conference.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import server.GetTimeTableServer;
import server.ServerRoot;

public class getTimeTable {
    Activity activity;
    private Button leftBtn, rightBtn;
    private TextView timeText;
    private HashMap<Integer, ConferenceBookVO> list = new HashMap<Integer, ConferenceBookVO>();
    private Adapter adapter;
    private RecyclerView recyclerView;

    public getTimeTable(Activity activity) {
        this.activity = activity;
        recyclerView = activity.findViewById(R.id.recyclerView);
        leftBtn = activity.findViewById(R.id.leftBtn);
        rightBtn = activity.findViewById(R.id.rightBtn);
        timeText = activity.findViewById(R.id.timeText);
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        adapter = new Adapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

        list.put(1, new ConferenceBookVO(1, 1, 201906150930L, 0, "연구실 회의"));
        list.put(2, new ConferenceBookVO(1, 1, 201906151000L, 0, "연구실 회의"));
        list.put(3, new ConferenceBookVO(1, 1, 201906151030L, 0, "연구실 회의"));

        list.put(6, new ConferenceBookVO(1, 2, 201906151200L, 0, "코틀린 jetpack 스터디"));
        list.put(7, new ConferenceBookVO(1, 2, 201906151230L, 0, "코틀린 jetpack 스터디"));
        adapter.notifyDataSetChanged();
    }

    private void setDate(long date) throws JSONException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DATE);
        String timeStr = "";
        timeStr += String.valueOf(year);
        timeStr += month<10?"0"+month:month;
        timeStr += day<10?"0"+day:day;
        GetTimeTableServer getTimeTableServer = new GetTimeTableServer(activity, "put serverURL here").setInfo(Integer.parseInt(timeStr));
        getTimeTableServer.setLister(new ServerRoot.OnAfterLister() {
            @Override
            public void after(String result) {
                try {
                    JSONObject o = new JSONObject(result);
                    JSONArray array = o.getJSONArray("array");
                    for(int i = 0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        ConferenceBookVO vo = new ConferenceBookVO(object.getInt("pk"), object.getInt("bookPk"), object.getLong("time"), object.getInt("confirm"), object.getString("title"));
                        long time = vo.getTime()%10000;
                        int hour = (int) (time/100 - 9);
                        int minute = time%100==30?1:0;
                        list.put(hour+minute, vo);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        getTimeTableServer.run();
    }

    class Adapter extends RecyclerView.Adapter<Holder>{

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = View.inflate(activity, R.layout.conference_table, null);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(params);
            return new Holder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            TextView titleText = (TextView) holder.titleText;
            TextView timeText = holder.timeText;
            int hour = ((position/2)+9);
            String hourStr = hour<10?"0"+hour:String.valueOf(hour);
            int minute = (position%2==0?0:30);
            String minuteStr =  minute<10?"0"+minute:String.valueOf(minute);
            timeText.setText(hourStr+" : "+minuteStr);
            if(list.containsKey(position)){
                ConferenceBookVO vo = list.get(position);
                if(position>0 && list.containsKey(position-1) && list.get(position-1).getBookPk() == vo.getBookPk()){
                    titleText.setText("");
                }else{
                   titleText.setText(vo.getTitle());
                }
                titleText.setBackgroundColor(ActivityCompat.getColor(activity, R.color.blue));
                holder.itemView.setOnClickListener(null);
            }else{
                titleText.setBackgroundColor(ActivityCompat.getColor(activity, R.color.white));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //여기서 추가하기
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return 19;
        }
    }

    class Holder extends RecyclerView.ViewHolder{
        public TextView timeText, titleText;
        public Holder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.timeText);
            titleText = itemView.findViewById(R.id.titleText);

        }
    }
}
