package com.bignerdranch.android.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;

/**
 * Created by hotun on 16.08.2017.
 */

public class SunsetFragment extends Fragment {
    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset,container,false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);
        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });

        return view;
    }

    private void startAnimation() {
        //Метод getTop() — один из четырех методов View, возвращающих прямоугольник локального макета для этого представления: getTop(), getBottom(), getRight() и getLeft(). Прямоугольник локального макета представления определяет позицию и размер этого представления
        // относительно его родителя на момент включения представления в макет. В принципе, положение представления на экране можно изменять изменением этих значений, но делать это не рекомендуется. Эти значения сбрасываются при каждом проходе обработки макета, поэтому присвоенные значения обычно долго не держатся.
        float sunYStart = mSunView.getTop();
        //Для перехода в новое состояние потребуется смещение на величину, равную высоте mSkyView, которая может быть определена вызовом getHeight(). Метод getHeight() возвращает результат, который также может быть получен по формуле getTop()-getBottom().
        float sunYEnd = mSkyView.getHeight();

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());//изначально движение замедленно затем ускоряется

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());//нужен для того что бы вычислить промежуточный цвет при переходе

       ObjectAnimator nightSkyAnimator = ObjectAnimator
               .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
               .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        //При вызове play(Animator) вы получаете объект AnimatorSet.Builder, который позволяет построить цепочку инструкций. Объект Animator, передаваемый play(Animator), является «субъектом» цепочки. Таким образом,
        // написанная нами цепочка вызовов может быть описана в виде «Воспроизвести heightAnimator с sunsetSkyAnimator; также воспроизвести heightAnimator до nightSkyAnimator». Возможно, в сложных разновидностях
        // AnimatorSet потребуется вызвать play(Animator) несколько раз; это вполне нормально.
        animatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
        animatorSet.start();
    }
}
