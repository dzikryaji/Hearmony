package das.mobile.hearmony.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import das.mobile.hearmony.databinding.ItemInvestationBinding;

public class InvestationAdapter extends RecyclerView.Adapter<InvestationAdapter.InvestationViewHolder> {
    private final Context context;
    private List<String> investList;
    private String savingTenor;
    private String targetFunds;

    public InvestationAdapter(Context context, List<String> investList, String tenor, String target) {
        this.context = context;
        this.investList = investList;
        this.savingTenor = tenor;
        this.targetFunds = target.replaceAll("[^\\d]", "").replaceAll("\\d{2}$", ""); // Remove non-digit characters
    }

    @NonNull
    @Override
    public InvestationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvestationViewHolder(ItemInvestationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvestationViewHolder holder, int position) {
        String investmentDetails = investList.get(position);
        String[] detailsArray = investmentDetails.split(":");

        if (detailsArray.length >= 2) {
            String assetName = detailsArray[0].trim();
            String returnapy = detailsArray[1].trim();

            holder.binding.assetName.setText(assetName);
            int returnapyValue = (int) Math.floor(Double.parseDouble(returnapy));
            holder.binding.tvSave.setText(countReturn(returnapyValue, Long.parseLong(targetFunds), Integer.parseInt(savingTenor)));
            holder.binding.returnapy.setText(String.format(Locale.getDefault(), "1 Y return up to %s", returnapy));
        } else {
            holder.binding.assetName.setText("Invalid Format");
            holder.binding.returnapy.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return investList.size();
    }

    public class InvestationViewHolder extends RecyclerView.ViewHolder {
        private final ItemInvestationBinding binding;

        public InvestationViewHolder(@NonNull ItemInvestationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String countReturn(int yield, long target, int tenor) {
        double monthlyRate = (yield / 100.0) / 12.0;
        double compoundFactor = Math.pow((1 + monthlyRate), tenor);
        double result = (target/compoundFactor)/tenor;
        String formattedResult = String.format("%,d", (int) result);
        return formattedResult;
    }
}
