/**
 * Project Name:okdeer-user. Package Name:com.okdeer.store.seller.my.order.utils. Date:2017/2/21
 * 20:11. Copyright (c) 2017, 广东云上城网络科技有限公司.
 */

package com.chunlangjiu.app.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Shader;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.ThumbnailUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.chunlangjiu.app.R;


/**
 * .ClassName: RatingBarUtils(用一句话描述这个类的作用是做什么) date: 2017/2/21 20:11
 *
 * @author wushf
 */
public class RatingBarUtils {

    /**
     * setRatingBarStyle:【设置星级】. <br/>
     * .@param ratingBar .@param stepSize .@param rating .@param max .@param size.<br/>
     */
    public static void setRatingBarStyle(Context context, RatingBar ratingBar, float stepSize,
        float rating, int max, int size) {
        Drawable ratingBardrawable;

        Bitmap bitmapEmpty =
            BitmapFactory.decodeResource(context.getResources(), R.mipmap.uncollect);
        Bitmap bitmapFull =
            BitmapFactory.decodeResource(context.getResources(), R.mipmap.collect);
        Bitmap resizeBmp1 = ThumbnailUtils.extractThumbnail(bitmapEmpty, size, size);
        Bitmap resizeBmp2 = ThumbnailUtils.extractThumbnail(bitmapFull, size, size);
        Bitmap[] bitmaps = new Bitmap[] {resizeBmp1, resizeBmp1, resizeBmp2};
        ratingBardrawable = buildRatingBarDrawables(bitmaps);
        bitmapEmpty = null;
        bitmapFull = null;
        ViewGroup.LayoutParams layoutParams = ratingBar.getLayoutParams();
        layoutParams.width = 5 * size;
        layoutParams.height = size;
        ratingBar.setLayoutParams(layoutParams);
        ratingBar.setProgressDrawable(ratingBardrawable);
        ratingBar.setMax(max);
        ratingBar.setRating(rating);
        ratingBar.setStepSize(stepSize);
    }

    /**
     * buildRatingBarDrawables:【画规定大小的五角星】. <br/>
     * .@param images .@return.<br/>
     */
    private static Drawable buildRatingBarDrawables(Bitmap[] images) {
        final int[] requiredIds =
            {android.R.id.background, android.R.id.secondaryProgress, android.R.id.progress};
        final float[] roundedCorners = new float[] {5, 5, 5, 5, 5, 5, 5, 5};
        Drawable[] pieces = new Drawable[3];
        for (int i = 0; i < 3; i++) {
            ShapeDrawable sd = new ShapeDrawable(new RoundRectShape(roundedCorners, null, null));
            BitmapShader bitmapShader =
                new BitmapShader(images[i], Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            sd.getPaint().setShader(bitmapShader);
            ClipDrawable cd = new ClipDrawable(sd, Gravity.LEFT, ClipDrawable.HORIZONTAL);
            if (i == 0) {
                pieces[i] = sd;
            } else {
                pieces[i] = cd;
            }
        }
        LayerDrawable ld = new LayerDrawable(pieces);
        for (int i = 0; i < 3; i++) {
            ld.setId(i, requiredIds[i]);
        }
        return ld;
    }
}
