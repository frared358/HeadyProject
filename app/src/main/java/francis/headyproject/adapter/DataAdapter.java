package francis.headyproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import francis.headyproject.R;
import francis.headyproject.pojo.AllData;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyHolder> {

    private Context context;
    private ArrayList<AllData> list;

    public DataAdapter(Context context, ArrayList<AllData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DataAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_varients, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.MyHolder holder, int i) {
        AllData obj = list.get(i);
        if (obj.color.equalsIgnoreCase("null")) {
            holder.tv_Color.setText(context.getResources().getString(R.string.not_available));
        }else {
            holder.tv_Color.setText(obj.color);
        }
        if (obj.size.equalsIgnoreCase("null")) {
            holder.tv_Size.setText(context.getResources().getString(R.string.not_available));
        }else {
            holder.tv_Size.setText(obj.size);
        }
        if (obj.price.equalsIgnoreCase("null")) {
            holder.tv_Price.setText(context.getResources().getString(R.string.not_available));
        }else {
            holder.tv_Price.setText(obj.price);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder  extends RecyclerView.ViewHolder {
        TextView tv_Color,tv_Size,tv_Price;

        MyHolder(@NonNull View v) {
            super(v);
            tv_Color = v.findViewById(R.id.tv_color);
            tv_Size = v.findViewById(R.id.tv_size);
            tv_Price = v.findViewById(R.id.tv_price);
        }
    }
}
