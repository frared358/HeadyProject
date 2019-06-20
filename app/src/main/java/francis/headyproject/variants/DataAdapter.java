package francis.headyproject.variants;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyHolder> {

    Context context;

    @NonNull
    @Override
    public DataAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.MyHolder myHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyHolder  extends RecyclerView.ViewHolder {
        public MyHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
