package com.example.herarevisi.SplashScreen;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.herarevisi.R;

public class ObAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;

    public ObAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    private int titles[] = {
            R.string.title1,
            R.string.title2,
            R.string.title3,
            R.string.title4
    };

    private int descriptions[] = {
            R.string.description1,
            R.string.description2,
            R.string.description3,
            R.string.description4,
    };

    private int images[] = {
            R.drawable.undraw_nature,
            R.drawable.undraw_sentiment,
            R.drawable.undraw_confirmed,
            R.drawable.undraw_modern,
    };

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = layoutInflater.inflate(R.layout.page_ob, container, false);

        ImageView image = v.findViewById(R.id.image);
        TextView title, description;
        title = v.findViewById(R.id.title);
        description = v.findViewById(R.id.description);

        image.setImageResource(images[position]);
        title.setText(titles[position]);
        description.setText(descriptions[position]);

        container.addView(v);
        return v;
    }
}
