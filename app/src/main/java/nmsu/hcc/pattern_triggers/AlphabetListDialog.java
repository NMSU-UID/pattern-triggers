package nmsu.hcc.pattern_triggers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Window;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import nmsu.hcc.pattern_triggers.adapters.AlphabetListAdapter;
import nmsu.hcc.pattern_triggers.databinding.DialogAlphabetListBinding;
import nmsu.hcc.pattern_triggers.model.Alphabet;


public class AlphabetListDialog extends Dialog {
    private Context context;
    private Dialog alertDialog;
    private DialogAlphabetListBinding dialogAlphabetListBinding;
    private ButtonClickListener buttonClickListener;
    private AlphabetListAdapter alphabetListAdapter;

    ArrayList<Alphabet> alphabetList;
    int selectedItemPosition;
    String headerText;

    private boolean isCancelable = true;

    public interface ButtonClickListener {
        void onButtonClick(Alphabet alphabet);
    }

    public void getButtonCLickListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public AlphabetListDialog(Context context, ArrayList<Alphabet> alphabetList, int selectedItem, String headerText) {
        super(context);
        this.context = context;
        this.alphabetList = alphabetList;
        this.selectedItemPosition = selectedItem;
        this.headerText = headerText;
        alertDialog = new Dialog(context, R.style.CustomDialog); // ** no style defined **
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void show() {
        dialogAlphabetListBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_alphabet_list, null, false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(dialogAlphabetListBinding.getRoot());

        dialogAlphabetListBinding.rvAlphabetList.setLayoutManager(new GridLayoutManager(context, 4));
        alphabetListAdapter = new AlphabetListAdapter(context, alphabetList, selectedItemPosition);
        dialogAlphabetListBinding.rvAlphabetList.setAdapter(alphabetListAdapter);
        alphabetListAdapter.setOnItemClickListener((position, alphabet) -> {
            this.selectedItemPosition = position;
        });

        dialogAlphabetListBinding.tvHeaderText.setText(headerText);
        dialogAlphabetListBinding.ivCross.setOnClickListener(view -> alertDialog.dismiss());
        dialogAlphabetListBinding.btnDone.setOnClickListener(view -> {
            buttonClickListener.onButtonClick(LocalStorage.getInstance().getAlphabetById(this.selectedItemPosition));
        });

        alertDialog.setCancelable(isCancelable);

        try {
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
    }

    public void dismissDialog() {
        alertDialog.dismiss();
    }

    public Dialog getAlertDialog() {
        return alertDialog;
    }

    public boolean isShowing() {
        if (alertDialog != null && alertDialog.isShowing()) return true;
        else return false;
    }
}
