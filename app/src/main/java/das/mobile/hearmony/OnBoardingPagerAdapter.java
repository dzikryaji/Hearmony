package das.mobile.hearmony;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import das.mobile.hearmony.databinding.OnboardingItemBinding;

public class OnBoardingPagerAdapter extends RecyclerView.Adapter<OnBoardingPagerAdapter.ViewHolder> {

    List<Integer> imgList;
    List<String> txtList;

    public OnBoardingPagerAdapter(List<Integer> imgList, List<String> txtList) {
        this.imgList = imgList;
        this.txtList = txtList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(OnboardingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int img = imgList.get(position);
        String txt = txtList.get(position);
        holder.binding.tvTitle.setText(txt);
        holder.binding.ivMain.setImageResource(img);
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        OnboardingItemBinding binding;
        public ViewHolder(@NonNull OnboardingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
