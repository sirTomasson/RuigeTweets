package ap.tomassen.online.ruigetweets.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import ap.tomassen.online.ruigetweets.R;

/**
 * A fragment that represents a search bar in which the user can input text to search for
 * within the twitter api
 */
public class SearchFragment extends Fragment {
    ImageView searchButton;
    String searchText;
    EditText searchEditText;
    public CallbackListener listener;
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            listener = (CallbackListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchButton= (ImageView) view.findViewById(R.id.iv_search_tweet);
        searchEditText = (EditText) view.findViewById(R.id.et_search_tweet);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText = String.valueOf(searchEditText.getText());
                listener.searchTweet(searchText);
            }
        });

        return view;
    }

    public interface CallbackListener {
        /**
         * searches the twitter api for tweets based on a query made by the user
         * @param searchText input from the user
         */
        void searchTweet(String searchText);
    }

}
