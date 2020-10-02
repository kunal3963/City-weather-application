package kunal.weather.ui;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import kunal.weather.R;
import kunal.weather.data.model.currentweather.CurrentWeatherModel;

import static kunal.weather.utils.Utils.getDate;

public class CurrentWeatherAdapter extends RecyclerView.Adapter<CurrentWeatherAdapter.MyViewHolder> implements Filterable {

    public interface OnItemClickListener {
        void onItemClick(CurrentWeatherModel item);
    }
    DecimalFormat df = new DecimalFormat("#.##");
    private List<CurrentWeatherModel> curWeatherList;
    private List<CurrentWeatherModel> curWeatherListFiltered;
    private Context context;
    private final OnItemClickListener listener;

    public CurrentWeatherAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setCurWeatherList(Context context, final List<CurrentWeatherModel> curWeatherList){
        this.context = context;
        if(this.curWeatherList == null){
            this.curWeatherList = curWeatherList;
            this.curWeatherListFiltered = curWeatherList;
            notifyItemChanged(0, curWeatherListFiltered.size());
        } else {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return CurrentWeatherAdapter.this.curWeatherList.size();
                }

                @Override
                public int getNewListSize() {
                    return curWeatherList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return CurrentWeatherAdapter.this.curWeatherList.get(oldItemPosition).getCity() == curWeatherList.get(newItemPosition).getCity();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

                    CurrentWeatherModel newWeather = CurrentWeatherAdapter.this.curWeatherList.get(oldItemPosition);

                    CurrentWeatherModel oldWeather = curWeatherList.get(newItemPosition);

                    return newWeather.getCity() == oldWeather.getCity() ;
                }
            });
            this.curWeatherList = curWeatherList;
            this.curWeatherListFiltered = curWeatherList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public CurrentWeatherAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_adapter,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(CurrentWeatherAdapter.MyViewHolder holder, int position) {
        holder.city.setText(curWeatherListFiltered.get(position).getCity());
        holder.country.setText(curWeatherListFiltered.get(position).getCountry());
        holder.date.setText(getDate(curWeatherListFiltered.get(position).getTimestamp()));
        Double value = Double.valueOf(curWeatherListFiltered.get(position).getTemp())-273.15;
        holder.temp.setText( df.format(value)+ "\u2103");
        holder.main.setText(curWeatherListFiltered.get(position).getMain());
        holder.humidity.setText("Humidity "+curWeatherListFiltered.get(position).getHumidity()+ "%");
        holder.wind.setText("Wind "+curWeatherListFiltered.get(position).getWindSpeed()+ "km/hr");
        holder.bind(curWeatherListFiltered.get(position), this.listener);
    }

    @Override
    public int getItemCount() {

        if(curWeatherList != null) {
            if (curWeatherListFiltered != null) {
                return curWeatherListFiltered.size();
            }
        }
            return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if(curWeatherList==null || curWeatherList.size()<=0){
                    return null;
                }
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    curWeatherListFiltered = curWeatherList;
                } else {

                    List<CurrentWeatherModel> filteredList = new ArrayList<>();
                    for (CurrentWeatherModel curWeatherCity : curWeatherList) {
                        if (curWeatherCity.getCity().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(curWeatherCity);
                        }
                    }
                    curWeatherListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = curWeatherListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(filterResults == null){
                    return;
                }
                curWeatherListFiltered = (ArrayList<CurrentWeatherModel>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView city, country, date, temp, main, humidity, wind;
        public MyViewHolder(View view) {
            super(view);
            city = (TextView) view.findViewById(R.id.city_name);
            country = (TextView) view.findViewById(R.id.country_code);
            date = (TextView) view.findViewById(R.id.date);
            temp = (TextView) view.findViewById(R.id.temp);
            main = (TextView) view.findViewById(R.id.main);
            humidity= (TextView) view.findViewById(R.id.humidity);
            wind= (TextView) view.findViewById(R.id.wind);
        }
        public void bind(final CurrentWeatherModel item, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }

    }
}
