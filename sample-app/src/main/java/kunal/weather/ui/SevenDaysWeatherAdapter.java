package kunal.weather.ui;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import kunal.weather.R;
import kunal.weather.data.model.sevendayweather.SevenDaysModel;

import static kunal.weather.utils.Utils.getDate;

public class SevenDaysWeatherAdapter extends RecyclerView.Adapter<SevenDaysWeatherAdapter.MyViewHolder> {

    private static final String TAG = "SevenDaysWeatherAdapter";

    private List<SevenDaysModel> FiveDaysWeatherList;
    private Context context;
    DecimalFormat df = new DecimalFormat("#.##");

    public void setFiveDaysWeatherList(Context context, final List<SevenDaysModel> fiveDaysWeatherList){
        this.context = context;
        if(this.FiveDaysWeatherList == null){
            this.FiveDaysWeatherList = fiveDaysWeatherList;
            notifyItemChanged(0, FiveDaysWeatherList.size());
        } else {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return SevenDaysWeatherAdapter.this.FiveDaysWeatherList.size();
                }

                @Override
                public int getNewListSize() {
                    return fiveDaysWeatherList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return SevenDaysWeatherAdapter.this.FiveDaysWeatherList.get(oldItemPosition).getCity() == fiveDaysWeatherList.get(newItemPosition).getCity();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

                    SevenDaysModel item = SevenDaysWeatherAdapter.this.FiveDaysWeatherList.get(oldItemPosition);

                    SevenDaysModel oldItem = fiveDaysWeatherList.get(newItemPosition);

                    return item.getCity() == oldItem.getCity() ;
                }
            });
            this.FiveDaysWeatherList = fiveDaysWeatherList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public SevenDaysWeatherAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_seven_days_list_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SevenDaysWeatherAdapter.MyViewHolder holder, int position) {
        holder.date.setText(getDate(FiveDaysWeatherList.get(position).getTimestamp()));
        holder.main.setText(FiveDaysWeatherList.get(position).getMain());
        Double value = Double.valueOf(FiveDaysWeatherList.get(position).getTempMin())-273.15;
        holder.minTemp.setText( df.format(value)+ "\u2103");
        value = Double.valueOf(FiveDaysWeatherList.get(position).getTempMax())-273.15;
        holder.maxTemp.setText( df.format(value)+ "\u2103");
    }

    @Override
    public int getItemCount() {

        if(FiveDaysWeatherList != null){
            return FiveDaysWeatherList.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, main, minTemp, maxTemp;
        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            main = (TextView) view.findViewById(R.id.main);
            minTemp= (TextView) view.findViewById(R.id.min_temp);
            maxTemp= (TextView) view.findViewById(R.id.max_temp);
        }
    }
}
