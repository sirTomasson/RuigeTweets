package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Date;

import ap.tomassen.online.ruigetweets.R;


/**
 * This fragment can be used to write a status update and is displayed in front of the current visible elements
 */
public class UpdateStatusFragment extends Fragment {

    private final int STATUS_LENGTH = 140;  // max length of a status
    private int charCount = STATUS_LENGTH;

    private EditText etWriteStatus;
    private TextView tvCharCount;


    private CallbackListener listener;

    public interface CallbackListener {
        void sendTweet(String tweet);
    }

    public UpdateStatusFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CallbackListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_write_tweet, container, false);

        tvCharCount = (TextView) rootView.findViewById(R.id.tv_char_count);
        tvCharCount.setText(STATUS_LENGTH + "");
        TextView tvDateTime = (TextView) rootView.findViewById(R.id.tv_date_time);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String s = dateFormatter.format(today);

        tvDateTime.setText(s);

        etWriteStatus = (EditText) rootView.findViewById(R.id.et_write_tweet);

        etWriteStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                charCount = STATUS_LENGTH - charSequence.length();
                tvCharCount.setText(charCount + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= STATUS_LENGTH) {
                    editable.delete(STATUS_LENGTH, editable.length());
                }
            }
        });

        Button btnSendTweet = (Button) rootView.findViewById(R.id.btn_send_tweet);
        btnSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweet = etWriteStatus.getText().toString();

                listener.sendTweet(tweet);
            }
        });

        return rootView;
    }
}
