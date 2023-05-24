package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.part2_1_firstactivity.R;

import java.util.List;
import java.util.Map;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {
    private List<Map> itemList;

    public CustomListAdapter(List<Map> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map item = itemList.get(position);
        // 항목을 화면에 표시하는 로직 작성

        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder 내부에 필요한 뷰들을 선언

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // 뷰 초기화 및 이벤트 처리 등의 로직 작성
        }

        public void bind(Map item) {
            TextView textViewProductName = itemView.findViewById(R.id.textViewProductName);
            TextView textViewKey = itemView.findViewById(R.id.textViewKey);

            String productName = (String) item.get("productName");
            String key = (String) item.get("key");

            textViewProductName.setText(productName);
            textViewKey.setText(key);

            // 이벤트 처리 등 추가 작업 수행
        }
    }
}
